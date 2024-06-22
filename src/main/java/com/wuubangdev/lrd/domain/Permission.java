package com.wuubangdev.lrd.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wuubangdev.lrd.util.SecurityUtil;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "permissions")
@NoArgsConstructor
public class Permission {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@NotBlank(message = "Name khong duoc de trong.")
	private String name;
	@NotBlank(message = "ApiPath khong duoc de trong.")
	private String apiPath;
	@NotBlank(message = "Method khong duoc de trong.")
	private String method;
	@NotBlank(message = "Module khong duoc de trong.")
	private String module;

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "permissions")
	@JsonIgnore
	private List<Role> roles;

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

	public Permission(
			@NotBlank(message = "Name khong duoc de trong.")
			String name,
			@NotBlank(message = "ApiPath khong duoc de trong.")
			String apiPath,
			@NotBlank(message = "Method khong duoc de trong.")
			String method,
			@NotBlank(message = "Module khong duoc de trong.")
			String module) {
		this.name = name;
		this.apiPath = apiPath;
		this.method = method;
		this.module = module;
	}

}
