package it.macros.security.repositories.impl;

public interface SqlStrings {
	
	public static final String USER_BY_EMAIL =			
	"SELECT new it.macros.security.data.UserDTO(a.id, a.username, a.email, a.password, a.attivo) FROM Utente a WHERE a.email = :email";
	
	public static final String USER_BY_USERNAME =
		"SELECT a.id, a.username, a.password, IF(a.attivo IS TRUE AND b.cancellato IS FALSE, '1', '0') abilitato, d.nome FROM utenti a "
		+ "INNER JOIN anagrafica b on b.id_utente = a.id "
		+ "INNER JOIN profili c on c.id_utente = a.id "
		+ "INNER JOIN ruoli d on d.id = c.id_ruolo "
		+ "WHERE c.data_inizio <= NOW() AND (c.data_fine >= NOW() OR c.data_fine IS NULL) "
		+ "AND a.username = :username";

	public static final String ACCESS_BY_USERNAME =
		"SELECT a.id, a.id_utente, a.token, a.data_accesso, a.logout FROM accessi a "
		+ "INNER JOIN utenti b on b.id = a.id_utente "
		+ "INNER JOIN anagrafica c on c.id_utente = b.id "
		+ "WHERE b.attivo IS TRUE "
		+ "AND c.cancellato IS FALSE "
		+ "AND b.username = :username";

	public static final String PRIVILEGES_BY_USERNAME =
		"SELECT a.percorso, a.metodo FROM operazioni a "
		+ "INNER JOIN funzioni b on b.id = a.id_funzione "
		+ "INNER JOIN privilegi c on c.id_funzione = b.id "
		+ "INNER JOIN ruoli d on d.id = c.id_ruolo "
		+ "INNER JOIN profili e on e.id_ruolo = d.id "
		+ "INNER JOIN utenti f on f.id = e.id_utente "
		+ "INNER JOIN anagrafica g on g.id_utente = f.id "
		+ "WHERE e.data_inizio <= NOW() AND (e.data_fine >= NOW() OR e.data_fine IS NULL) "
		+ "AND f.attivo IS TRUE "
		+ "AND g.cancellato IS FALSE "
		+ "AND f.username = :username";
	
	public static final String INSERT_NEW_USER =
			"INSERT INTO utenti(username, email, password, attivo) "
			+ "VALUES (:username, :email, :password, :attivo)";
	
	public static final String INSERT_PROFILE =
			"INSERT INTO profili(id_ruolo, id_utente, data_inizio) "
			+ "VALUES (:roleId, :userId, :startDate)";

	public static final String INSERT_ACCESS =
		"INSERT INTO accessi(id_utente, token, data_accesso) "
		+ "VALUES (:userId, :token, :accessData)";

	public static final String UPDATE_ACCESS =
		"UPDATE accessi a SET "
		+ "a.id_utente = :userId, a.token = :token, a.data_accesso = :accessData, a.logout = :logout "
		+ "WHERE a.id = :id";

	public static final String DELETE_ACCESS =
		"UPDATE accessi a SET a.logout = :logout WHERE a.id = :id";
}