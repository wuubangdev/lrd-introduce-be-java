package com.wuubangdev.lrd.domain.user;

import com.wuubangdev.lrd.domain.Role;
import com.wuubangdev.lrd.util.SecurityUtil;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@NotBlank(message = "Email không được để trống.")
	private String email;
	@NotBlank(message = "Password không được để trống.")
	private String password;
	@NotBlank(message = "Tên không được để trống.")
	private String name;
	private UserGender gender;
	private Instant birthDay;
	private String born; // Noi sinh
	private String address;
	private String position; // Chuc vu
	private String office; // Vi tri lam viec
	private String civilServants; // Ngach vien chuc
	private int yearExp;
	private String level;
	@Column(columnDefinition = "MEDIUMTEXT")
	private String trainingProcess; // Qua trinh dao tao
	@Column(columnDefinition = "MEDIUMTEXT")
	private String workingProcess; // Qua trinh lam viec

	@ManyToOne
	@JoinColumn(name = "role_id")
	private Role role;

	@Column(columnDefinition = "MEDIUMTEXT")
	private String refreshToken;

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
