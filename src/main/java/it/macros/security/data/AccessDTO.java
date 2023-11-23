package it.macros.security.data;

import java.util.*;

import lombok.*;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class AccessDTO {

	private Integer id;
	private Integer userId;
	private String token;
	private Date accessDate;
	private Date logout;

	/**
	 * @param userId
	 * @param token
	 * @param accessDate
	 */
	public AccessDTO(Integer userId, String token, Date accessDate) {
		super();
		this.userId = userId;
		this.token = token;
		this.accessDate = accessDate;
	}
}