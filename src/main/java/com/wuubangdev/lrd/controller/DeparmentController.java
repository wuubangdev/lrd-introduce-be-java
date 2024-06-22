package com.wuubangdev.lrd.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wuubangdev.lrd.domain.Department;
import com.wuubangdev.lrd.service.DepartmentService;
import com.wuubangdev.lrd.util.ExceptionUtil.CheckExistException;
import com.wuubangdev.lrd.util.anotation.ApiMessage;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1")
public class DeparmentController {
    private final DepartmentService departmentService;

    public DeparmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping("/department")
    @ApiMessage("Tạo thông tin bộ môn.")
    public ResponseEntity<Department> create(@RequestBody Department department) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.departmentService.create(department));
    }

    @GetMapping("/department/{id}")
    @ApiMessage("Lấy thông tin bộ môn.")
    public ResponseEntity<Department> getDeparmentById(@PathVariable("id") long id)
            throws CheckExistException {
        Department departmentDB = this.departmentService.fetchById(id);
        if (departmentDB == null)
            throw new CheckExistException("Thông tin cần lấy không tồn tại.");
        return ResponseEntity.ok(departmentDB);
    }

    @GetMapping("/department")
    @ApiMessage("Lấy tất cả thông tin bộ môn.")
    public ResponseEntity<List<Department>> getAllDeparment() throws CheckExistException {
        List<Department> departments = this.departmentService.fetchAll();
        if (departments.isEmpty())
            throw new CheckExistException("Thông tin cần lấy không tồn tại.");
        return ResponseEntity.ok(departments);
    }

    @PutMapping("/department")
    @ApiMessage("Cập nhật thông tin bộ môn.")
    public ResponseEntity<Department> update(@RequestBody Department department)
            throws CheckExistException {
        Department departmentDB = this.departmentService.fetchById(department.getId());
        if (departmentDB == null)
            throw new CheckExistException("Thông tin cần cập nhật không tồn tại.");
        return ResponseEntity.ok(this.departmentService.update(departmentDB, department));
    }

    @DeleteMapping("/department/{id}")
    @ApiMessage("Xoá thông tin bộ môn.")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws CheckExistException {
        Department departmentDB = this.departmentService.fetchById(id);
        if (departmentDB == null)
            throw new CheckExistException("Thông tin cần xoá không tồn tại.");
        this.departmentService.delete(departmentDB);
        return ResponseEntity.ok(null);
    }
}
