package com.wuubangdev.lrd.domain.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {
	@NotBlank(message = "Email không được để trống.")
	private String email;
	@NotBlank(message = "Mật khẩu không được để trống.")
	private String password;
}
