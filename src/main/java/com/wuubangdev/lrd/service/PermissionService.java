package com.wuubangdev.lrd.service;

import com.wuubangdev.lrd.domain.Permission;
import com.wuubangdev.lrd.domain.response.ResultPaginateDTO;
import com.wuubangdev.lrd.repository.PermissionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PermissionService {
	private final PermissionRepository permissionRepository;

	public PermissionService(PermissionRepository permissionRepository) {
		this.permissionRepository = permissionRepository;
	}

	public boolean checkExist(String name, String apiPath, String method) {
		Permission permission = this.permissionRepository.findByNameAndApiPathAndMethod(name, apiPath, method);
		return permission != null;
	}

	public Permission fetchById(long id) {
		Optional<Permission> opPer = this.permissionRepository.findById(id);
		return opPer.orElse(null);
	}

	public Permission create(Permission p) {
		return this.permissionRepository.save(p);
	}

	public ResultPaginateDTO<Permission> fetchAll(Specification<Permission> spec, Pageable pageable) {
		Page<Permission> pagePer = this.permissionRepository.findAll(spec, pageable);
		return new ResultPaginateDTO<>(pagePer);
	}

	public Permission update(Permission permissionDB, Permission p) {
		permissionDB.setName(p.getName());
		permissionDB.setApiPath(p.getApiPath());
		permissionDB.setMethod(p.getMethod());
		permissionDB.setModule(p.getModule());
		return this.permissionRepository.save(permissionDB);
	}

	public void delete(Permission permission) {
		if (permission.getRoles() != null) {
			permission.getRoles().forEach(role -> role.getPermissions().remove(permission));
		}
		this.permissionRepository.delete(permission);
	}
}
