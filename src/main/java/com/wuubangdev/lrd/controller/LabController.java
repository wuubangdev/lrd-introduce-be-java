package com.wuubangdev.lrd.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;
import com.wuubangdev.lrd.domain.Lab;
import com.wuubangdev.lrd.domain.response.ResultPaginateDTO;
import com.wuubangdev.lrd.service.LabService;
import com.wuubangdev.lrd.util.ExceptionUtil.CheckExistException;
import com.wuubangdev.lrd.util.anotation.ApiMessage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1")
public class LabController {
    private final LabService labService;

    public LabController(LabService labService) {
        this.labService = labService;
    }

    @GetMapping("/labs/{id}")
    @ApiMessage("Lấy thông tin phòng thí nghiệm thành công.")
    public ResponseEntity<Lab> getById(@PathVariable("id") long id)
            throws CheckExistException {
        Lab lab = this.labService.fetchById(id);
        if (lab == null)
            throw new CheckExistException("Phòng thí nghiệm cần lấy không tồn tại.");
        return ResponseEntity.status(HttpStatus.CREATED).body(lab);
    }

    @PostMapping("/labs")
    @ApiMessage("Tạo thông tin tất cả phòng thí nghiệm thành công.")
    public ResponseEntity<Lab> create(@RequestBody Lab lab)
            throws CheckExistException {
        Lab labDB = this.labService.fetchByName(lab.getName());
        if (labDB != null)
            throw new CheckExistException("Tên phòng thí nghiệm đã tồn tại, vui lòng chọn tên khác.");
        return ResponseEntity.ok(this.labService.cretae(lab));
    }

    @GetMapping("/labs")
    public ResponseEntity<ResultPaginateDTO<Lab>> getAll(
            @Filter Specification<Lab> spec,
            Pageable pageable) {
        return ResponseEntity.ok(this.labService.fetchAll(spec, pageable));
    }

    @PutMapping("/labs")
    @ApiMessage("Cập nhật thông tin phòng thí nghiệm thành công.")
    public ResponseEntity<Lab> update(@RequestBody Lab lab)
            throws CheckExistException {
        Lab labDB = this.labService.fetchById(lab.getId());
        if (labDB == null)
            throw new CheckExistException("Phòng thí nghiệm cần cập nhật không tồn tại.");
        if (this.labService.fetchByName(lab.getName()) != null)
            throw new CheckExistException("Tên phòng thí nghiệm đã tồn tại, vui lòng chọn tên khác.");
        return ResponseEntity.ok(this.labService.update(labDB, lab));
    }

    @DeleteMapping("/labs/{id}")
    @ApiMessage("Xoá thông tin phòng thí nghiệm thành công.")
    public ResponseEntity<Void> delete(@PathVariable("id") long id)
            throws CheckExistException {
        Lab lab = this.labService.fetchById(id);
        if (lab == null)
            throw new CheckExistException("Phòng thí nghiệm cần xoá không tồn tại.");
        this.labService.delete(lab);
        return ResponseEntity.ok(null);
    }
}
