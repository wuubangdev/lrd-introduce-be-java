package com.wuubangdev.lrd.controller;

import com.turkraft.springfilter.boot.Filter;
import com.wuubangdev.lrd.domain.Role;
import com.wuubangdev.lrd.domain.response.ResultPaginateDTO;
import com.wuubangdev.lrd.service.RoleService;
import com.wuubangdev.lrd.util.ExceptionUtil.CheckExistException;
import com.wuubangdev.lrd.util.anotation.ApiMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class RoleController {
	private final RoleService roleService;

	public RoleController(RoleService roleService) {
		this.roleService = roleService;
	}

	@PostMapping("/roles")
	@ApiMessage("Create role")
	public ResponseEntity<Role> createRole(@RequestBody Role role)
			throws CheckExistException {
		boolean isNameExist = this.roleService.isNameExist(role.getName());
		if (isNameExist)
			throw new CheckExistException("Role name: " + role.getName() + " da ton tai.");
		return ResponseEntity.status(HttpStatus.CREATED).body(this.roleService.create(role));
	}

	@GetMapping("/roles/{id}")
	@ApiMessage("Get role by id")
	public ResponseEntity<Role> getRoleById(@PathVariable("id") long id)
			throws CheckExistException {
		Role role = this.roleService.fetchById(id);
		if (role == null)
			throw new CheckExistException("Role voi id: " + id + " khong ton tai.");
		return ResponseEntity.ok(role);
	}

	@GetMapping("/roles")
	@ApiMessage("Get all role")
	public ResponseEntity<ResultPaginateDTO<Role>> getAllRole(
			@Filter Specification<Role> spec,
			Pageable pageable) {
		return ResponseEntity.ok(this.roleService.fetchAllRole(spec, pageable));
	}

	@PutMapping("roles")
	public ResponseEntity<Role> updateRole(@RequestBody Role role)
			throws CheckExistException {
		Role roleDB = this.roleService.fetchById(role.getId());
		if (roleDB == null)
			throw new CheckExistException("Role voi id: " + role.getId() + " khong ton tai.");
		// if (this.roleService.isNameExist(role.getName()))
		// throw new CheckExistException("Role name da ton tai.");
		return ResponseEntity.ok(this.roleService.update(roleDB, role));
	}

	@DeleteMapping("/roles/{id}")
	@ApiMessage("Delete role")
	public ResponseEntity<Role> deleteRole(@PathVariable("id") long id)
			throws CheckExistException {
		Role role = this.roleService.fetchById(id);
		if (role == null)
			throw new CheckExistException("Role voi id: " + id + " khong ton tai.");
		this.roleService.delete(role);
		return ResponseEntity.ok(null);
	}
}
