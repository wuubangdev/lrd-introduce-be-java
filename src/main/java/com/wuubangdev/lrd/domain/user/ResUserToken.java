package com.wuubangdev.lrd.domain.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wuubangdev.lrd.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ResUserToken {
	private String access_token;
	@JsonProperty("user")
	private UserLogin userLogin;

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class UserLogin {
		private long id;
		private String email;
		private String name;
		private Role role;
	}

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class UserGetAccount {
		private UserLogin user;
	}

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class UserInsideLogin {
		private long id;
		private String email;
		private String name;
	}
}