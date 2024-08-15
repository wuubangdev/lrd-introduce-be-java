package com.wuubangdev.lrd.controller;

import com.turkraft.springfilter.boot.Filter;
import com.wuubangdev.lrd.domain.Staff;
import com.wuubangdev.lrd.domain.response.ResultPaginateDTO;
import com.wuubangdev.lrd.service.StaffService;
import com.wuubangdev.lrd.util.ExceptionUtil.CheckExistException;
import com.wuubangdev.lrd.util.anotation.ApiMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class StaffController {
	private final StaffService staffService;

	public StaffController(StaffService staffService) {
		this.staffService = staffService;
	}

	@PostMapping("/staffs")
	@ApiMessage("Tạo người dùng thành công.")
	public ResponseEntity<Staff> create(@RequestBody Staff staff)
			throws CheckExistException {
		return ResponseEntity.status(HttpStatus.CREATED).body(this.staffService.create(staff));
	}

	@GetMapping("/staffs/{id}")
	@ApiMessage("Lấy người dùng theo id")
	public ResponseEntity<Staff> getUserById(@PathVariable("id") long id) throws CheckExistException {
		Staff staff = this.staffService.fetchById(id);
		if (staff == null)
			throw new CheckExistException("Người dùng có id: " + id + "không tồn tại");
		return ResponseEntity.ok(staff);
	}

	@GetMapping("/staffs")
	@ApiMessage("Lấy tất cả người dùng")
	public ResponseEntity<ResultPaginateDTO<Staff>> getAllUser(
			@Filter Specification<Staff> spec,
			Pageable pageable) {
		return ResponseEntity.ok(this.staffService.fetchAll(spec, pageable));
	}

	@PutMapping("/staffs")
	@ApiMessage("Cập nhật người dùng")
	public ResponseEntity<Staff> putUpdateUser(@RequestBody Staff staff) throws CheckExistException {
		Staff userDB = this.staffService.fetchById(staff.getId());
		if (userDB == null)
			throw new CheckExistException("Người dùng có id: " + staff.getId() + " không tồn tại");
		Staff updatedUser = this.staffService.update(userDB, staff);
		return ResponseEntity.ok(updatedUser);
	}

	@DeleteMapping("/staffs/{id}")
	@ApiMessage("Xóa người dùng")
	public ResponseEntity<Void> deleteUserById(@PathVariable("id") long id) throws CheckExistException {
		Staff staff = this.staffService.fetchById(id);
		if (staff == null)
			throw new CheckExistException("Người dùng có id: " + id + "không tồn tại");
		this.staffService.delete(staff);
		return ResponseEntity.ok(null);
	}
}
