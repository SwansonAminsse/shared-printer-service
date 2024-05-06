package com.yn.printer.service.modules.meta.repository;

import com.yn.printer.service.modules.meta.entity.MetaSequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 序列Repository
 *
 * @author huabiao
 * @create 2023/12/11  09:43
 **/
@Repository
public interface MetaSequenceRepository extends JpaRepository<MetaSequence, Long> {

    /**
     * 序列-查询
     *
     * @param name 序列名称
     * @return 序列
     * @author huabiao
     * @create 2023/12/11  14:29
     */
    MetaSequence findByName(String name);
}
