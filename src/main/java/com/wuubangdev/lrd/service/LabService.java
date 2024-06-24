package com.wuubangdev.lrd.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.wuubangdev.lrd.domain.Lab;
import com.wuubangdev.lrd.domain.response.ResultPaginateDTO;
import com.wuubangdev.lrd.repository.LabRepository;

@Service
public class LabService {
    private final LabRepository labRepository;

    public LabService(LabRepository labRepository) {
        this.labRepository = labRepository;
    }

    public Lab fetchById(long id) {
        return this.labRepository.findById(id).orElse(null);
    }

    public Lab fetchByName(String name) {
        return this.labRepository.findByName(name);
    }

    public Lab cretae(Lab lab) {
        return this.labRepository.save(lab);
    }

    public ResultPaginateDTO<Lab> fetchAll(Specification<Lab> spec, Pageable pageable) {
        Page<Lab> pageLab = this.labRepository.findAll(spec, pageable);
        return new ResultPaginateDTO<>(pageLab);
    }

    public Lab update(Lab labDB, Lab lab) {
        labDB.setName(lab.getName());
        labDB.setTitle(lab.getTitle());
        labDB.setLabFunction(lab.getLabFunction());
        labDB.setPerform(lab.getPerform());
        labDB.setLabIteams(lab.getLabIteams());
        labDB.setTerm(lab.getTerm());
        lab.setSecienceField(lab.getSecienceField());
        return this.labRepository.save(labDB);
    }

    public void delete(Lab lab) {
        this.labRepository.delete(lab);
    }
}
