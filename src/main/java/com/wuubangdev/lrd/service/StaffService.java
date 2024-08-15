package com.wuubangdev.lrd.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.wuubangdev.lrd.domain.Staff;
import com.wuubangdev.lrd.domain.response.ResultPaginateDTO;
import com.wuubangdev.lrd.repository.StaffRepository;

@Service
public class StaffService {

    private final StaffRepository staffRepository;

    public StaffService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    public Staff fetchById(long id) {
        return this.staffRepository.findById(id).orElse(null);
    }

    public Staff fetchByName(String name) {
        return this.staffRepository.findByName(name);
    }

    public ResultPaginateDTO<Staff> fetchAll(Specification<Staff> spec, Pageable pageable) {
        Page<Staff> staffPage = this.staffRepository.findAll(spec, pageable);
        return new ResultPaginateDTO<Staff>(staffPage);
    }

    public Staff create(Staff staff) {
        return this.staffRepository.save(staff);
    }

    public Staff update(Staff staffDB, Staff staffUpdate) {
        staffDB.setName(staffUpdate.getName());
        staffDB.setGender(staffUpdate.getGender());
        staffDB.setBirthDay(staffUpdate.getBirthDay());
        staffDB.setBorn(staffUpdate.getBorn());
        staffDB.setAddress(staffUpdate.getAddress());
        staffDB.setPosition(staffUpdate.getPosition());
        staffDB.setOffice(staffUpdate.getOffice());
        staffDB.setCivilServants(staffUpdate.getCivilServants());
        staffDB.setYearExp(staffUpdate.getYearExp());
        staffDB.setLevel(staffUpdate.getLevel());
        staffDB.setTrainingProcess(staffUpdate.getTrainingProcess());
        staffDB.setWorkingProcess(staffUpdate.getWorkingProcess());
        return this.staffRepository.save(staffDB);
    }

    public void delete(Staff staff) {
        this.staffRepository.delete(staff);
    }

}
