package com.wuubangdev.lrd.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wuubangdev.lrd.domain.response.RestResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
	private final AuthenticationEntryPoint delegate = new BearerTokenAuthenticationEntryPoint();

	private final ObjectMapper mapper;

	public CustomAuthenticationEntryPoint(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
	                     AuthenticationException authException) throws IOException, ServletException {
		this.delegate.commence(request, response, authException);

		response.setContentType("application/json;charset=UTF-8");

		RestResponse<Object> res = new RestResponse<>();
		res.setStatusCode(HttpStatus.UNAUTHORIZED.value());
		res.setError(authException.getMessage());
		res.setMessage("Token không hợp lệ (hết hạn, không đúng định dạng, hoặc không đúng)");

		mapper.writeValue(response.getWriter(), res);
	}

}
