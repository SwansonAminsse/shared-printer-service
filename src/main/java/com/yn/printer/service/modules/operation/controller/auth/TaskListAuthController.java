package com.yn.printer.service.modules.operation.controller.auth;

import com.yn.printer.service.interceptor.AuditInterceptor;
import com.yn.printer.service.modules.member.entity.Member;
import com.yn.printer.service.modules.meta.enums.PaperType;
import com.yn.printer.service.modules.operation.enums.ConsumableType;
import com.yn.printer.service.modules.operation.enums.OperationsType;
import com.yn.printer.service.modules.operation.enums.ProcessingStatus;
import com.yn.printer.service.modules.operation.service.IDeviceService;
import com.yn.printer.service.modules.operation.service.ITaskListService;
import com.yn.printer.service.modules.operation.vo.AddVO;
import com.yn.printer.service.modules.operation.vo.ReadVo;
import com.yn.printer.service.modules.operation.vo.TaskDetailsVO;
import com.yn.printer.service.modules.operation.vo.TaskStatisticsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/task-list")
@Api(tags = "会员端-运维", value = "运维操作")
public class TaskListAuthController {

    @Autowired
    ITaskListService taskListService;
    @Autowired
    IDeviceService deviceService;

    @GetMapping("/statistics")
    @ApiOperation(value = "统计运维任务", notes = "页面上的已读未读已处理未处理数量")
    public TaskStatisticsVO getStatistics() {
        return taskListService.getStatistics();
    }

    @GetMapping("/details")
    @ApiOperation(value = "获取运维列表", notes = "根据已读未读已处理未处理或设备编码活动运维任务列表可选择根据开始时间进行排序", response = TaskDetailsVO.class, responseContainer = "List")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskType", value = "运维类型（AddingPaper或者ReplacingConsumables或者Other）", required = false, dataType = "com.yn.printer.service.modules.operation.enums.OperationsType"),
            @ApiImplicitParam(name = "readed", value = "真是已读假是未读", required = false, dataType = "java.lang.Boolean"),
            @ApiImplicitParam(name = "taskStatus", value = "枚举PS1是已处理，PS0是未处理", required = false, dataType = "com.yn.printer.service.modules.operation.enums.ProcessingStatus"),
            @ApiImplicitParam(name = "page", value = "页码", defaultValue = "0", dataType = "java.lang.Integer"),
            @ApiImplicitParam(name = "size", value = "记录数", defaultValue = "10", dataType = "java.lang.Integer"),
            @ApiImplicitParam(name = "sortField", value = "默认按创建时间排序", defaultValue = "createdOn", dataType = "java.lang.String"),
            @ApiImplicitParam(name = "sortOrder", value = "ASC是早的在前，DESC是晚的在前", defaultValue = "ASC", dataType = "java.lang.String"),
            @ApiImplicitParam(name = "code", value = "设备编码（在网店筛选界面得到）", required = false, dataType = "java.lang.String")
    })
    public Page<TaskDetailsVO> getTaskDetails(
            @RequestParam(required = false) OperationsType taskType,
            @RequestParam(required = false) Boolean readed,
            @RequestParam(required = false) ProcessingStatus taskStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdOn") String sortField,
            @RequestParam(defaultValue = "ASC") String sortOrder,
            @RequestParam(required = false) String code) {

        Member member = AuditInterceptor.CURRENT_MEMBER.get();

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(sortOrder), sortField);
        return taskListService.getTaskDetailsByPhoneNumber(
                 taskType, readed, taskStatus, code, pageable);
    }

    @PostMapping("/allRead")
    @ApiOperation(value = "全都已读", notes = "在未读任务页面将所有未读任务更改为已读")
    public Boolean chargeread() {

        return taskListService.readcharge();
    }

    @PostMapping("/read")
    @ApiOperation(value = "已读", notes = "将未读任务更改为已读，並返回運維類型和所需的紙張耗材種類")
    public ReadVo read(@RequestParam Long id) {
        return taskListService.readTask(id);
    }

    @GetMapping("/add")
    @ApiOperation(value = "完成运维任务", notes = "修改设备和运维任务的值")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "residue", value = "剩余值", required = true, dataType = "java.lang.Integer"),
            @ApiImplicitParam(name = "id", value = "任务id", required = true, dataType = "java.lang.Long", paramType = "query"),
            @ApiImplicitParam(name = "additionsNumber", value = "添加值", dataType = "java.lang.Integer"),
            @ApiImplicitParam(name = "information", value = "其他信息",required = false, dataType = "java.lang.String")
    })
    public Boolean add(
            @RequestParam(defaultValue = "0",required = false) int residue,@RequestParam Long id, @RequestParam(required = false) Integer additionsNumber,@RequestParam(required = false) String information) {
        return taskListService.add(residue,id, additionsNumber,information);
    }
    @GetMapping("/creatAdd")
    @ApiOperation(value = "主动完成运维任务", notes = "创建并完成运维任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "residue", value = "剩余值", required = true, dataType = "java.lang.Integer"),
            @ApiImplicitParam(name = "id", value = "设备id", required = true, dataType = "java.lang.Long", paramType = "query"),
            @ApiImplicitParam(name = "additionsNumber", value = "添加值", required = false,dataType = "java.lang.Integer"),
            @ApiImplicitParam(name = "information", value = "完成后的备注",required = false, dataType = "java.lang.String"),
            @ApiImplicitParam(name = "taskType", value = "运维类型（AddingPaper或者ReplacingConsumables或者Other）",  dataType = "com.yn.printer.service.modules.operation.enums.OperationsType"),
            @ApiImplicitParam(name = "paperType", value = "纸张类型（A4或者Photo或者Inches6）", required = false, dataType = "com.yn.printer.service.modules.meta.enums.PaperType"),
            @ApiImplicitParam(name = "consumableType", value = "耗材类型（C1）", required = false, dataType = "com.yn.printer.service.modules.operation.enums.ConsumableType"),
            @ApiImplicitParam(name = "message", value = "出现的问题",required = false, dataType = "java.lang.String")
    })
    public Boolean creatAdd(
            @RequestParam(defaultValue = "0",required = false) int residue,@RequestParam Long id, @RequestParam(required = false) Integer additionsNumber,@RequestParam(required = false) String information,@RequestParam  OperationsType taskType,@RequestParam (required = false) PaperType paperType,@RequestParam (required = false) ConsumableType  consumableType,@RequestParam(required = false) String message) {
        return taskListService.creatAdd(residue,id, additionsNumber,information,taskType,paperType,consumableType,message);
    }


    @GetMapping("/taskdetails")
    @ApiOperation(value = "获取运维列表(运营)", notes = "根据时间获取运营的运维任务列表，可选code进行筛选", response = TaskDetailsVO.class, responseContainer = "List")
    @ApiImplicitParams({
              @ApiImplicitParam(name = "page", value = "页码", defaultValue = "0", dataType = "java.lang.Integer"),
            @ApiImplicitParam(name = "size", value = "记录数", defaultValue = "10", dataType = "java.lang.Integer"),
              @ApiImplicitParam(name = "code", value = "设备编码（在网店筛选界面得到）", required = false, dataType = "java.lang.String")
    })
    public Page<TaskDetailsVO> getTaskDetails(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String code) {
        Pageable pageable = PageRequest.of(page, size);
        return taskListService.getOperationTaskList(
                startDate, endDate, code, pageable);
    }

    @PostMapping("/area")
    @ApiOperation(value = "根据经纬度设定位置", notes = "根据经纬度获取位置信息存入设备中")
    public Boolean area(@RequestParam Long deviceId,@RequestParam double latitude,
    @RequestParam double longitude) {
        deviceService.saveArea(deviceId,latitude,longitude);
        return true;
    }



}
