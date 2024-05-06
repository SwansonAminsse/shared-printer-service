package com.yn.printer.service.modules.orders.repository;


import com.yn.printer.service.modules.orders.entity.OrderManagement;
import com.yn.printer.service.modules.orders.entity.PrintTask;
import com.yn.printer.service.modules.orders.enums.PrintTaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrintTaskRepository extends JpaRepository<PrintTask, Long> {

    List<PrintTask> findByOrdersAndPrintTaskStatus(OrderManagement orders, PrintTaskStatus printTaskStatus);

    List<PrintTask> findByOrders(OrderManagement orders);

    PrintTask findByCode(String code);
}

