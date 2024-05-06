package com.yn.printer.service.modules.advertisement.repository;

import com.yn.printer.service.modules.advertisement.entity.Placement;
import com.yn.printer.service.modules.advertisement.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RegistrationRepository extends JpaRepository<Registration, Long>, JpaSpecificationExecutor<Registration> {
}
