package it.macros.security.data;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDTO {
	
	private Integer id;
	private Integer roleId;
	private Integer userId;
	private Date startDate;
	
	
	public ProfileDTO(Integer roleId, Integer userId, Date startDate) {
		super();
		this.roleId = roleId;
		this.userId = userId;
		this.startDate = startDate;
	}
	
	
}
