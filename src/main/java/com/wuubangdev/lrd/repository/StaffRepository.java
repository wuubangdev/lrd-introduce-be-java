package com.wuubangdev.lrd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.wuubangdev.lrd.domain.Staff;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long>, JpaSpecificationExecutor<Staff> {
    Staff findByEmail(String email);

    Staff findByName(String name);
}
