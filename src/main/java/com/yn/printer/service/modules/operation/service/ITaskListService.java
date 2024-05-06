package com.yn.printer.service.modules.operation.service;

import com.yn.printer.service.modules.meta.enums.PaperType;
import com.yn.printer.service.modules.operation.enums.ConsumableType;
import com.yn.printer.service.modules.operation.enums.OperationsType;
import com.yn.printer.service.modules.operation.enums.ProcessingStatus;
import com.yn.printer.service.modules.operation.vo.ReadVo;
import com.yn.printer.service.modules.operation.vo.TaskDetailsVO;
import com.yn.printer.service.modules.operation.vo.TaskStatisticsVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ITaskListService {

    TaskStatisticsVO getStatistics();

    Page<TaskDetailsVO> getTaskDetailsByPhoneNumber(
             OperationsType taskType, Boolean readed, ProcessingStatus taskStatus, String code, Pageable pageable);

    @Transactional
    Boolean readcharge();

    @Transactional
    ReadVo readTask(Long id);

    @Transactional
    Boolean add(int residue, Long id, Integer additionsNumber, String information);


    @Transactional
    TaskStatisticsVO getOperationStatistics(LocalDate startDate, LocalDate endDate);

    Page<TaskDetailsVO> getOperationTaskList(LocalDate startDate, LocalDate endDate,String code, Pageable pageable);

    Boolean creatAdd(int residue, Long id, Integer additionsNumber, String information, OperationsType taskType, PaperType paperType, ConsumableType consumable,String message);
}
