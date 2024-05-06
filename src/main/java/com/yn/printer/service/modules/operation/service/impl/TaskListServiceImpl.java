package com.yn.printer.service.modules.operation.service.impl;

import com.yn.printer.service.common.exception.YnError;
import com.yn.printer.service.common.exception.YnErrorException;
import com.yn.printer.service.interceptor.AuditInterceptor;
import com.yn.printer.service.modules.channel.entity.ChannelPartner;
import com.yn.printer.service.modules.channel.entity.ChannelUser;
import com.yn.printer.service.modules.channel.repository.ChannelUserRepository;
import com.yn.printer.service.modules.member.entity.Member;
import com.yn.printer.service.modules.meta.enums.PaperType;
import com.yn.printer.service.modules.meta.repository.PaperWarningRepository;
import com.yn.printer.service.modules.operation.entity.ConsumablesValue;
import com.yn.printer.service.modules.operation.entity.DevicesList;
import com.yn.printer.service.modules.operation.entity.PaperTable;
import com.yn.printer.service.modules.operation.entity.TaskList;
import com.yn.printer.service.modules.operation.enums.ConsumableType;
import com.yn.printer.service.modules.operation.enums.OperationsType;
import com.yn.printer.service.modules.operation.enums.ProcessingStatus;
import com.yn.printer.service.modules.operation.repository.*;
import com.yn.printer.service.modules.operation.service.ITaskListService;
import com.yn.printer.service.modules.operation.vo.ReadVo;
import com.yn.printer.service.modules.operation.vo.TaskDetailsVO;
import com.yn.printer.service.modules.operation.vo.TaskStatisticsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TaskListServiceImpl implements ITaskListService {

    @Autowired
    private TaskListRepository taskListRepository;

    @Autowired
    private DevicesListRepository devicesListRepository;

    @Autowired
    private ConsumablesValueRepository consumablesValueRepository;

    @Autowired
    private PaperTableRepository paperTableRepository;


    @Autowired
    private ChannelUserRepository channelUserRepository;

    @Autowired
    private PaperWarningRepository paperWarningRepository;

    @Override
    public TaskStatisticsVO getStatistics() {
        // 查询对应电话号码列 readed 为真的数量
        long readedTrueCount = taskListRepository.countByPersonnelAndReaded(AuditInterceptor.CURRENT_MEMBER.get(), true);
        // 查询对应电话号码列 readed 为假的数量
        long readedFalseCount = taskListRepository.countByPersonnelAndReaded(AuditInterceptor.CURRENT_MEMBER.get(), false);
        // 查询对应电话号码列 taskStatus 为已处理的数量
        long taskStatusTrueCount = taskListRepository.countByPersonnelAndTaskStatus(AuditInterceptor.CURRENT_MEMBER.get(), ProcessingStatus.PS1);
        // 查询对应电话号码列 taskStatus 为未处理处理的数量
        long taskStatusFalseCount = taskListRepository.countByPersonnelAndTaskStatus(AuditInterceptor.CURRENT_MEMBER.get(), ProcessingStatus.PS0);
        TaskStatisticsVO statistics = new TaskStatisticsVO();
        statistics.setReadedTrueCount((int) readedTrueCount);
        statistics.setReadedFalseCount((int) readedFalseCount);
        statistics.setTaskStatusTrueCount((int) taskStatusTrueCount);
        statistics.setTaskStatusFalseCount((int) taskStatusFalseCount);
        return statistics;
    }

    @Override
    public Page<TaskDetailsVO> getTaskDetailsByPhoneNumber(OperationsType taskType, Boolean readed, ProcessingStatus taskStatus, String code, Pageable pageable) {
        Page<TaskList> taskListPage = taskListRepository.findTaskByMember(AuditInterceptor.CURRENT_MEMBER.get(), taskType, readed, taskStatus, code, pageable);

        List<TaskDetailsVO> taskDetailsVOList = taskListPage.getContent().stream()
                .map(this::taskToTaskDetailsVO)
                .collect(Collectors.toList());

        return new PageImpl<>(taskDetailsVOList, pageable, taskListPage.getTotalElements());
    }

    //将taskList转为运维信息VO
    private TaskDetailsVO taskToTaskDetailsVO(TaskList task) {
        TaskDetailsVO taskDetailsVO = new TaskDetailsVO();

        BeanUtils.copyProperties(task, taskDetailsVO);

        setDeviceAndPaperDetails(taskDetailsVO,task.getCode());

        return taskDetailsVO;
    }
    //向运维信息添加设备的参数
    private void setDeviceAndPaperDetails(TaskDetailsVO taskDetailsVO, DevicesList devicesList) {

        taskDetailsVO.setDeviceName(devicesList.getName());
        taskDetailsVO.setProvince(devicesList.getProvince());
        taskDetailsVO.setCity(devicesList.getCity());
        taskDetailsVO.setCounty(devicesList.getCounty());
        taskDetailsVO.setStreet(devicesList.getStreet());
        taskDetailsVO.setDetailAddress(devicesList.getAddress());
        taskDetailsVO.setChannelName(devicesList.getTerminalMerchants().getName());
        //为加纸任务提供纸张余量和纸盒容量
        if (taskDetailsVO.getTaskType().equals(OperationsType.AddingPaper)) {
            devicesList.getPaperNumber().stream()
                    .filter(paperTable -> paperTable.getName().equals(taskDetailsVO.getPaperType()))
                    .findFirst()
                    .ifPresent(paperTable -> {
                        taskDetailsVO.setResidue(paperTable.getResidue());
                        taskDetailsVO.setMaxResidue(paperWarningRepository.findByXingHaoAndPaperType(devicesList.getXh(), taskDetailsVO.getPaperType()).getMaxPaperResidue());
                    });
        }
    }


    @Override
    @Transactional
    public Boolean readcharge() {
        taskListRepository.updateReadedForPhoneNumber(AuditInterceptor.CURRENT_MEMBER.get());
        return true;
    }
    @Override
    @Transactional
    public ReadVo readTask(Long id) {
        taskListRepository.updateReadedForId(id);
        return taskListRepository.findTaskDetailsById(id);
    }
    @Override
    @Transactional
    public Boolean add(int residue, Long id, Integer additionsNumber, String information) {
        TaskList taskList = taskListRepository.findById(id).orElse(null);
        if (taskList == null) throw new YnErrorException(YnError.YN_600001);
        DevicesList device = taskList.getCode();
        Map<OperationsType, Runnable> operationMap = new HashMap<>();
        operationMap.put(OperationsType.AddingPaper, () -> {
            PaperType paperType = taskList.getPaperType();
            setPaperTable(residue, additionsNumber, paperType, taskList, device);
        });
        operationMap.put(OperationsType.ReplacingConsumables, () -> {
            ConsumableType consumable = taskList.getConsumable();
            setConsumablesValue(additionsNumber, consumable, taskList, device);
        });
        operationMap.put(OperationsType.Other, () -> {
            taskList.setInformation(information);
        });
        operationMap.getOrDefault(taskList.getTaskType(), () -> {}).run();
        taskList.setTaskStatus(ProcessingStatus.PS1);
        taskList.setCompletionTime(LocalDateTime.now());
        taskListRepository.save(taskList);
        return true;
    }
    @Override
    public TaskStatisticsVO getOperationStatistics(LocalDate startDate, LocalDate endDate) {
        ChannelUser channelUser = channelUserRepository.findFirstByContactPhone(AuditInterceptor.CURRENT_MEMBER.get().getPhoneNumber());
        ChannelPartner channelPartner = channelUser.getChannelPartner();
        List<DevicesList> devicesLists = devicesListRepository.findDevices(channelPartner);
        LocalDateTime startOfDate = startDate.atStartOfDay();
        LocalDateTime endOfDay = endDate.atTime(LocalTime.MAX);
        int readedTrueCount = 0;
        int readedFalseCount = 0;
        int taskStatusTrueCount = 0;
        int taskStatusFalseCount = 0;
        for (DevicesList devicesList : devicesLists) {
            // 查询对应电话号码列 readed 为真的数量
            readedTrueCount += taskListRepository.countByCodeAndReadedAndGenerateTimeBetween(devicesList, true, startOfDate, endOfDay);
            // 查询对应电话号码列 readed 为假的数量
            readedFalseCount += taskListRepository.countByCodeAndReadedAndGenerateTimeBetween(devicesList, false, startOfDate, endOfDay);
            // 查询对应电话号码列 taskStatus 为已处理的数量
            taskStatusTrueCount += taskListRepository.countByCodeAndTaskStatusAndGenerateTimeBetween(devicesList, ProcessingStatus.PS1, startOfDate, endOfDay);
            // 查询对应电话号码列 taskStatus 为未处理的数量
            taskStatusFalseCount += taskListRepository.countByCodeAndTaskStatusAndGenerateTimeBetween(devicesList, ProcessingStatus.PS0, startOfDate, endOfDay);
        }
        TaskStatisticsVO statistics = new TaskStatisticsVO();
        statistics.setReadedTrueCount(readedTrueCount);
        statistics.setReadedFalseCount(readedFalseCount);
        statistics.setTaskStatusTrueCount(taskStatusTrueCount);
        statistics.setTaskStatusFalseCount(taskStatusFalseCount);
        return statistics;
    }
    @Override
    public Page<TaskDetailsVO> getOperationTaskList(LocalDate startDate, LocalDate endDate, String code, Pageable pageable) {
         ChannelUser channelUser = channelUserRepository.findFirstByContactPhone(AuditInterceptor.CURRENT_MEMBER.get().getPhoneNumber());
        ChannelPartner channelPartner = channelUser.getChannelPartner();
        List<DevicesList> devicesLists = devicesListRepository.findDevices(channelPartner);
        LocalDateTime startOfDate = startDate.atStartOfDay();
        LocalDateTime endOfDay = endDate.atTime(LocalTime.MAX);
        List<TaskDetailsVO> taskDetailsList = new ArrayList<>();
        for (DevicesList devicesList : devicesLists) {
            List<TaskDetailsVO> taskDetails = taskListRepository.findTask(devicesList, code, startOfDate, endOfDay);
            taskDetailsList.addAll(taskDetails);
        }
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), taskDetailsList.size());
        List<TaskDetailsVO> paginatedList = taskDetailsList.subList(start, end);
        return new PageImpl<>(paginatedList, pageable, taskDetailsList.size());
    }
    @Override
    public Boolean creatAdd(int residue, Long id, Integer additionsNumber, String information, OperationsType taskType, PaperType paperType, ConsumableType consumable,String message) {
        TaskList taskList = new TaskList();
        Member member = AuditInterceptor.CURRENT_MEMBER.get();
        DevicesList device = devicesListRepository.findById(id).orElse(null);

        Map<OperationsType, Runnable> operationMap = new HashMap<>();
        operationMap.put(OperationsType.AddingPaper, () -> {
            taskList.setPaperType(paperType);
            taskList.setAdditionsNumber(additionsNumber);
            setPaperTable(residue, additionsNumber, paperType, taskList, device);
            setDefaultTask(taskList, taskType, consumable, additionsNumber, device, member);
        });
        operationMap.put(OperationsType.ReplacingConsumables, () -> {
            taskList.setConsumable(consumable);
            taskList.setAdditionsNumber(additionsNumber);
            setConsumablesValue(additionsNumber, consumable, taskList, device);
            setDefaultTask(taskList, taskType, consumable, additionsNumber, device, member);
        });
        operationMap.put(OperationsType.Other, () -> {
            taskList.setMessage(message);
            taskList.setReaded(Boolean.FALSE);
            taskList.setTaskStatus(ProcessingStatus.PS0);
            taskList.setInformation(information);
            taskList.setPersonnel(member);
            taskList.setTaskType(taskType);
            taskList.setGenerateTime(LocalDateTime.now());
            taskList.setCode(device);
        });
        operationMap.getOrDefault(taskType,  () -> {}).run();
        taskListRepository.save(taskList);
        return true;
    }

    private void setConsumablesValue(Integer additionsNumber, ConsumableType consumable, TaskList taskList, DevicesList device) {
        ConsumablesValue consumablesValue = consumablesValueRepository.findByDeviceAndName(device, consumable);
        if (consumablesValue == null) {
            consumablesValue = new ConsumablesValue();
            consumablesValue.setDevice(device);
            consumablesValue.setName(consumable);
            consumablesValue.setConsumablesValue(0);
            consumablesValueRepository.save(consumablesValue);
        }
        consumablesValue.setConsumablesValue(additionsNumber);
        consumablesValueRepository.save(consumablesValue);
    }

    private void setPaperTable(int residue, Integer additionsNumber, PaperType paperType, TaskList taskList, DevicesList device) {
        PaperTable paperTable = paperTableRepository.findByDeviceAndName(device, paperType);
        if (paperTable == null) {
            paperTable = new PaperTable();
            paperTable.setDevice(device);
            paperTable.setName(paperType);
            paperTable.setResidue(0);
            paperTableRepository.save(paperTable);
        }
        paperTable.setResidue(residue + additionsNumber);
        paperTableRepository.save(paperTable);
    }

    private void setDefaultTask(TaskList taskList, OperationsType taskType, ConsumableType consumable, Integer additionsNumber, DevicesList device, Member member) {
        taskList.setTaskStatus(ProcessingStatus.PS1);
        taskList.setCompletionTime(LocalDateTime.now());
        taskList.setPersonnel(member);
        taskList.setTaskType(taskType);
        taskList.setAdditionsNumber(additionsNumber);
        taskList.setReaded(true);
        taskList.setGenerateTime(LocalDateTime.now());
        taskList.setCode(device);
    }
}


