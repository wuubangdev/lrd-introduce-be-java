package com.wuubangdev.lrd.service;

import com.wuubangdev.lrd.domain.Permission;
import com.wuubangdev.lrd.domain.Role;
import com.wuubangdev.lrd.domain.response.ResultPaginateDTO;
import com.wuubangdev.lrd.repository.PermissionRepository;
import com.wuubangdev.lrd.repository.RoleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleService {
	private final RoleRepository roleRepository;
	private final PermissionRepository permissionRepository;

	public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
		this.roleRepository = roleRepository;
		this.permissionRepository = permissionRepository;
	}

	public Role fetchById(long id) {
		Optional<Role> OpRole = this.roleRepository.findById(id);
		return OpRole.orElse(null);
	}

	public boolean isNameExist(String name) {
		Role role = this.roleRepository.findByName(name);
		return role != null;
	}

	public Role create(Role role) {
		if (role.getPermissions() != null) {
			List<Permission> permissions = this.permissionRepository.findByIdIn(
					role.getPermissions()
							.stream().map(Permission::getId)
							.collect(Collectors.toList()));
			role.setPermissions(permissions);
		}
		return this.roleRepository.save(role);
	}

	public ResultPaginateDTO<Role> fetchAllRole(Specification<Role> spec, Pageable pageable) {
		Page<Role> pageRole = this.roleRepository.findAll(spec, pageable);
		return new ResultPaginateDTO<>(pageRole);
	}

	public Role update(Role roleDB, Role role) {
		if (role.getPermissions() != null) {
			List<Permission> permissions = this.permissionRepository.findByIdIn(
					role.getPermissions()
							.stream().map(Permission::getId)
							.collect(Collectors.toList()));
			roleDB.setPermissions(permissions);
		}
		roleDB.setName(role.getName());
		roleDB.setDescription(role.getDescription());
		roleDB.setActive(role.isActive());
		return this.roleRepository.save(roleDB);
	}

	public void delete(Role role) {
		this.roleRepository.delete(role);
	}
}