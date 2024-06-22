package com.wuubangdev.lrd.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wuubangdev.lrd.domain.user.User;
import com.wuubangdev.lrd.util.SecurityUtil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name;
	private String description;
	private boolean active;

	@ManyToMany(fetch = FetchType.LAZY)
	@JsonIgnoreProperties(value = {"permissions"})
	@JoinTable(name = "permission_role", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "permission_id"))
	private List<Permission> permissions;

	@OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<User> users;

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
