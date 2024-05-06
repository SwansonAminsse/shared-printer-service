package com.yn.printer.service.modules.operation.repository;

import com.yn.printer.service.modules.meta.entity.Tutorials;
import com.yn.printer.service.modules.operation.enums.TutorialTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TutorialsRepository extends JpaRepository<Tutorials, Long> , JpaSpecificationExecutor<Tutorials> {


    Tutorials findByTutorialType(TutorialTypes tutorialType);
}