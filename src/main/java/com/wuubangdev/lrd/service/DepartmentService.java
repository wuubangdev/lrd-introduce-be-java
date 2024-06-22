package com.wuubangdev.lrd.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wuubangdev.lrd.domain.Department;
import com.wuubangdev.lrd.repository.DepartmentRepository;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public Department fetchById(long id) {
        return this.departmentRepository.findById(id).orElse(null);
    }

    public List<Department> fetchAll() {
        return this.departmentRepository.findAll();
    }

    public Department create(Department department) {
        return this.departmentRepository.save(department);
    }

    public Department update(Department departmentDB, Department department) {
        departmentDB.setName(department.getName());
        departmentDB.setDescription(department.getDescription());
        return this.departmentRepository.save(departmentDB);
    }

    public void delete(Department department) {
        this.departmentRepository.delete(department);
    }

}
