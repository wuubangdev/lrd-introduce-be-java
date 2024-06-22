package com.wuubangdev.lrd.repository;

import com.wuubangdev.lrd.domain.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long>, JpaSpecificationExecutor<Permission> {
	// Permission findByApiPath(String apiPath);
	// Permission findByMethod(String method);

	Permission findByNameAndApiPathAndMethod(String name, String apiPath, String method);

	List<Permission> findByIdIn(List<Long> id);

}
