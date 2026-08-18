package com.epiis.app;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.epiis.app.auxobject.JwtProperties;
import com.epiis.app.dto.DtoUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	

	public String generateToken(DtoUser dtoUser) {
	    return Jwts.builder()
	        .setSubject(dtoUser.getIdUser().toString()) // 👈 USAR ID
	        .setIssuedAt(new Date())
	        .setExpiration(new Date(System.currentTimeMillis() + JwtProperties.timeAuthMs))
	        .signWith(getSignInKey(), SignatureAlgorithm.HS256)
	        .compact();
	}

	public boolean isTokenValid(String token, String userId) {
	    return userId.equals(extractUsername(token)) && !isTokenExpired(token);
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);

		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts
			.parserBuilder()
			.setSigningKey(getSignInKey())
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

	

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	private Key getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(JwtProperties.secretKey);

		return Keys.hmacShaKeyFor(keyBytes);
	}
}