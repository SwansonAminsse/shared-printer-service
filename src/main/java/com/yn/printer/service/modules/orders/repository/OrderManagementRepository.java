package com.yn.printer.service.modules.orders.repository;


import com.yn.printer.service.modules.member.entity.Member;
import com.yn.printer.service.modules.operation.entity.DevicesList;
import com.yn.printer.service.modules.orders.entity.OrderManagement;
import com.yn.printer.service.modules.orders.enums.OrderType;
import com.yn.printer.service.modules.orders.enums.PayStatus;
import com.yn.printer.service.modules.orders.enums.TransactionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderManagementRepository extends JpaRepository<OrderManagement, Long> {

    Page<OrderManagement> findAllByPayStatusAndOrdererAndOrderTypeOrderByCreatedOnDesc(Pageable pageable,
                                                                                       PayStatus payStatus,
                                                                                       Member member,
                                                                                       OrderType orderType);

    List<OrderManagement> findAllByTransactionStatus(TransactionStatus init);


    @Query("SELECT COUNT(o) FROM OrderManagement o WHERE (o.transactionStatus = 'INIT' OR o.transactionStatus = 'IN_PROGRESS') AND o.device = :device ")
    long countPrintOrderInProgress(@Param("device") DevicesList device);

    OrderManagement findByCode(String code);

    @Query("SELECT COUNT(DISTINCT o.orderer) FROM OrderManagement o " +
            "WHERE o.orderDate BETWEEN :startDate AND :endDate " +
            "AND o.device = :device " +
            "GROUP BY o.device, o.orderer")
    Long countDistinctOrderByOrderDateBetweenAndDevice(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("device") DevicesList device);

    @Query("SELECT COUNT(DISTINCT o.orderer) FROM OrderManagement o " +
            "WHERE o.orderDate BETWEEN :startDate AND :endDate ")
    Long countDistinctOrderByOrderDateBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(DISTINCT o.orderer) FROM OrderManagement o " +
            "WHERE o.orderDate < :startDate " +
            "AND o.device = :device " +
            "GROUP BY o.device, o.orderer")
    Long countDistinctOrderByOrderDateBeforeAndDevice(
            @Param("startDate") LocalDateTime startDate,
            @Param("device") DevicesList device);




    @Query("SELECT COUNT(o) FROM OrderManagement o " +
            "WHERE o.orderDate BETWEEN :startDate AND :endDate " +
            "AND o.device = :device")
    Long countOrdersByOrderDateBetweenAndDevice(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("device") DevicesList device);
    @Query("SELECT COUNT(o) FROM OrderManagement o " +
            "WHERE o.orderDate BETWEEN :startDate AND :endDate " )
    Long countOrdersByOrderDateBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(DISTINCT o.orderer) FROM OrderManagement o " +
            "WHERE o.orderDate BETWEEN :startDate AND :endDate " +
            "AND o.device = :device " +
            "AND o.orderer.joiningDate BETWEEN :startDate AND :endDate")
    Long countNewUsersByOrderDateAndJoiningDate(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("device") DevicesList device);
    @Query("SELECT COUNT(DISTINCT o.orderer) FROM OrderManagement o " +
            "WHERE o.orderDate BETWEEN :startDate AND :endDate " +
            "AND o.orderer.joiningDate BETWEEN :startDate AND :endDate")
    Long countNewUsersByOrderDate(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);


    @Query("SELECT COUNT(DISTINCT o.orderer) FROM OrderManagement o " +
            "WHERE o.orderDate BETWEEN :startDate AND :endDate " +
            "AND o.device = :device " +
            "GROUP BY o.orderer HAVING COUNT(DISTINCT o.code) >= 2")
    Long countRepeatBuyersByOrderDateAndDevice(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("device") DevicesList device);
    @Query("SELECT COUNT(DISTINCT o.orderer) FROM OrderManagement o " +
            "WHERE o.orderDate BETWEEN :startDate AND :endDate " +
            "GROUP BY o.orderer HAVING COUNT(DISTINCT o.code) >= 2")
    Long countRepeatBuyersByOrderDate(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT SUM(o.paymentAmount) FROM OrderManagement o " +
            "WHERE o.orderDate BETWEEN :startDate AND :endDate " +
            "AND o.device = :device AND o.payStatus = :paid")
    BigDecimal sumPaymentAmountByOrderDateBetweenAndDevice(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("device") DevicesList device,
            @Param("paid") PayStatus paid);
    @Query("SELECT SUM(o.paymentAmount) FROM OrderManagement o " +
            "WHERE o.orderDate BETWEEN :startDate AND :endDate " +
            "AND o.payStatus = :paidStatus ")
    BigDecimal sumByDeviceIncome(@Param("startDate") LocalDateTime startDate,
                                 @Param("endDate") LocalDateTime endDate,
                                 @Param("paidStatus") PayStatus paidStatus);
    @Query("SELECT SUM(o.paymentAmount) FROM OrderManagement o " +
            "WHERE o.orderDate < :startDate " +
            "AND o.payStatus = :paidStatus ")
    BigDecimal sumByOldDeviceIncome(@Param("startDate") LocalDateTime startDate,
                                 @Param("paidStatus") PayStatus paidStatus);

    List<OrderManagement> findByDeviceAndPayStatusAndOrderDateBetween(DevicesList devicesList, PayStatus payStatus, LocalDateTime startOfDate, LocalDateTime endOfDate);

  @Query("SELECT COUNT(o) FROM OrderManagement o WHERE o.payStatus = :payStatus ")
    long countByPayStatus(@Param("payStatus") PayStatus payStatus);



    @Query("SELECT o.transactionStatus FROM OrderManagement o WHERE o.code = :code ")
    TransactionStatus findTransactionStatusByCode(@Param("code") String code);
}

