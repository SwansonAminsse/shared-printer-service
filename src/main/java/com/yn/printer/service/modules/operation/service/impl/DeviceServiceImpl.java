package com.yn.printer.service.modules.operation.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import javax.persistence.EntityManager;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;
import com.yn.printer.service.common.exception.YnError;
import com.yn.printer.service.common.exception.YnErrorException;
import com.yn.printer.service.common.vo.AreaVO;
import com.yn.printer.service.interceptor.AuditInterceptor;
import com.yn.printer.service.modules.common.api.tentcentMap.service.inverseAddressResolution;
import com.yn.printer.service.modules.common.mqtt.MqttConfig;
import com.yn.printer.service.modules.common.mqtt.MqttSender;
import com.yn.printer.service.modules.common.mqtt.dto.*;
import com.yn.printer.service.modules.common.service.IFileService;
import com.yn.printer.service.modules.common.util.PdfUtil;
import com.yn.printer.service.modules.operation.enums.DeviceStatus;
import com.yn.printer.service.modules.member.entity.Member;
import com.yn.printer.service.modules.member.service.IMemberService;
import com.yn.printer.service.modules.meta.entity.*;
import com.yn.printer.service.modules.meta.enums.PaperType;
import com.yn.printer.service.modules.meta.enums.PrintFaces;
import com.yn.printer.service.modules.meta.enums.PrintFileType;
import com.yn.printer.service.modules.meta.enums.PrintType;
import com.yn.printer.service.modules.meta.repository.*;
import com.yn.printer.service.modules.operation.dto.PreprintTask;
import com.yn.printer.service.modules.operation.dto.SubmitPrintingTaskDto;
import com.yn.printer.service.modules.operation.entity.*;
import com.yn.printer.service.modules.operation.enums.OperationsType;
import com.yn.printer.service.modules.operation.repository.*;
import com.yn.printer.service.modules.operation.service.IDeviceService;
import com.yn.printer.service.modules.operation.util.ChannelUtils;
import com.yn.printer.service.modules.operation.vo.*;
import com.yn.printer.service.modules.orders.entity.OrderManagement;
import com.yn.printer.service.modules.orders.entity.PrintTask;
import com.yn.printer.service.modules.orders.enums.OrderPrintType;
import com.yn.printer.service.modules.orders.enums.PayStatus;
import com.yn.printer.service.modules.orders.enums.PrintTaskStatus;
import com.yn.printer.service.modules.orders.enums.TransactionStatus;
import com.yn.printer.service.modules.orders.repository.OrderManagementRepository;
import com.yn.printer.service.modules.orders.repository.PrintTaskRepository;
import com.yn.printer.service.modules.orders.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManagerFactory;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.yn.printer.service.modules.meta.enums.PrintDirection.VERTICAL;
import static com.yn.printer.service.modules.meta.enums.PrintFaces.DOUBLE;

/**
 * 设备模块服务
 *
 * @author : Jonas Chan
 * @since : 2023/12/18 18:00
 */
@Slf4j
@Service
public class DeviceServiceImpl implements IDeviceService {
    @Autowired
    DevicesListRepository repository;

    @Autowired
    TransactionTemplate transactionTemplate;
    @Autowired
    OrderManagementRepository orderManagementRepository;

    @Autowired
    FixPriceRepository fixPriceRepository;
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    @Autowired
    GoodsRepository goodsRepository;

    @Autowired
    MetaFileRepository metaFileRepository;

    @Autowired
    PrintTaskRepository printTaskRepository;

    @Autowired
    CommodityRepository commodityRepository;

    @Autowired
    PaperTableRepository paperTableRepository;

    @Autowired
    ConsumablesValueRepository consumablesValueRepository;

    @Autowired
    PaperWarningRepository paperWarningRepository;

    @Autowired
    ConsumableWarningRepository consumableWarningRepository;

    @Autowired
    TaskListRepository taskListRepository;

    @Autowired
    IOrderService orderService;

    @Autowired
    IFileService fileService;

    @Autowired
    IMemberService memberService;

    @Autowired
    MqttSender mqttSender;

    @Autowired
    MqttConfig mqttConfig;

    @Autowired
    ChannelUtils channelUtils;

    @Autowired
    inverseAddressResolution inverseAddressResolution;
    @Autowired
    DeviceInterfaceRepository deviceInterfaceRepository;

    @Autowired
     EntityManager entityManager;

    @Autowired
    ThirdPartyVouchersRepository thirdPartyVouchersRepository;
    @Autowired
    DeviceMetaRepository deviceMetaRepository;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    OperationDevicesListSupportingComponentsRepository operationDevicesListSupportingComponentsRepository;
    @Autowired
    OperationDevicesListDeviceInterfaceRepository operationDevicesListDeviceInterfaceRepository;

    @Override
    public String addNewDevice() {
        DevicesList newDevice = new DevicesList();
        String code = DateUtil.format(new Date(), "yyyyMMdd-");
        while (true) {
            String sn = RandomUtil.randomNumbers(6);
            if (repository.findByCode(code + sn) == null) {
                code += sn;
                break;
            }
        }

        newDevice.setCode(code);
        newDevice.setStatus(DeviceStatus.NOT_ACTIVE);

        repository.save(newDevice);

        return code;
    }

    @Override
    public DeviceVo findByCode(String code) {
        DevicesList device = repository.findByCode(code);
        if (device == null) throw new YnErrorException(YnError.YN_400001);
        if (DeviceStatus.NOT_ACTIVE.equals(device.getStatus())) throw new YnErrorException(YnError.YN_400012);

        DeviceVo deviceVo = deviceVo(device);
        Map<PaperType, Integer> map = new HashMap<>();
        List<PaperWarning> paperWarning = paperWarningRepository.findByXingHao(device.getXh());
        if (paperWarning != null && !paperWarning.isEmpty()) {
            paperWarning.forEach(item -> map.put(item.getPaperType(), item.getMaxPaperResidue()));
            deviceVo.setMaxReduction(map);
        }

        return deviceVo;
    }

    @Override
    public BigDecimal getPrintPrice(Long deviceId, PreprintTask task) {

        DevicesList device = repository.findById(deviceId).orElse(null);
        if (device == null) throw new YnErrorException(YnError.YN_400001);

        FixPrice fixPrice = device.getFixPrice();
        if (fixPrice == null) throw new YnErrorException(YnError.YN_400002);

        Goods goods = null;
        switch (task.getPrintType()) {
            case IdPrinting:
                goods = goodsRepository.findByPrintType(task.getPrintType());
                break;
            case PhotoPrinting:
            case DocumentPrinting:
                goods = goodsRepository.findByPrintTypeAndPrintColorAndPrintFaces(
                        task.getPrintType(), task.getPrintingColor(), task.getPrintingFaces());
                break;
        }
        if (goods == null) throw new YnErrorException(YnError.YN_400003);

        Commodity commodity = commodityRepository.findByGoodsAndFixPrice(goods, fixPrice);
        if (commodity == null || commodity.getPrice() == null) throw new YnErrorException(YnError.YN_400004);

        MetaFile metaFile = metaFileRepository.findById(task.getFileId()).orElse(null);
        if (metaFile == null) throw new YnErrorException(YnError.YN_400005);

        int pages = "pdf".equals(metaFile.getFileType()) ? Math.max(PdfUtil.getNumberOfPages(fileService.metaFile2File(metaFile)), 1) : 1;
        if (PrintFaces.DOUBLE.equals(task.getPrintingFaces()) && "pdf".equals(metaFile.getFileType())) {
            pages = pages % 2 == 0 ? pages : pages + 1;
        }
        return commodity.getPrice()
                .multiply(BigDecimal.valueOf(pages))
                .multiply(BigDecimal.valueOf(task.getCopies()));
    }

    @Transactional
    @Override
    public Boolean bindUser(String code) {

        Member member = AuditInterceptor.CURRENT_MEMBER.get();

        // 解绑旧设备
        DevicesList bound = repository.findByBindUser(member);
        if (bound != null) this.disBindUser(bound);

        // 绑定新设备
        DevicesList device = repository.findByCode(code);
        if (device == null) throw new YnErrorException(YnError.YN_400001);
//        if (device.getBindUser() != null) throw new YnErrorException(YnError.YN_400010);
        device.setBindUser(member);
        device.setBindTime(LocalDateTime.now());
        repository.save(device);

        MemberLoginPush memberLoginPush = new MemberLoginPush();
        BeanUtils.copyProperties(memberService.createLoginVo(member), memberLoginPush);
        this.mqttPush(device, JSON.toJSONString(memberLoginPush));

        return true;
    }

    @Override
    public Boolean disBindUser(String code) {
        DevicesList device = repository.findByCode(code);
        if (device == null) throw new YnErrorException(YnError.YN_400001);

        this.mqttPush(device, JSON.toJSONString(new MemberLogoutPush()));

        return disBindUser(device);
    }

    @Override
    public Boolean disBindUser(DevicesList device) {
        device.setBindUser(null);
        repository.save(device);
        return true;
    }

    @Override
    @Transactional
    public Long submitPrintingTask(SubmitPrintingTaskDto dto, Member member) {

        DevicesList device = repository.findById(dto.getDeviceId()).orElse(null);
        if (device == null) throw new YnErrorException(YnError.YN_400010);
        String lastHeartbeatTime = stringRedisTemplate.opsForValue().get(device.getCode());
        if (lastHeartbeatTime == null) throw new YnErrorException(YnError.YN_400013);
        // 判断设备状态
        switch (device.getStatus()) {
            case RUN:
                throw new YnErrorException(YnError.YN_400008);
            case STOP:
                throw new YnErrorException(YnError.YN_400009);
            case OFFLINE:
                throw new YnErrorException(YnError.YN_400010);
            case ABNORMAL:
                throw new YnErrorException(YnError.YN_400011);
            case NOT_ACTIVE:
                throw new YnErrorException(YnError.YN_400012);
        }

        // 待执行任务列表
        List<PrintTask> printTaskList = new ArrayList<>();
        // 获取支付总价格
        BigDecimal price = BigDecimal.ZERO;
        // 打印任务所需纸张类型总页数
        Map<PaperType, Integer> paperConsume = new HashMap<>();
        for (PreprintTask preprintTask : dto.getTaskList()) {
            price = price.add(this.getPrintPrice(device.getId(), preprintTask));
            MetaFile metaFile = metaFileRepository.findById(preprintTask.getFileId()).orElse(null);
            if (metaFile == null) throw new YnErrorException(YnError.YN_700001);

            // 设置各纸张类型所需纸张数
            int pageSize = "pdf".equals(metaFile.getFileType()) ? PdfUtil.getNumberOfPages(fileService.metaFile2File(metaFile)) : 1;
            PaperType paperType = preprintTask.getPrintType().getPaperType();
            try {
                if (paperType == PaperType.Inches6) {
                    paperType = PaperType.getActualPaperType(device.getPaperNumber());
                }
            } catch (Exception e) {
                log.info("获取纸张类型失败，使用默认纸张类型");
            }
            if (paperConsume.containsKey(paperType))
                paperConsume.put(paperType, paperConsume.get(paperType) + pageSize);
            else
                paperConsume.put(paperType, pageSize);

            // 保存打印任务
            PrintTask printTask = new PrintTask();
            BeanUtils.copyProperties(preprintTask, printTask);
            printTask.setPrintingColor(preprintTask.getPrintingColor());
            printTask.setDevice(device);
            printTask.setPrintDirection(VERTICAL);
            printTask.setPrintTaskStatus(PrintTaskStatus.TO_BE_PRINTED);
            printTask.setTranscodingFile(metaFileRepository.findById(preprintTask.getFileId()).orElse(null));
            printTask.setPaperConsume(PrintFaces.SINGLE.equals(printTask.getPrintingFaces()) ? pageSize
                    : pageSize % 2 == 0 ? pageSize / 2 : pageSize / 2 + 1);
            printTask.setPageSize(pageSize);
            printTask.setSourceFileName(preprintTask.getSourceFileName());
            printTaskList.add(printTask);
        }

        // 校验设备剩余纸张和耗材是否足够，任意纸张缺少都会抛出异常
        Map<PaperType, Integer> paperResidue = paperTableRepository.findByDevice(device)
                .stream().collect(Collectors.toMap(PaperTable::getName, PaperTable::getResidue, (v1, v2) -> v2));
        paperConsume.forEach((paperType, consume) -> {
            if (paperResidue.get(paperType) == null || paperResidue.get(paperType) < consume)
                throw new YnErrorException(YnError.YN_500002);
        });

        // 创建打印订单
        OrderManagement order = orderService.createPrintOrder(device, member, price, dto.getPayMode());
        try {
            for (PreprintTask task : dto.getTaskList()) {
                if (task.getPrintType() == PrintType.DocumentPrinting) {
                    order.setOrderPrintType(OrderPrintType.DOCUMENT);
                } else if (task.getPrintType() == PrintType.PhotoPrinting || task.getPrintType() == PrintType.IdPrinting) {
                    if (dto.getTaskList().stream().anyMatch(t -> t.getPrintType() == PrintType.PhotoPrinting || t.getPrintType() == PrintType.IdPrinting)) {
                        order.setOrderPrintType(OrderPrintType.PHOTO);
                    } else {
                        order.setOrderPrintType(OrderPrintType.PHOTO);
                    }
                }
            }
        } catch (Exception e) {
            log.info("打印类型添加异常");
        }
        // 保存任务列表, 并关联订单
        for (int i = 0; i < printTaskList.size(); i++) {
            PrintTask printTask = printTaskList.get(i);
            printTask.setPrintTaskStatus(PrintTaskStatus.TO_BE_PRINTED);
            printTask.setCode(order.getCode() + "-" + i);
            printTask.setOrders(order);
        }
        printTaskRepository.saveAll(printTaskList);

        // 当前设备绑定用户非下单用户时, 自动绑定用户
        if (!member.equals(device.getBindUser()))
            this.bindUser(device.getCode());

        return order.getId();

    }

    @Override
    public void executePrintingTask(OrderManagement order) {
        log.info("开始处理订单下相关的待打印任务 -> 订单号: {}", order.getCode());
        List<PrintTask> printTaskList = printTaskRepository.findByOrders(order);
        for (PrintTask printTask : printTaskList) {
            this.printTaskPush(printTask);
        }
        log.info("开始推送订单信息至设备客户端 -> 订单号: {}", order.getCode());
        this.printOrderPush(order);
    }

    @Override
    public boolean printOrderPush(OrderManagement order) {

        PrintOrderPush printOrderPush = new PrintOrderPush();
        BeanUtils.copyProperties(order, printOrderPush);

        printOrderPush.setPrintTaskPushList(new ArrayList<>());
        List<PrintTask> printTaskList = printTaskRepository.findByOrders(order);
        for (PrintTask printTask : printTaskList) {
            PrintTaskPush printTaskPush = new PrintTaskPush();
            BeanUtils.copyProperties(printTask, printTaskPush);
            printTaskPush.setPrintFileType(printTask.getTranscodingFile().getFileName().endsWith(".pdf") ? PrintFileType.PDF : PrintFileType.IMAGE);
            printTaskPush.setPrintFileId(printTask.getTranscodingFile().getId());
            printTaskPush.setPrintFileName(printTask.getTranscodingFile().getFileName());
            // 设置当前设备型号对应的打印机驱动
            XingHao xingHao = order.getDevice().getXh();
            printTaskPush.setA4PrinterName(xingHao.getA4PrinterName());
            printTaskPush.setPhotoPrinterName(xingHao.getPhotoPrinterName());
            printOrderPush.getPrintTaskPushList().add(printTaskPush);

            printTask.setPushTime(LocalDateTime.now());
            printTask.setPrintTaskStatus(PrintTaskStatus.PUSHED);
            // 照片打印处理，使照片竖排
            if (PrintFileType.IMAGE.equals(printTaskPush.getPrintFileType())) {
                File src = fileService.metaFile2File(printTask.getTranscodingFile());
                Image image;
                try {
                    image = Image.getInstance(src.getPath());
                    // 如果长比宽小, 则图片 90度 旋转, 使其竖排
                    if (image.getHeight() < image.getWidth()) {
                        ImgUtil.rotate(src, 90, src);
                    }
                } catch (BadElementException | IOException e) {
                    e.printStackTrace();
                }
            }
            // 扣除打印任务所用纸张和耗材
            deductPaperAndConsumables(printTask);
        }

        this.mqttPush(order.getDevice(), JSON.toJSONString(printOrderPush));

        // 更新设备状态
        order.getDevice().setStatus(DeviceStatus.RUN);
        repository.save(order.getDevice());

        return true;
    }

    @Override
    public boolean printTaskPush(PrintTask printTask) {
        PrintTaskPush push = new PrintTaskPush();
        BeanUtils.copyProperties(printTask, push);
        push.setPrintFileType(printTask.getTranscodingFile().getFileName().endsWith(".pdf") ? PrintFileType.PDF : PrintFileType.IMAGE);
        push.setPrintFileId(printTask.getTranscodingFile().getId());
        push.setPrintFileName(printTask.getTranscodingFile().getFileName());
        this.mqttPush(printTask.getDevice(), JSON.toJSONString(push));
        printTask.setPushTime(LocalDateTime.now());
        printTask.setPrintTaskStatus(PrintTaskStatus.PUSHED);
        printTaskRepository.save(printTask);
        return true;
    }

    @Override
    public List<GoodsVo> getPrintPriceList(Long deviceId) {
        DevicesList device = repository.findById(deviceId).orElse(null);
        if (device == null)
            throw new YnErrorException(YnError.YN_400001);

        FixPrice fixPrice = device.getFixPrice();
        if (fixPrice == null) throw new YnErrorException(YnError.YN_400002);

        // 查询该定价分类下的所有商品定价
        List<Commodity> commodityList = commodityRepository.findByFixPrice(fixPrice);

        return commodityList.stream().map(item -> {
            GoodsVo vo = new GoodsVo();
            vo.setName(item.getGoods().getName());
            vo.setAmount(item.getPrice());
            if(item.getGoods().getPrintFaces() == DOUBLE)
                vo.setAmount(item.getPrice().multiply(new BigDecimal("2")));
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public PrintTaskPush getPrintTask(Long orderId) {

        OrderManagement order = orderManagementRepository.findById(orderId).orElse(null);
        if (order == null || !PayStatus.PAID.equals(order.getPayStatus())) return null;

        List<PrintTask> printTaskList = printTaskRepository.findByOrders(order);
        for (PrintTask printTask : printTaskList) {
            if (PrintTaskStatus.PUSHED.equals(printTask.getPrintTaskStatus())) {
                PrintTaskPush dto = new PrintTaskPush();
                BeanUtils.copyProperties(printTask, dto);
                dto.setPrintFileType(printTask.getTranscodingFile().getFileName().endsWith(".pdf") ? PrintFileType.PDF : PrintFileType.IMAGE);
                dto.setPrintFileId(printTask.getTranscodingFile().getId());
                dto.setPrintFileName(printTask.getTranscodingFile().getFileName());
                this.printTaskComplete(printTask, 1);
                return dto;
            }
        }

        return null;
    }

    @Override
    public Page<DevicesListVO> findDevicesByPhoneNumberAndLocation(double latitude, double longitude, Pageable pageable) {
        return repository.findDevicesByPhoneNumberWithDistance(AuditInterceptor.CURRENT_MEMBER.get(), latitude, longitude, pageable);
    }

    @Override
    public Page<DevicesListVO> findDevicesAndLocation(double latitude, double longitude, Pageable pageable) {
        return repository.findDevicesWithDistance(channelUtils.getChannelPartner(AuditInterceptor.CURRENT_MEMBER.get()), latitude, longitude, pageable);
    }

    @Override
    public Page<DevicesListVO> findDevicesUser(double latitude, double longitude, Pageable pageable) {
        return repository.findDevicesWithDistance(latitude, longitude, pageable);
    }

    @Override
    public void mqttPush(DevicesList device, String message) {
        String topic = mqttConfig.topicSendDevice + device.getCode();
        mqttSender.send(topic, message);
        log.info("推送MQTT消息 主题: {}, 内容: {}", topic, message);
    }

    @Transactional
    public void deductPaperAndConsumables(PrintTask printTask) {
        PaperType paperType = printTask.getPrintType().getPaperType();
        try {
            if (paperType == PaperType.Inches6) {
                paperType = PaperType.getActualPaperType(printTask.getDevice().getPaperNumber());
            }
        } catch (Exception e) {
            log.info("获取纸张类型失败，使用默认纸张类型");
        }
        // 扣除消耗纸张数量
        PaperTable paperTable = paperTableRepository.findByDeviceAndName(printTask.getDevice(), paperType);
        paperTable.setResidue(paperTable.getResidue() - printTask.getPaperConsume());
        paperTableRepository.save(paperTable);
        // 判断纸张是否达到预警值
        PaperWarning paperWarning = paperWarningRepository
                .findByXingHaoAndPaperType(printTask.getDevice().getXh(), paperType);
        if (paperWarning == null || paperWarning.getPaperWarningCount() >= paperTable.getResidue()) {
            TaskList taskList = new TaskList();
            taskList.setCode(printTask.getDevice());
            taskList.setTaskType(OperationsType.AddingPaper);
            taskList.setPersonnel(printTask.getDevice().getDev());
            taskList.setPhoneNumber(printTask.getDevice().getDev() != null ? printTask.getDevice().getDev().getPhoneNumber() : "");
            taskList.setGenerateTime(LocalDateTime.now());
            taskList.setMessage(taskList.getTaskType().getName());
            taskList.setPaperType(paperTable.getName());
            taskListRepository.save(taskList);
        }

        // 扣除耗材数量
        ConsumablesValue consumablesValue = consumablesValueRepository
                .findByDeviceAndName(printTask.getDevice(), printTask.getPrintType().getConsumableType());
        consumablesValue.setConsumablesValue(consumablesValue.getConsumablesValue() - printTask.getPageSize());
        consumablesValueRepository.save(consumablesValue);
        // 判断耗材是否达到预警值
        ConsumableWarning consumableWarning = consumableWarningRepository
                .findByXingHaoAndConsumableType(printTask.getDevice().getXh(), printTask.getPrintType().getConsumableType());
        if (consumableWarning == null || consumableWarning.getWarningValue() >= consumablesValue.getConsumablesValue()) {
            TaskList taskList = new TaskList();
            taskList.setCode(printTask.getDevice());
            taskList.setTaskType(OperationsType.ReplacingConsumables);
            taskList.setPersonnel(printTask.getDevice().getDev());
            taskList.setPhoneNumber(printTask.getDevice().getDev() != null ? printTask.getDevice().getDev().getPhoneNumber() : "");
            taskList.setGenerateTime(LocalDateTime.now());
            taskList.setMessage(taskList.getTaskType().getName());
            taskList.setConsumable(consumablesValue.getName());
            taskListRepository.save(taskList);
        }
    }

    @Override
    public void mqttCallback(String deviceCode, String message) {

        BaseCallback callback = JSON.parseObject(message, BaseCallback.class);

        switch (callback.getCmd()) {
            case HEARTBEAT:
                this.updateHeartbeatTime(deviceCode);
                this.handleHeartbeat(deviceCode, JSON.parseObject(message, DeviceHeartbeatCallback.class));
                break;
            case PRINT_STATUS:
                log.info(deviceCode + " : " + message);
                this.printTaskCallback(deviceCode, JSON.parseObject(message, PrintTaskCallback.class));
                break;
            case CHANGE:
                this.printVersionBack(deviceCode, JSON.parseObject(message, CodeBack.class));
                break;
            default:
                log.info("mqttCallback -> 忽略处理的回调 : {}", callback.getCmd());
        }

    }
    private void printVersionBack(String deviceCode, CodeBack codeBack){
        DevicesList device = repository.findByCode(deviceCode);
        if (device == null) return;
        if(!codeBack.getVersionNumber().equals( device.getVersionNumber())) {
            device.setVersionNumber(codeBack.getVersionNumber());
            repository.save(device);
        }
        return;
    }


    private void updateHeartbeatTime(String deviceCode) {
        stringRedisTemplate.opsForValue().set(deviceCode, String.valueOf(System.currentTimeMillis()), 240, TimeUnit.SECONDS);
    }

    @Scheduled(fixedRate = 120000)
    public void checkHeartbeatTimeout() {
        log.info("检查设备网络状态");
        List<String> deviceCodes = repository.findCodeByStatus(DeviceStatus.ONLINE); // 获取所有设备编码
        for (String deviceCode : deviceCodes) {
            String lastHeartbeatTime = stringRedisTemplate.opsForValue().get(deviceCode);
            if (lastHeartbeatTime != null && System.currentTimeMillis() - Long.parseLong(lastHeartbeatTime) >60 * 1000 * 5) {
                handleHeartbeatTimeout(deviceCode);
            }
        }
    }
    private void handleHeartbeatTimeout(String deviceCode) {
        log.info(deviceCode + "网络状态异常");
        DevicesList device = repository.findByCode(deviceCode);
        if (device == null || DeviceStatus.NOT_ACTIVE.equals(device.getStatus())) return;

        updateDeviceStatus(DeviceStatus.ABNORMAL, deviceCode, "网络断开");
    }

    private void handleHeartbeat(String deviceCode, DeviceHeartbeatCallback callback) {
        DevicesList device = repository.findByCode(deviceCode);
        if (device == null || DeviceStatus.NOT_ACTIVE.equals(device.getStatus())) return;
        updateDeviceStatus(callback.getDeviceStatus(), deviceCode, callback.getAbnormalReason());
    }

    private void printTaskCallback(String deviceCode, PrintTaskCallback callback) {

        DevicesList device = repository.findByCode(deviceCode);
        if (device == null) return;

        PrintTask printTask = printTaskRepository.findByCode(callback.getTaskCode());
        if (printTask == null) return;

        switch (callback.getTaskStatus()) {
            case PRINTING:
                this.printTaskPrinting(printTask);
                break;
            case INTERRUPT:
                this.printTaskInterrupt(printTask, callback.getComplete(),callback.getInterruptReason());
                break;
            case COMPLETE:
                this.printTaskComplete(printTask, callback.getComplete());
                break;
            default:
                printTask.setPrintTaskStatus(callback.getTaskStatus());
                printTaskRepository.save(printTask);
        }

    }

    private void printTaskPrinting(PrintTask printing) {
        log.info("打印任务进行中 -> 任务号: {}", printing.getCode());
        printTaskRepository.findById(printing.getId()).ifPresent(printTask -> printTask.setPrintTaskStatus(PrintTaskStatus.PRINTING));
        printTaskRepository.save(printing);
    }

    public void printTaskInterrupt(PrintTask interrupt, Integer complete, String interruptReason) {
        transactionTemplate.executeWithoutResult(status -> {log.warn("打印任务中断 -> 任务号: {} 原因: {}", interrupt.getCode(), interruptReason);
        PrintTask printTask = printTaskRepository.findById(interrupt.getId()).orElse(null);
        if (printTask != null) {
            printTask.setPrintTaskStatus(PrintTaskStatus.INTERRUPT);
            printTask.setInterruptReason(interruptReason);
            printTask.setEndTime(LocalDateTime.now());
            printTask.setPageComplete(complete);
            if(interrupt.getPaperConsume()!=null)
                printTask.setPaperConsume(interrupt.getPaperConsume());
            printTaskRepository.save(printTask);
        }
        log.warn("订单异常 -> 订单号: {} 原因: {}", interrupt.getOrders().getCode(), interruptReason);
        orderService.orderAbnormal(interrupt.getOrders().getId(), interruptReason);});
        log.warn("设备进入异常状态 -> 设备号: {} 原因: {}", interrupt.getDevice().getCode(), interruptReason);
        updateDeviceStatus(DeviceStatus.ABNORMAL, interrupt.getDevice().getCode(), interruptReason);
    }

    public void printTaskComplete(PrintTask complete, Integer pageComplete) {
        log.info("打印任务已完成 -> 任务号: {}", complete.getCode());
        PrintTask printTask = printTaskRepository.findById(complete.getId()).orElse(null);
        if (printTask != null && !PrintTaskStatus.COMPLETE.equals(complete.getPrintTaskStatus())) {
            printTask.setPrintTaskStatus(PrintTaskStatus.COMPLETE);
            printTask.setEndTime(LocalDateTime.now());
            printTask.setPageComplete(pageComplete);
            if(complete.getPaperConsume()!=null)
                printTask.setPaperConsume(complete.getPaperConsume());
            printTaskRepository.save(printTask);
        }
        if(!TransactionStatus.COMPLETE.equals(complete.getOrders().getTransactionStatus())) {
            List<PrintTask> printTaskList = printTaskRepository.findByOrders(complete.getOrders())
                    .stream().filter(item -> !PrintTaskStatus.COMPLETE.equals(item.getPrintTaskStatus())).collect(Collectors.toList());
            if (printTaskList.isEmpty()) {
                log.info("订单所有任务已完成 -> 订单号: {}", complete.getOrders().getCode());
                orderService.orderComplete(complete.getOrders().getId());
                updateDeviceStatus(DeviceStatus.ONLINE, complete.getDevice().getCode(), null);
            }
        }
    }
    private void updateDeviceStatus(DeviceStatus status, String deviceCode, String reason) {
        DevicesList device = repository.findByCode(deviceCode);
        if (device == null) return;
        try {
            device.setStatus(status);
            device.setAbnormalReason(reason);
            repository.save(device);
        } catch (OptimisticLockingFailureException ex) {
            log.warn("设备 {} 更新失败，原因：{}。跳过此次更新。", deviceCode, ex.getMessage());
        }
    }

    private DeviceVo deviceVo(DevicesList device) {
        DeviceVo vo = new DeviceVo();
        BeanUtils.copyProperties(device, vo);
        vo.setPaperNumber(paperTableRepository.findByDevice(device).stream().map(item -> {
            PaperTableVo paperTableVo = new PaperTableVo();
            paperTableVo.setName(item.getName().getName());
            paperTableVo.setResidue(item.getResidue());
            return paperTableVo;
        }).collect(Collectors.toList()));
        vo.setConsumablesValue(consumablesValueRepository.findByDevice(device).stream().map(item -> {
            ConsumablesValueVo consumablesValueVo = new ConsumablesValueVo();
            consumablesValueVo.setName(item.getName().getName());
            consumablesValueVo.setConsumablesValue(item.getConsumablesValue());
            return consumablesValueVo;
        }).collect(Collectors.toList()));

        // 客服电话
        if (device.getDev() != null)
            vo.setServicePhone(device.getDev().getPhoneNumber());
        if (device.getTerminalMerchants() != null)
            vo.setTerminalMerchants(device.getTerminalMerchants().getName());
        vo.generateDetailAddress();
        return vo;
    }

    @Override
    public void saveArea(Long deviceId, double latitude, double longitude) {
        DevicesList device = repository.findById(deviceId).orElse(null);
        if (device == null) throw new YnErrorException(YnError.YN_400001);
        String latitudeStr = Double.toString(latitude);
        String longitudeStr = Double.toString(longitude);
        BigDecimal longitudeBD = BigDecimal.valueOf(longitude)
                .setScale(6, RoundingMode.HALF_UP);
        device.setJd(longitudeBD);
        BigDecimal latitudeBD = BigDecimal.valueOf(latitude)
                .setScale(6, RoundingMode.HALF_UP);
        device.setWd(latitudeBD);
        AreaVO areaVO = inverseAddressResolution.getAreaVO(latitudeStr,longitudeStr,thirdPartyVouchersRepository.findAll().get(0).getTentcentMapKey());
        device.setProvince(areaVO.getProvince());
        device.setCity(areaVO.getCity());
        device.setCounty(areaVO.getCounty());
        device.setStreet(areaVO.getStreet());
        device.setAddress(areaVO.getAddress());
        repository.save(device);
    }

    @Override
    public List<String> getPrint(String deviceCode,String name) {
        DevicesList devicesList = repository.findByCode(deviceCode);

        List<Long> ids = operationDevicesListSupportingComponentsRepository.finddeviceMeatIdsBydeviceId(devicesList.getId());

        List<DeviceMeta> deviceMetas = deviceMetaRepository.findAllById(ids);

        return deviceMetas.stream()
                .filter(meta -> name.equals(meta.getComponentType()))
                .map(DeviceMeta::getName)
                .collect(Collectors.toList());
    }

    @Override
    public DeviceStatusVO getDeviceStatusVO(String code) {
      return   repository.findDeviceStatusVOByCode(code);
    }

    @Override
    public List<DeviceInterfaceVO> getPrintInterface(String deviceCode) {
        DevicesList devicesList = repository.findByCode(deviceCode);
        List<Long> ids = operationDevicesListDeviceInterfaceRepository.finddeviceInterfaceIdsBydeviceId(devicesList.getId());
        return deviceInterfaceRepository.findAllById(ids).stream()
                .map(deviceInterface -> {
                    DeviceInterfaceVO deviceInterfaceVO = new DeviceInterfaceVO();
                    deviceInterfaceVO.setName(deviceInterface.getName());
                    deviceInterfaceVO.setInterfaceAddress(deviceInterface.getInterfaceAddress());
                    return deviceInterfaceVO;
                })
                .collect(Collectors.toList());
    }
}
