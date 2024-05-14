package com.yn.printer.service.modules.orders.controller.auth;

import com.yn.printer.service.interceptor.AuditInterceptor;
import com.yn.printer.service.modules.operation.dto.SubmitPrintingTaskDto;
import com.yn.printer.service.modules.orders.dto.SubmitRechargeTaskDto;
import com.yn.printer.service.modules.orders.service.IOrderService;
import com.yn.printer.service.modules.orders.vo.PayInfoVo;
import com.yn.printer.service.modules.orders.vo.PrintOrderVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@Api(value = "OrderController", tags = "用户端-订单")
@RestController
@RequestMapping("/order/auth")
public class OrderAuthController {

    @Autowired
    IOrderService orderService;

    @ApiOperation(value = "订单-获取支付二维码")
    @GetMapping("/getPayQrCode")
    public PayInfoVo getPayQrCode(@RequestParam Long orderId) {
        return orderService.getPayQrCode(orderId);
    }

    @ApiOperation(value = "订单-获取打印订单记录")
    @GetMapping("/getPrintOrder")
    public Page<PrintOrderVo> getPrintOrder(Pageable pageable) {
        return orderService.getPrintOrder(pageable);
    }

    @ApiOperation(value = "订单-获取订单详情")
    @GetMapping("/getPrintOrderVo")
    public PrintOrderVo getPrintOrderVo(@RequestParam Long id) {
        return orderService.getPrintOrderVo(id);
    }

    @ApiOperation(value = "订单-提交充值任务")
    @PostMapping("/submitRechargeTask")
    public Long submitRechargeTask(@RequestBody SubmitRechargeTaskDto dto) {
        return orderService.submitRechargeTask(dto, AuditInterceptor.CURRENT_MEMBER.get());
    }

    @ApiOperation(value = "订单-确认支付余额")
    @PostMapping("/balancePayment")
    public Boolean balancePayment(@RequestBody Long id) {
        return orderService.balancePayment(id);
    }

}
