package com.wuubangdev.lrd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.wuubangdev.lrd.domain.Lab;

@Repository
public interface LabRepository extends JpaRepository<Lab, Long>, JpaSpecificationExecutor<Lab> {
    Lab findByName(String name);
}
