package it.macros.security.beans.responses;

import it.macros.security.beans.Esito;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class GenericResponse {
	private Esito esito = new Esito();
}