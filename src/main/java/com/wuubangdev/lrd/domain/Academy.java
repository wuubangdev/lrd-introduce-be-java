package com.wuubangdev.lrd.domain;

import com.wuubangdev.lrd.util.SecurityUtil;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "academy")
public class Academy {//Thong tin dao tao: cac nganh dao tao
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@NotBlank
	private String name;
	@NotBlank
	@Column(columnDefinition = "MEDIUMTEXT")
	private String shortDescription;
	@NotBlank
	@Column(columnDefinition = "MEDIUMTEXT")
	private String viewPoint;//Mục tiêu đào tạo
	@NotBlank
	@Column(columnDefinition = "MEDIUMTEXT")
	private String eduInformation; //Thông tin đào tạo
	@NotBlank
	@Column(columnDefinition = "MEDIUMTEXT")
	private String eduProgram; //Chương trình đào tạo
	@NotBlank
	@Column(columnDefinition = "MEDIUMTEXT")
	private String scienceActivity; //Công tác nghiên cứu khoa học
	@NotBlank
	@Column(columnDefinition = "MEDIUMTEXT")
	private String relationship;//Quan hệ hợp tác

	@NotBlank
	@Column(columnDefinition = "MEDIUMTEXT")
	private String jobPosition; //Vị trí việc làm khi ra trường

	@NotBlank
	@Column(columnDefinition = "MEDIUMTEXT")
	private String workingPlace; //Nơi làm việc sau khi ra trường

	private Instant createdAt;
	private Instant updatedAt;
	private String createdBy;
	private String updatedBy;

	@PostPersist
	public void handlePersist() {
		this.createdBy = SecurityUtil.getCurrentUserLogin().isPresent()
				? SecurityUtil.getCurrentUserLogin().get()
				: "";
		this.createdAt = Instant.now();
	}

	@PreUpdate
	public void handlePreUpdate() {
		this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent()
				? SecurityUtil.getCurrentUserLogin().get()
				: "";
		this.updatedAt = Instant.now();
	}

}
