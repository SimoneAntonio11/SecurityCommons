package it.macros.security.data;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrivilegesDTO {

	private String path;
	private String method;
}