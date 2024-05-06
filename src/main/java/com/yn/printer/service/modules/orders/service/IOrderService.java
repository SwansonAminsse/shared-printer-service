package com.yn.printer.service.modules.orders.service;

import com.yn.printer.service.modules.common.api.tz.dto.TzPayResultCallback;
import com.yn.printer.service.modules.enums.PayMode;
import com.yn.printer.service.modules.member.entity.Member;
import com.yn.printer.service.modules.operation.entity.DevicesList;
import com.yn.printer.service.modules.orders.entity.OrderManagement;
import com.yn.printer.service.modules.orders.enums.TransactionStatus;
import com.yn.printer.service.modules.orders.vo.PayInfoVo;
import com.yn.printer.service.modules.orders.vo.PrintOrderVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * 订单服务
 *
 * @author : Jonas Chan
 * @since : 2023/12/20 17:05
 */
public interface IOrderService {

    /**
     * 获取正在打印中的订单
     *
     * @param device 设备
     * @return 订单
     */
    Long findPrintOrderInProgress(DevicesList device);

    /**
     * 创建打印订单
     *
     * @param device 设备
     * @param member 用户
     * @param price  订单金额
     * @return
     */
    OrderManagement createPrintOrder(DevicesList device, Member member, BigDecimal price, PayMode payMode);

    /**
     * 获取支付信息
     *
     * @param orderId 订单id
     * @return
     */
    PayInfoVo getPayQrCode(Long orderId);

    Boolean handleTzPayCallback(TzPayResultCallback callback);

    Boolean handleWxPayCallback(String orderCode);

    /**
     * 支付成功
     *
     * @param order
     * @return
     */
    Boolean orderPaySuccess(OrderManagement order, BigDecimal amount);

    /**
     * 订单完成
     *
     * @param order
     * @return
     */
    void orderComplete(Long id);

    void orderAbnormal(Long id,String interruptReason);

    Page<PrintOrderVo> getPrintOrder(Pageable pageable);

    PrintOrderVo getPrintOrderVo(Long id);

    TransactionStatus getTransactionStatus(String code);

//    Boolean yuEPay(Long orderId);
}
