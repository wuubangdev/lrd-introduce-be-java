package com.wuubangdev.lrd.service;

import com.wuubangdev.lrd.domain.Role;
import com.wuubangdev.lrd.domain.response.ResultPaginateDTO;
import com.wuubangdev.lrd.domain.user.User;
import com.wuubangdev.lrd.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class UserService {
	private final UserRepository userRepository;
	private final RoleService roleService;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, RoleService roleService, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleService = roleService;
		this.passwordEncoder = passwordEncoder;
	}

	public User fetchById(long id) {
		return this.userRepository.findById(id).orElse(null);
	}

	public User fetchByEmail(String email) {
		return this.userRepository.findByEmail(email);
	}

	public User create(User user) {
		if (user.getRole() != null) {
			Role role = this.roleService.fetchById(user.getRole().getId());
			user.setRole(role);
		}
		user.setPassword(this.passwordEncoder.encode(user.getPassword()));
		user = this.userRepository.save(user);
		return this.convertAfterCreate(user);
	}

	public User update(User userBD, User userUpdate) {
		if (userUpdate.getRole() != null) {
			Role role = this.roleService.fetchById(userUpdate.getRole().getId());
			userBD.setRole(role);
		}
		userBD.setName(userUpdate.getName());
		userBD.setGender(userUpdate.getGender());
		userBD.setBirthDay(userUpdate.getBirthDay());
		userBD.setBorn(userUpdate.getBorn());
		userBD.setAddress(userUpdate.getAddress());
		userBD.setPosition(userUpdate.getPosition());
		userBD.setOffice(userUpdate.getOffice());
		userBD.setCivilServants(userUpdate.getCivilServants());
		userBD.setYearExp(userUpdate.getYearExp());
		userBD.setLevel(userUpdate.getLevel());
		userBD.setTrainingProcess(userUpdate.getTrainingProcess());
		userBD.setWorkingProcess(userUpdate.getWorkingProcess());
		return this.userRepository.save(userBD);
	}

	public ResultPaginateDTO<User> fetchAll(Specification<User> spec, Pageable pageable) {
		Page<User> pageUser = this.userRepository.findAll(spec, pageable);
		ResultPaginateDTO<User> rsp = new ResultPaginateDTO<>(pageUser);
		rsp.setResult(pageUser.getContent()
				.stream().map(this::convertAfterGet)
				.collect(Collectors.toList()));
		return rsp;
	}

	public void delete(User user) {
		this.userRepository.delete(user);
	}

	public User convertAfterCreate(User user) {
		user.setPassword("PROTECTED");
		user.setRefreshToken(null);
		user.setUpdatedAt(null);
		user.setCreatedAt(null);
		return user;
	}

	public User convertAfterGet(User user) {
		user.setPassword("PROTECTED");
		user.setRefreshToken("PROTECTED");
		return user;
	}


	public void updateUserToken(String refreshToken, String email) {
		User currentUser = this.userRepository.findByEmail(email);
		if (currentUser != null) {
			currentUser.setRefreshToken(refreshToken);
			this.userRepository.save(currentUser);
		}
	}

	public User getUserByRefreshTokenAndEmail(String refreshToken, String email) {
		return this.userRepository.findByRefreshTokenAndEmail(refreshToken, email);
	}
}
