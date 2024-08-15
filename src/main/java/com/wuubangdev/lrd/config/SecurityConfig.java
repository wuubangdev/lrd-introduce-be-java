package com.wuubangdev.lrd.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.wuubangdev.lrd.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
	private final JwtUtil jwtUtil;

	public SecurityConfig(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(
			HttpSecurity http,
			CustomAuthenticationEntryPoint customAuthenticationEntryPoint)
			throws Exception {
		String[] whiteList = { "/", "/storage/**",
				"/api/v1/auth/login", "/api/v1/auth/refresh", "/api/v1/auth/register",
				// "/v3/api-docs/**",
				// "/swagger-ui/**",
				// "/swagger-ui.html",
		};
		http
				.csrf(AbstractHttpConfigurer::disable)
				.cors(Customizer.withDefaults())
				.authorizeHttpRequests(
						authorize -> authorize
								.requestMatchers(whiteList)
								.permitAll()
								.requestMatchers(HttpMethod.GET, "/api/v1/staffs")
								.permitAll()
								.anyRequest().authenticated())
				.oauth2ResourceServer(oauth2 -> oauth2
						.jwt(Customizer.withDefaults())
						.authenticationEntryPoint(customAuthenticationEntryPoint))
				.formLogin(AbstractHttpConfigurer::disable)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		return http.build();
	}

	@Bean
	public JwtEncoder jwtEncoder() {
		return new NimbusJwtEncoder(new ImmutableSecret<>(this.jwtUtil.getSecretKey()));
	}

	@Bean
	public JwtDecoder jwtDecoder() {
		NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(this.jwtUtil.getSecretKey())
				.macAlgorithm(JwtUtil.JWT_ALGORITHM).build();
		return token -> {
			try {
				return jwtDecoder.decode(token);
			} catch (Exception e) {
				System.out.println(">>> Jwt erorr:" + e.getMessage());
				throw e;
			}
		};
	}

	@Bean
	public JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
		grantedAuthoritiesConverter.setAuthorityPrefix("");
		grantedAuthoritiesConverter.setAuthoritiesClaimName("permission");
		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
		return jwtAuthenticationConverter;
	}
}
