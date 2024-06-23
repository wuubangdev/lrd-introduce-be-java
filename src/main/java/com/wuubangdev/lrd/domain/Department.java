package com.wuubangdev.lrd.domain;

import com.wuubangdev.lrd.util.SecurityUtil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "department")
public class Department { //Thông tin về bộ môn
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name; //Tiêu đề thông tin
	@Column(columnDefinition = "MEDIUMTEXT")
	private String description;//Nội dung thông tin

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
