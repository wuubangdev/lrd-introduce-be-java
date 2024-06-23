package com.wuubangdev.lrd.service;

import com.wuubangdev.lrd.domain.Academy;
import com.wuubangdev.lrd.domain.response.ResultPaginateDTO;
import com.wuubangdev.lrd.repository.AcademyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class AcademyService {
	private final AcademyRepository academyRepository;

	public AcademyService(AcademyRepository academyRepository) {
		this.academyRepository = academyRepository;
	}

	public Academy fetchById(long id) {
		return this.academyRepository.findById(id).orElse(null);
	}

	public Academy fetchByName(String name) {
		return this.academyRepository.findByName(name);
	}

	public ResultPaginateDTO<Academy> fetchAll(Specification<Academy> spec, Pageable pageable) {
		Page<Academy> academyPage = this.academyRepository.findAll(spec, pageable);
		return new ResultPaginateDTO<Academy>(academyPage);
	}

	public Academy create(Academy academy) {
		return this.academyRepository.save(academy);
	}

	public Academy update(Academy academyDB, Academy academy) {
		academyDB.setName(academy.getName());
		academyDB.setShortDescription(academy.getShortDescription());
		academyDB.setViewPoint(academy.getViewPoint());
		academyDB.setEduInformation(academy.getEduInformation());
		academyDB.setEduProgram(academy.getEduProgram());
		academyDB.setScienceActivity(academy.getScienceActivity());
		academyDB.setRelationship(academy.getRelationship());
		academyDB.setJobPosition(academy.getJobPosition());
		academyDB.setWorkingPlace(academy.getWorkingPlace());
		return this.academyRepository.save(academyDB);
	}

	public void delete(Academy academy) {
		this.academyRepository.delete(academy);
	}

}
