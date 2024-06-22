package com.wuubangdev.lrd.util;

import com.wuubangdev.lrd.domain.user.ResUserToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class SecurityUtil {
	private final JwtEncoder jwtEncoder;
	private final JwtUtil jwtUtil;

	public SecurityUtil(JwtEncoder jwtEncoder, JwtUtil jwtUtil) {
		this.jwtEncoder = jwtEncoder;
		this.jwtUtil = jwtUtil;
	}

	public String createAccessToken(String email, ResUserToken resUserToken) {

		ResUserToken.UserInsideLogin userToken = new ResUserToken.UserInsideLogin();
		userToken.setId(resUserToken.getUserLogin().getId());
		userToken.setEmail(resUserToken.getUserLogin().getEmail());
		userToken.setName(resUserToken.getUserLogin().getName());

		Instant now = Instant.now();
		Instant validity = now.plus(this.jwtUtil.getAccessExpiration(), ChronoUnit.SECONDS);

		JwtClaimsSet claims = JwtClaimsSet.builder()
				.issuedAt(now)
				.expiresAt(validity)
				.subject(email)
				.claim("user", userToken)
				.build();
		JwsHeader jwsHeader = JwsHeader.with(JwtUtil.JWT_ALGORITHM).build();
		return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
	}

	public String createRefreshToken(String email, ResUserToken resUserToken) {
		ResUserToken.UserInsideLogin userToken = new ResUserToken.UserInsideLogin();
		userToken.setId(resUserToken.getUserLogin().getId());
		userToken.setEmail(resUserToken.getUserLogin().getEmail());
		userToken.setName(resUserToken.getUserLogin().getName());
		Instant now = Instant.now();
		Instant validity = now.plus(this.jwtUtil.getRefreshExpiration(), ChronoUnit.SECONDS);

		JwtClaimsSet claims = JwtClaimsSet.builder()
				.issuedAt(now)
				.expiresAt(validity)
				.subject(email)
				.claim("user", userToken)
				.build();
		JwsHeader jwsHeader = JwsHeader.with(JwtUtil.JWT_ALGORITHM).build();
		return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
	}

	/**
	 * Get the login of the current user.
	 *
	 * @return the login of the current user.
	 */
	public static Optional<String> getCurrentUserLogin() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
	}

	private static String extractPrincipal(Authentication authentication) {
		if (authentication == null) {
			return null;
		} else if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
			return springSecurityUser.getUsername();
		} else if (authentication.getPrincipal() instanceof Jwt jwt) {
			return jwt.getSubject();
		} else if (authentication.getPrincipal() instanceof String s) {
			return s;
		}
		return null;
	}


	public Jwt checkValidRefreshToken(String token) {
		NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(this.jwtUtil.getSecretKey())
				.macAlgorithm(JwtUtil.JWT_ALGORITHM).build();
		try {
			return jwtDecoder.decode(token);
		} catch (Exception e) {
			System.out.println(">>> JWT error: " + e.getMessage());
			throw e;
		}
	}
}
