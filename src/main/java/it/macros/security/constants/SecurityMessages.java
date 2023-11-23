package it.macros.security.constants;

public enum SecurityMessages {

	ERRORE_GENERICO(-101),
	ERRORE_AUTENTICAZIONE(-102),
	ERRORE_AUTORIZZAZIONE(-103);

	private Integer code;

	public int getCode() {
		return code;
	}

	private SecurityMessages(Integer code) {
		this.code = code;
	}
}