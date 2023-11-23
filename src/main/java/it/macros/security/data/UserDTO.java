package it.macros.security.data;

import java.util.*;

import lombok.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@NoArgsConstructor
@Data
@AllArgsConstructor
@SuppressWarnings("serial")
public class UserDTO implements UserDetails {

	private Integer id;
	private String username;
	private String email;
	private String password;
	private Boolean attivo;
	private boolean enabled;
	private Collection<? extends GrantedAuthority> authorities;
	private boolean accountNonExpired = true;
	private boolean accountNonLocked = true;
	private boolean credentialsNonExpired = true;

	/**
	 * @param username
	 * @param enabled
	 * @param authorities
	 */
	public UserDTO(String username, boolean enabled, Collection<? extends GrantedAuthority> authorities) {
		this.username = username;
		this.enabled = enabled;
		this.authorities = authorities;
	}

	/**
	 * @param id
	 * @param username
	 * @param password
	 * @param enabled
	 * @param authorities
	 */
	public UserDTO(Integer id, String username, String password, boolean enabled, Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.enabled = enabled;
		this.authorities = authorities;
	}
	
	public UserDTO(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public UserDTO(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public UserDTO(Integer id, String username, String email, String password, Boolean attivo) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.attivo = attivo;
	}
	
	
}