package com.wuubangdev.lrd.controller;

import com.turkraft.springfilter.boot.Filter;
import com.wuubangdev.lrd.domain.Permission;
import com.wuubangdev.lrd.domain.response.ResultPaginateDTO;
import com.wuubangdev.lrd.service.PermissionService;
import com.wuubangdev.lrd.util.ExceptionUtil.CheckExistException;
import com.wuubangdev.lrd.util.anotation.ApiMessage;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class PermissionController {
	private final PermissionService permissionService;

	public PermissionController(PermissionService permissionService) {
		this.permissionService = permissionService;
	}

	@PostMapping("/permissions")
	@ApiMessage("Create permission")
	public ResponseEntity<Permission> createPermission(@Valid @RequestBody Permission permission)
			throws CheckExistException {
		boolean isExist = this.permissionService
				.checkExist(permission.getName(),
						permission.getApiPath(),
						permission.getMethod());
		if (isExist)
			throw new CheckExistException("Permission da ton tai!");
		return ResponseEntity.status(HttpStatus.CREATED).body(this.permissionService.create(permission));
	}

	@GetMapping("/permissions/{id}")
	@ApiMessage("Get permission by id")
	public ResponseEntity<Permission> getPermissionById(@PathVariable("id") long id)
			throws CheckExistException {
		Permission currentPer = this.permissionService.fetchById(id);
		if (currentPer == null)
			throw new CheckExistException("Permission co id " + id + " khong ton tai!");
		return ResponseEntity.ok(currentPer);
	}

	@GetMapping("/permissions")
	@ApiMessage("Get all permissions")
	public ResponseEntity<ResultPaginateDTO<Permission>> getAllPermission(
			@Filter Specification<Permission> spec,
			Pageable pageable) {
		return ResponseEntity.ok(this.permissionService.fetchAll(spec, pageable));
	}

	@PutMapping("/permissions")
	@ApiMessage("Update permission")
	public ResponseEntity<Permission> updatePermission(@Valid @RequestBody Permission permission)
			throws CheckExistException {
		Permission permissionDB = this.permissionService.fetchById(permission.getId());
		if (permissionDB == null)
			throw new CheckExistException("Permission co id " + permission.getId() + " khong ton tai!");
		boolean isExist = this.permissionService
				.checkExist(permission.getName(),
						permission.getApiPath(),
						permission.getMethod());
		if (isExist)
			throw new CheckExistException("Permission da ton tai!");
		return ResponseEntity.status(HttpStatus.CREATED).body(this.permissionService.update(permissionDB, permission));
	}

	@DeleteMapping("/permissions/{id}")
	@ApiMessage("Delete permission by id")
	public ResponseEntity<Void> deletePermissionById(@PathVariable("id") long id)
			throws CheckExistException {
		Permission currentPer = this.permissionService.fetchById(id);
		if (currentPer == null)
			throw new CheckExistException("Permission co id " + id + " khong ton tai!");
		this.permissionService.delete(currentPer);
		return ResponseEntity.ok(null);
	}
}
