package com.wuubangdev.lrd.config;

import com.wuubangdev.lrd.service.UserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component("userDetailsService")
public class CustomUserDetails implements UserDetailsService {
	private final UserService userService;

	public CustomUserDetails(UserService userService) {
		this.userService = userService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		com.wuubangdev.lrd.domain.user.User user = this.userService.fetchByEmail(username);
		return new User(
				user.getEmail(),
				user.getPassword(),
				Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
	}
}
