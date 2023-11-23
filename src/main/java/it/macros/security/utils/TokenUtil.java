package it.macros.security.utils;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import io.jsonwebtoken.*;
import it.macros.security.data.UserDTO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@SuppressWarnings("serial")
public class TokenUtil implements Serializable {

	private static final String CLAIM_KEY_USERNAME = "sub";
	private static final String CLAIM_KEY_CREATED = "iat";
	private static final String CLAIM_KEY_AUTHORITIES = "authorities";
	private static final String CLAIM_KEY_ENABLED = "enabled";

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private Integer expiration;

	/**
	 * @param token
	 * @return UserDTO
	 */
	@SuppressWarnings("unchecked")
	public UserDTO getUserDetails(String token) {

		UserDTO userDTO = null;

		try {
			Claims claims = getClaims(token);

			List<SimpleGrantedAuthority> authorities = null;

			if (claims.get(CLAIM_KEY_AUTHORITIES) != null) {
				authorities = ((List<String>)claims.get(CLAIM_KEY_AUTHORITIES)).stream().map(role-> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
			}

			userDTO = new UserDTO(
				claims.getSubject(),
				(boolean) claims.get(CLAIM_KEY_ENABLED),
				authorities);

		} catch(Exception e) {
			log.error("Could not load user details from token. The cause is: " + e.getMessage());
		}

		return userDTO;
	}

	/**
	 * @param token
	 * @return String
	 */
	public String getUsername(String token) {

		String username = null;

		try {
			Claims claims = getClaims(token);

			username = (String)claims.get(CLAIM_KEY_USERNAME);

		} catch(Exception e) {
			log.error("Could not load username from token. The cause is: " + e.getMessage());
		}

		return username;
	}

	/**
	 * @param token
	 * @return Date
	 */
	public Date getCreatedDate(String token) {

		Date created = null;

		try {
			Claims claims = getClaims(token);

			created = new Date((Long) claims.get(CLAIM_KEY_CREATED));

		} catch(Exception e) {
			log.error("Could not load created date from token. The cause is: " + e.getMessage());
		}

		return created;
	}

	/**
	 * @param token
	 * @return Date
	 */
	public Date getExpirationDate(String token) {

		Date expiration = null;

		try {
			Claims claims = getClaims(token);

			expiration = claims.getExpiration();

		} catch(Exception e) {
			log.error("Could not load expiration date from token. The cause is: " + e.getMessage());
		}

		return expiration;
	}

	/**
	 * @param token
	 * @return Boolean
	 */
	public Boolean isTokenExpired(String token) {
		return getExpirationDate(token) == null ? true : false;
	}

	/**
	 * @param UserDTO
	 * @return String
	 */
	public String generateToken(UserDTO userDTO) {

		Map<String, Object> claims = new HashMap<>();

		claims.put(CLAIM_KEY_USERNAME, userDTO.getUsername());
		claims.put(CLAIM_KEY_CREATED, new Date());
		List<String> authorities = userDTO.getAuthorities().stream().map(role-> role.getAuthority()).collect(Collectors.toList());
		claims.put(CLAIM_KEY_AUTHORITIES, authorities);
		claims.put(CLAIM_KEY_ENABLED, userDTO.isEnabled());

		return generateToken(claims);
	}

	/**
	 * @param token
	 * @return String
	 */
	public String refreshToken(String token) {

		Claims claims = getClaims(token);

		claims.put(CLAIM_KEY_CREATED, new Date());

		return generateToken(claims);
	}
	/**
	 * @param token
	 * @return Claims
	 */
	private Claims getClaims(String token) {

		return Jwts.parser()
			.setSigningKey(secret)
			.parseClaimsJws(token)
			.getBody();
	}

	/**
	 * @param claims
	 * @return String
	 */
	private String generateToken(Map<String, Object> claims) {

		Date expirationDate = new Date(System.currentTimeMillis() + expiration * 1000);

		return Jwts.builder()
			.setClaims(claims)
			.setExpiration(expirationDate)
			.signWith(SignatureAlgorithm.HS256, secret)
			.compact();	}
}