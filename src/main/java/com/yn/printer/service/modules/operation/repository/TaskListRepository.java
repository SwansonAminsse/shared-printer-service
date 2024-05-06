package com.yn.printer.service.modules.operation.repository;


import com.yn.printer.service.modules.member.entity.Member;
import com.yn.printer.service.modules.operation.entity.DevicesList;
import com.yn.printer.service.modules.operation.entity.EquipmentDivision;
import com.yn.printer.service.modules.operation.entity.TaskList;
import com.yn.printer.service.modules.operation.enums.OperationsType;
import com.yn.printer.service.modules.operation.enums.ProcessingStatus;
import com.yn.printer.service.modules.operation.vo.AddVO;
import com.yn.printer.service.modules.operation.vo.ReadVo;
import com.yn.printer.service.modules.operation.vo.TaskDetailsVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskListRepository extends JpaRepository<TaskList, Long>, JpaSpecificationExecutor<TaskList> {

    long countByPersonnelAndReaded(Member personnel, boolean readed);

    long countByPersonnelAndTaskStatus(Member personnel, ProcessingStatus taskStatus);

    int countByCodeAndReadedAndGenerateTimeBetween(DevicesList code, boolean readed, LocalDateTime startDate, LocalDateTime endDate);
    int countByCodeAndTaskStatusAndGenerateTimeBetween(DevicesList code, ProcessingStatus taskStatus, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT t  " +
            "FROM TaskList t " +
            "JOIN t.code  " +
            "WHERE t.personnel = :member " +
            "AND (:taskType IS NULL OR t.taskType = :taskType) " +
            "AND (:readed IS NULL OR t.readed = :readed) " +
            "AND (:taskStatus IS NULL OR t.taskStatus = :taskStatus)"+
            "AND (:code IS NULL OR t.code.code = :code) "
    )
    Page<TaskList> findTaskByMember(
            @Param("member") Member member,
            @Param("taskType") OperationsType taskType,
            @Param("readed") Boolean readed,
            @Param("taskStatus") ProcessingStatus taskStatus,
            @Param("code") String code,Pageable pageable
             );


    @Transactional
    @Modifying
    @Query("UPDATE TaskList t SET t.readed = true WHERE t.personnel = :member")
    void updateReadedForPhoneNumber(@Param("member") Member member);





    @Modifying
    @Query("UPDATE TaskList t SET t.readed = true WHERE t.id = :id")
    void updateReadedForId(@Param("id") Long id);


    @Query("SELECT new com.yn.printer.service.modules.operation.vo.ReadVo(t.taskType, t.id, t.code.code, t.readed, t.taskStatus,t.completionTime,t.createdOn,t.paperType,t.consumable) " +
            "FROM TaskList t " +
            "JOIN t.code  " +
            "WHERE t.id = :id "
    )
    ReadVo findTaskDetailsById(
            @Param("id") Long id
     );


    @Query("SELECT DISTINCT new com.yn.printer.service.modules.operation.vo.TaskDetailsVO(t.taskType,t.message,t.code.address, t.id, t.code.code, t.readed, t.taskStatus,t.completionTime,t.createdOn,t.paperType,t.consumable,t.code.name,t.code.terminalMerchants.name,t.code.province,t.code.city,t.code.county,t.code.street,t.information) " +
            "FROM TaskList t " +
            "WHERE t.code = :dev  " +
            "AND (:code IS NULL OR t.code.code = :code) " +
            "AND t.generateTime BETWEEN :startOfDate AND :endOfDay"
    )
    List<TaskDetailsVO> findTask(
            @Param("dev") DevicesList devicesList,
            @Param("code") String code,
            @Param("startOfDate") LocalDateTime startOfDate,
            @Param("endOfDay") LocalDateTime endOfDay
            );

    long countByCompletionTimeAfter(LocalDateTime startOfDate);
}




