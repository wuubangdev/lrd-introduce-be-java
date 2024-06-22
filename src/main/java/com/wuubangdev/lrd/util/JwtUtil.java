package com.wuubangdev.lrd.util;

import com.nimbusds.jose.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;

@Service
public class JwtUtil {
	public static MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;

	@Value("${jwt.base64-secret}")
	private String jwtKey;

	@Value("${jwt.access-token-validity-in-second}")
	private long jwtKeyAccessExpiration;

	@Value("${jwt.refresh-token-validity-in-second}")
	private long jwtKeyRefreshExpiration;

	public SecretKeySpec getSecretKey() {
		byte[] keyBytes = Base64.from(jwtKey).decode();
		return new SecretKeySpec(keyBytes, 0, keyBytes.length, JWT_ALGORITHM.getName());
	}

	public long getAccessExpiration() {
		return this.jwtKeyAccessExpiration;
	}

	public long getRefreshExpiration() {
		return this.jwtKeyRefreshExpiration;
	}
}
