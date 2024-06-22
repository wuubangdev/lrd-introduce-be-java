package com.wuubangdev.lrd.controller;

import com.turkraft.springfilter.boot.Filter;
import com.wuubangdev.lrd.domain.response.ResultPaginateDTO;
import com.wuubangdev.lrd.domain.user.User;
import com.wuubangdev.lrd.service.UserService;
import com.wuubangdev.lrd.util.ExceptionUtil.CheckExistException;
import com.wuubangdev.lrd.util.anotation.ApiMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UserController {
	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/users")
	@ApiMessage("Tạo người dùng thành công.")
	public ResponseEntity<User> create(@RequestBody User user)
			throws CheckExistException {
		if (this.userService.fetchByEmail(user.getEmail()) != null)
			throw new CheckExistException("Email " + user.getEmail() + " đã tồn tại vui lòng nhập email khác.");
		return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.create(user));
	}

	@GetMapping("/users/{id}")
	@ApiMessage("Lấy người dùng theo id")
	public ResponseEntity<User> getUserById(@PathVariable("id") long id) throws CheckExistException {
		User user = this.userService.fetchById(id);
		if (user == null)
			throw new CheckExistException("Người dùng có id: " + id + "không tồn tại");
		return ResponseEntity.ok(this.userService.convertAfterGet(user));
	}

	@GetMapping("/users")
	@ApiMessage("Lấy tất cả người dùng")
	public ResponseEntity<ResultPaginateDTO<User>> getAllUser(
			@Filter Specification<User> spec,
			Pageable pageable) {
		return ResponseEntity.ok(this.userService.fetchAll(spec, pageable));
	}

	@PutMapping("/users")
	@ApiMessage("Cập nhật người dùng")
	public ResponseEntity<User> putUpdateUser(@RequestBody User user) throws CheckExistException {
		User userDB = this.userService.fetchById(user.getId());
		if (userDB == null) throw new CheckExistException("Người dùng có id: " + user.getId() + " không tồn tại");
		User updatedUser = this.userService.update(userDB, user);
		return ResponseEntity.ok(this.userService.convertAfterGet(updatedUser));
	}

	@DeleteMapping("/users/{id}")
	@ApiMessage("Xóa người dùng")
	public ResponseEntity<Void> deleteUserById(@PathVariable("id") long id) throws CheckExistException {
		User user = this.userService.fetchById(id);
		if (user == null)
			throw new CheckExistException("Người dùng có id: " + id + "không tồn tại");
		this.userService.delete(user);
		return ResponseEntity.ok(null);
	}
}
