package com.wuubangdev.lrd.controller;

import com.turkraft.springfilter.boot.Filter;
import com.wuubangdev.lrd.domain.Academy;
import com.wuubangdev.lrd.domain.response.ResultPaginateDTO;
import com.wuubangdev.lrd.service.AcademyService;
import com.wuubangdev.lrd.util.ExceptionUtil.CheckExistException;
import com.wuubangdev.lrd.util.anotation.ApiMessage;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class AcademyController {
	private final AcademyService academyService;

	public AcademyController(AcademyService academyService) {
		this.academyService = academyService;
	}

	@PostMapping("/academy")
	@ApiMessage("Tạo nghành học thành công.")
	public ResponseEntity<Academy> create(@Valid @RequestBody Academy academy)
			throws CheckExistException {
		if (this.academyService.fetchByName(academy.getName()) != null)
			throw new CheckExistException("Tên nghành đã tồn tại vui lòng đặt tên khác!");
		return ResponseEntity.status(HttpStatus.CREATED).body(this.academyService.create(academy));
	}

	@GetMapping("/academy/{id}")
	@ApiMessage("Lấy nghành học.")
	public ResponseEntity<Academy> getById(@PathVariable("id") long id)
			throws CheckExistException {
		Academy academy = this.academyService.fetchById(id);
		if (academy == null) throw new CheckExistException("Nghành học với id: " + id + " không tồn tại.");
		return ResponseEntity.ok(academy);
	}

	@GetMapping("/academy")
	@ApiMessage("Lấy tất cả nghành học.")
	public ResponseEntity<ResultPaginateDTO<Academy>> getAll(
			@Filter Specification<Academy> spec,
			Pageable pageable) {
		return ResponseEntity.ok(this.academyService.fetchAll(spec, pageable));
	}

	@PutMapping("/academy")
	@ApiMessage("Cập nhật ngành học.")
	public ResponseEntity<Academy> update(@RequestBody Academy academy)
			throws CheckExistException {
		Academy academyDB = this.academyService.fetchById(academy.getId());
		if (academyDB == null)
			throw new CheckExistException("Nghành học với id: " + academy.getId() + " không tồn tại.");
		return ResponseEntity.ok(this.academyService.update(academyDB, academy));
	}

	@DeleteMapping("/academy/{id}")
	@ApiMessage("Xóa nghành học.")
	public ResponseEntity<Void> delete(@PathVariable("id") long id)
			throws CheckExistException {
		Academy academy = this.academyService.fetchById(id);
		if (academy == null) throw new CheckExistException("Nghành học với id: " + id + " không tồn tại.");
		this.academyService.delete(academy);
		return ResponseEntity.ok(null);
	}
}
