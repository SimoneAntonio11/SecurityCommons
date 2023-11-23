package it.macros.security.repositories.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "utenti")
public class Utente {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "username")
	private String username;
	
	@Column(name = "email")
	private String email;

	@Column(name = "password")
	private String password;

	@Column(name = "token_password")
	private String token;

	@Column(name = "attivo")
	private Boolean attivo;
	
	public Utente(String username, String password) {
		this.username = username;
		this.password = password;
	}

}
