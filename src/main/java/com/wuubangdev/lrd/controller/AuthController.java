package com.wuubangdev.lrd.controller;

import com.wuubangdev.lrd.domain.user.LoginDTO;
import com.wuubangdev.lrd.domain.user.ResUserToken;
import com.wuubangdev.lrd.domain.user.User;
import com.wuubangdev.lrd.service.UserService;
import com.wuubangdev.lrd.util.ExceptionUtil.CheckExistException;
import com.wuubangdev.lrd.util.JwtUtil;
import com.wuubangdev.lrd.util.SecurityUtil;
import com.wuubangdev.lrd.util.anotation.ApiMessage;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final SecurityUtil securityUtil;
	private final JwtUtil jwtUtil;
	private final UserService userService;
	private final PasswordEncoder passwordEncoder;

	public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil,
			JwtUtil jwtUtil, UserService userService, PasswordEncoder passwordEncoder) {
		this.authenticationManagerBuilder = authenticationManagerBuilder;
		this.securityUtil = securityUtil;
		this.jwtUtil = jwtUtil;
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
	}

	@PostMapping("/auth/login")
	@ApiMessage("Đăng nhập thành công.")
	public ResponseEntity<ResUserToken> postLogin(@Valid @RequestBody LoginDTO loginDTO) {

		// Nạp input gồm username/password vào Security
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				loginDTO.getEmail(), loginDTO.getPassword());

		// xác thực người dùng => cần viết hàm loadUserByUsername
		Authentication authentication = authenticationManagerBuilder.getObject()
				.authenticate(authenticationToken);

		// nạp thông tin (nếu xử lý thành công) vào SecurityContext
		SecurityContextHolder.getContext().setAuthentication(authentication);

		ResUserToken resUserToken = new ResUserToken();

		User currentUserDB = this.userService.fetchByEmail(loginDTO.getEmail());
		if (currentUserDB != null) {
			ResUserToken.UserLogin userLogin = new ResUserToken.UserLogin(
					currentUserDB.getId(),
					currentUserDB.getEmail(),
					currentUserDB.getName(),
					currentUserDB.getRole());
			resUserToken.setUserLogin(userLogin);

		}
		// Tao access token
		String access_token = this.securityUtil.createAccessToken(authentication.getName(), resUserToken);

		resUserToken.setAccess_token(access_token);
		// Create refresh token
		String refresh_token = this.securityUtil.createRefreshToken(authentication.getName(), resUserToken);
		this.userService.updateUserToken(refresh_token, loginDTO.getEmail());
		// Create ResponseCookie refresh token
		ResponseCookie responseCookie = ResponseCookie.from("refresh_token", refresh_token)
				.httpOnly(true)
				.secure(true)
				.maxAge(this.jwtUtil.getRefreshExpiration())
				.path("/")
				.build();

		return ResponseEntity
				.ok()
				.header(HttpHeaders.SET_COOKIE, responseCookie.toString())
				.body(resUserToken);
	}

	@GetMapping("/auth/account")
	@ApiMessage("Lấy tài khoản thành công.")
	public ResponseEntity<ResUserToken.UserGetAccount> getAccount() {
		String email = SecurityUtil.getCurrentUserLogin().isPresent()
				? SecurityUtil.getCurrentUserLogin().get()
				: "";
		User currentUserDB = this.userService.fetchByEmail(email);
		ResUserToken.UserLogin userLogin = new ResUserToken.UserLogin();
		ResUserToken.UserGetAccount userGetAccount = new ResUserToken.UserGetAccount();
		if (currentUserDB != null) {
			userLogin.setId(currentUserDB.getId());
			userLogin.setEmail(currentUserDB.getEmail());
			userLogin.setName(currentUserDB.getName());
			userLogin.setRole(currentUserDB.getRole());
			userGetAccount.setUser(userLogin);
		}
		return ResponseEntity.ok().body(userGetAccount);
	}

	@GetMapping("/auth/refresh")
	@ApiMessage("Làm mới refresh token")
	public ResponseEntity<ResUserToken> getNewToken(
			@CookieValue(name = "refresh_token", defaultValue = "abc") String refresh_token)
			throws CheckExistException {
		if (refresh_token.equals("abc")) {
			throw new CheckExistException("Bạn không có refresh token ở cookie.");
		}
		// check valid
		Jwt decodedToken = this.securityUtil.checkValidRefreshToken(refresh_token);
		String email = decodedToken.getSubject();
		// check user by token + email
		User currentUser = this.userService.getUserByRefreshTokenAndEmail(refresh_token, email);
		if (currentUser == null) {
			throw new CheckExistException("Refresh Token không hợp lệ.");
		}

		// issue new token/set refresh token as cookie
		ResUserToken resUserToken = new ResUserToken();

		User currentUserDB = this.userService.fetchByEmail(email);
		if (currentUserDB != null) {
			ResUserToken.UserLogin userLogin = new ResUserToken.UserLogin(
					currentUserDB.getId(),
					currentUserDB.getEmail(),
					currentUserDB.getName(),
					currentUserDB.getRole());
			resUserToken.setUserLogin(userLogin);

		}
		// Tao access token
		String access_token = this.securityUtil.createAccessToken(email, resUserToken);

		resUserToken.setAccess_token(access_token);
		// Create refresh token
		String new_refresh_token = this.securityUtil.createRefreshToken(email, resUserToken);
		this.userService.updateUserToken(new_refresh_token, email);
		// Create ResponseCookie refresh token
		ResponseCookie responseCookie = ResponseCookie
				.from("refresh_token", new_refresh_token)
				.httpOnly(true)
				.secure(true)
				.maxAge(this.jwtUtil.getRefreshExpiration())
				.path("/")
				.build();

		return ResponseEntity
				.ok()
				.header(HttpHeaders.SET_COOKIE, responseCookie.toString())
				.body(resUserToken);
	}

	@PostMapping("/auth/logout")
	@ApiMessage("Đăng xuất thành công")
	public ResponseEntity<Void> logout()
			throws CheckExistException {
		String email = SecurityUtil.getCurrentUserLogin().isPresent()
				? SecurityUtil.getCurrentUserLogin().get()
				: "";
		if (email.isEmpty()) {
			throw new CheckExistException("Token không hợp lệ.");
		}
		// update refresh token = null
		this.userService.updateUserToken(null, email);
		// remove refresh token cookie
		ResponseCookie removeCookie = ResponseCookie
				.from("refresh_token", "")
				.httpOnly(true)
				.secure(true)
				.path("/")
				.maxAge(0)
				.build();
		return ResponseEntity
				.ok()
				.header(HttpHeaders.SET_COOKIE, removeCookie.toString())
				.body(null);
	}

	@PostMapping("/auth/register")
	@ApiMessage("Đăng ký người dùng thành công.")
	public ResponseEntity<User> register(@Valid @RequestBody User user) throws CheckExistException {
		User curentUser = this.userService.fetchByEmail(user.getEmail());
		if (curentUser != null)
			throw new CheckExistException(
					"Email " + user.getEmail() + " đã tồn tại vui lòng chọn email khác.");
		User createdUser = this.userService.create(user);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(this.userService.convertAfterCreate(createdUser));
	}

}
