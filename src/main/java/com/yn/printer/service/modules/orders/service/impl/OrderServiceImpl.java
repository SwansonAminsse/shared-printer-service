package com.yn.printer.service.modules.orders.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.wechat.pay.java.service.payments.jsapi.model.*;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.yn.printer.service.common.exception.YnError;
import com.yn.printer.service.common.exception.YnErrorException;
import com.yn.printer.service.interceptor.AuditInterceptor;
import com.yn.printer.service.modules.common.api.tz.dto.*;
import com.yn.printer.service.modules.common.api.tz.service.TzPayService;
import com.yn.printer.service.modules.common.api.wx.dto.JsPayInfo;
import com.yn.printer.service.modules.common.api.wx.service.WeChatApiService;
import com.yn.printer.service.modules.common.service.IFileService;
import com.yn.printer.service.modules.enums.PayMode;
import com.yn.printer.service.modules.member.entity.Member;
import com.yn.printer.service.modules.member.service.IChargeFileService;
import com.yn.printer.service.modules.member.service.IPointsFileService;
import com.yn.printer.service.modules.operation.entity.DevicesList;
import com.yn.printer.service.modules.operation.service.IDeviceService;
import com.yn.printer.service.modules.orders.entity.OrderManagement;
import com.yn.printer.service.modules.orders.enums.OrderType;
import com.yn.printer.service.modules.orders.enums.PayStatus;
import com.yn.printer.service.modules.orders.enums.TransactionStatus;
import com.yn.printer.service.modules.orders.repository.OrderManagementRepository;
import com.yn.printer.service.modules.orders.repository.PrintTaskRepository;
import com.yn.printer.service.modules.orders.service.IOrderService;
import com.yn.printer.service.modules.orders.vo.PayInfoVo;
import com.yn.printer.service.modules.orders.vo.PrintOrderVo;
import com.yn.printer.service.modules.orders.vo.PrintTaskVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    TzPayService tzPayService;

    @Autowired
    WeChatApiService weChatApiService;

    @Autowired
    IDeviceService deviceService;

    @Autowired
    IFileService fileService;

    @Autowired
    PrintTaskRepository printTaskRepository;

    @Autowired
    OrderManagementRepository orderManagementRepository;

    @Autowired
    IPointsFileService pointsFileService;

    @Autowired
    IChargeFileService chargeFileService;

    // 定时关闭未支付订单
    @Scheduled(fixedDelay = 1000 * 60)
    public void orderAutoClose() {

        // 获取两分钟之前的订单
        Date now = DateUtil.offsetMinute(new Date(), -2);
        List<OrderManagement> unpaidList = orderManagementRepository.findAllByTransactionStatus(TransactionStatus.INIT)
                .stream().filter(item -> item.getCreatedOn().isBefore(DateUtil.toLocalDateTime(now)))
                .peek(item -> item.setTransactionStatus(TransactionStatus.CANCELED)).collect(Collectors.toList());
        orderManagementRepository.saveAll(unpaidList);

        if (!unpaidList.isEmpty())
            log.info("定时关闭未支付订单数量->{}", unpaidList.size());

    }
//将进行中的订单处理为异常
@Scheduled(fixedDelay = 1000 * 60 * 15)
public void orderStatusUpdate() {
    Date tenMinutesAgo = DateUtil.offsetMinute(new Date(), -30);
    List<OrderManagement> inProgressList = orderManagementRepository.findAllByTransactionStatus(TransactionStatus.IN_PROGRESS)
            .stream()
            .filter(item -> item.getCreatedOn().isBefore(DateUtil.toLocalDateTime(tenMinutesAgo)))
            .peek(item -> {
                item.setTransactionStatus(TransactionStatus.ABNORMAL);
                item.setAbnormalReason("网络异常，任务未成功推送");
            })
            .collect(Collectors.toList());
    orderManagementRepository.saveAll(inProgressList);
    if (!inProgressList.isEmpty())
        log.info("定时更新订单状态，将30分钟前的进行中订单改为异常状态->{}", inProgressList.size());
}

    @Override
    public Long findPrintOrderInProgress(DevicesList device) {
        return orderManagementRepository.countPrintOrderInProgress(device);
    }

    @Override
    public OrderManagement createPrintOrder(DevicesList device, Member member, BigDecimal price, PayMode payMode) {
        // 生成本地订单号
        String sn = DateUtil.format(new Date(), "yyyyMMddHHmmss") + RandomUtil.randomNumbers(6);

        if (price.compareTo(BigDecimal.ZERO) <= 0) throw new YnErrorException(YnError.YN_500003);

        // 生成待支付订单 保存支付金额, 支付单号
        OrderManagement order = new OrderManagement();
        order.setCode(sn);
        order.setDevice(device);
        order.setPayer(member);
        order.setOrderer(member);
        order.setOrderAmount(price);
        order.setTradeNo(sn);
        order.setPayStatus(PayStatus.UN_PAID);
        order.setTransactionStatus(TransactionStatus.INIT);
        order.setOrderType(OrderType.PRINT);
        order.setOrderDate(LocalDateTime.now());

        switch (payMode) {
            case TZ_PAY:
                // 调用天章支付生成支付订单
                TzCreateOrderRequest tzCreateOrderRequest = new TzCreateOrderRequest();
                tzCreateOrderRequest.setOrderId(sn);
                tzCreateOrderRequest.setMerDate(DateUtil.format(new Date(), "yyyyMMdd"));
                tzCreateOrderRequest.setAmount(String.valueOf(price.multiply(BigDecimal.valueOf(100)).intValue()));
                TzCreateOrderResponse response = tzPayService.createOrder(tzCreateOrderRequest);
                order.setQrCode(response.getQrCode());
                break;
            case WX_NATIVE_PAY:
                String wxPayLink = weChatApiService.createNativePayOrder(price, "订单支付", sn);
                order.setWxPayLink(wxPayLink);
                break;
            case WX_JS_PAY:
                // 调用微信支付生成小程序支付订单
                PrepayRequest jsPrepayRequest = new PrepayRequest();
                // 付款人
                Payer payer = new Payer();
                payer.setOpenid(member.getOpenId());
                jsPrepayRequest.setPayer(payer);
                // 订单金额
                Amount amount = new Amount();
                amount.setTotal(price.multiply(BigDecimal.valueOf(100)).intValue());
                jsPrepayRequest.setAmount(amount);
                // 订单描述
                jsPrepayRequest.setDescription("订单支付");
                // 本地订单号
                jsPrepayRequest.setOutTradeNo(sn);
                PrepayResponse prepayResponse = weChatApiService.createJsPayOrder(jsPrepayRequest);
                order.setWxPrepayId(prepayResponse.getPrepayId());
                break;
            case YU_E_PAY:
                    break;
            default:
                throw new YnErrorException(YnError.YN_700001);

        }

        order.setPayMode(payMode);
        orderManagementRepository.save(order);
        return order;
    }

    @Override
    public PayInfoVo getPayQrCode(Long orderId) {

        OrderManagement order = orderManagementRepository.findById(orderId).orElse(null);
        if (order == null) throw new YnErrorException(YnError.YN_500001);

        PayInfoVo payInfoVo = new PayInfoVo();
        switch (order.getPayMode()) {
            case TZ_PAY:
                payInfoVo.setQrCode(order.getQrCode());
                break;
            case WX_NATIVE_PAY:
                payInfoVo.setQrCode(order.getWxPayLink());
                break;
            case WX_JS_PAY:
                JsPayInfo jsPayInfo = new JsPayInfo();
                jsPayInfo.setTimeStamp(System.currentTimeMillis() / 1000);
                jsPayInfo.setNonceStr(RandomUtil.randomString(32).toUpperCase());
                jsPayInfo.setPackages("prepay_id=" + order.getWxPrepayId());
                jsPayInfo.setSignType("RSA");
                weChatApiService.signByRSA(jsPayInfo);
                payInfoVo.setJsPayInfo(jsPayInfo);
                break;
            default:
                throw new YnErrorException(YnError.YN_700001);
        }

        return payInfoVo;
    }

    @Override
    public Boolean handleTzPayCallback(TzPayResultCallback callback) {

        log.info("开始处理天章支付结果回调>>>{}", callback);

        OrderManagement order = orderManagementRepository.findByCode(callback.getOrderId());
        if (order == null) return false;

        TzQueryPayResultRequest request = new TzQueryPayResultRequest();
        request.setOrderId(callback.getOrderId());
        request.setMerDate(order.getCreatedOn().format(DateTimeFormatter.BASIC_ISO_DATE));
        TzQueryPayResultResponse response = tzPayService.queryPayResult(request);

        switch (response.getTradeState()) {
            case "20":
                log.info("未支付");
                return false;
            case "21":
                log.info("支付成功");
                return PayStatus.PAID.equals(order.getPayStatus()) ? true :
                        this.orderPaySuccess(order, new BigDecimal(response.getAmount()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
            case "22":
                log.info("支付失败");
                return false;
            case "23":
                log.info("支付中");
                return false;
        }

        return true;
    }

    @Override
    public Boolean handleWxPayCallback(String orderCode) {

        log.info("开始处理微信支付结果回调>>>{}", orderCode);

        OrderManagement order = orderManagementRepository.findByCode(orderCode);
        if (order == null) return false;

        QueryOrderByOutTradeNoRequest request = new QueryOrderByOutTradeNoRequest();
        request.setOutTradeNo(orderCode);
        Transaction transaction = weChatApiService.queryPayOrder(request);

        switch (transaction.getTradeState()) {
            case NOTPAY:
                log.info("未支付");
                return false;
            case SUCCESS:
                log.info("支付成功");
                return PayStatus.PAID.equals(order.getPayStatus()) ? true :
                        this.orderPaySuccess(order, new BigDecimal(transaction.getAmount().getPayerTotal()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
        }

        return null;
    }

    @Transactional
    @Override
    public Boolean orderPaySuccess(OrderManagement order, BigDecimal amount) {
        order.setPayStatus(PayStatus.PAID);
        order.setTransactionStatus(TransactionStatus.IN_PROGRESS);
        order.setPaymentAmount(amount);
        order.setPayDate(LocalDateTime.now());
        orderManagementRepository.save(order);
        deviceService.executePrintingTask(order);
        try {
            pointsFileService.creatAddPointsFile(amount, order.getOrderer());
        }catch (Exception ex) {
            log.error("Creating points file failed.", ex);
        }
        return true;
    }

    @Override
    public void orderComplete(Long id) {
        OrderManagement order = orderManagementRepository.findById(id).orElse(null);
        if (order!=null&&!TransactionStatus.COMPLETE.equals(order.getTransactionStatus())) {
        order.setTransactionStatus(TransactionStatus.COMPLETE);
        orderManagementRepository.save(order);
        }
    }

    @Override
    public void orderAbnormal(Long id,String interruptReason) {
        OrderManagement order = orderManagementRepository.findById(id).orElse(null);
        if (order!=null&&!TransactionStatus.ABNORMAL.equals(order.getTransactionStatus())) {
            order.setTransactionStatus(TransactionStatus.ABNORMAL);
            order.setAbnormalReason(interruptReason);
            orderManagementRepository.save(order);
        }
    }

    @Override
    public Page<PrintOrderVo> getPrintOrder(Pageable pageable) {

        Member member = AuditInterceptor.CURRENT_MEMBER.get();

        Page<OrderManagement> page = orderManagementRepository
                .findAllByPayStatusAndOrdererAndOrderTypeOrderByCreatedOnDesc(pageable, PayStatus.PAID, member, OrderType.PRINT);

        return new PageImpl<>(page.getContent().stream().map(item -> {
            PrintOrderVo vo = new PrintOrderVo();

            vo.setCreateTime(DateUtil.formatLocalDateTime(item.getCreatedOn()));
            vo.setShopName(item.getDevice().getTerminalMerchants().getName());
            vo.setDeviceName(item.getDevice().getName());
            vo.setAmount(item.getOrderAmount());
            vo.setTransactionStatus(item.getTransactionStatus());
            vo.setPayStatus(item.getPayStatus());
            vo.setPrintTaskVoList(printTaskRepository.findByOrders(item).stream().map(printTask -> {
                PrintTaskVo printTaskVo = new PrintTaskVo();

                BeanUtils.copyProperties(printTask, printTaskVo);
                printTaskVo.setFilePreviewUrl(fileService.findPreviewUrl(printTask.getTranscodingFile()));
                printTaskVo.setFileName(printTask.getSourceFileName());

                return printTaskVo;
            }).collect(Collectors.toList()));

            return vo;
        }).collect(Collectors.toList()), pageable, page.getTotalElements());
    }

    @Override
    public PrintOrderVo getPrintOrderVo(Long id) {

        OrderManagement order = orderManagementRepository.findById(id).orElse(null);
        if (order == null) throw new YnErrorException(YnError.YN_500001);

        PrintOrderVo vo = new PrintOrderVo();

        vo.setCreateTime(DateUtil.formatLocalDateTime(order.getCreatedOn()));
        vo.setShopName(order.getDevice().getTerminalMerchants().getName());
        vo.setDeviceName(order.getDevice().getName());
        vo.setAmount(order.getOrderAmount());
        vo.setTransactionStatus(order.getTransactionStatus());
        vo.setPayStatus(order.getPayStatus());
        vo.setPrintTaskVoList(printTaskRepository.findByOrders(order).stream().map(printTask -> {
            PrintTaskVo printTaskVo = new PrintTaskVo();

            BeanUtils.copyProperties(printTask, printTaskVo);
            printTaskVo.setFilePreviewUrl(fileService.findPreviewUrl(printTask.getTranscodingFile()));
            printTaskVo.setFileName(printTask.getTranscodingFile().getFileName());

            return printTaskVo;
        }).collect(Collectors.toList()));

        return vo;
    }

    @Override
    public TransactionStatus getTransactionStatus(String code){
        return orderManagementRepository.findTransactionStatusByCode(code);
    }

}
