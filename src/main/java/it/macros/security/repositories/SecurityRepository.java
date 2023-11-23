package it.macros.security.repositories;

import java.util.*;

import it.macros.security.data.AccessDTO;
import it.macros.security.data.PrivilegesDTO;
import it.macros.security.data.ProfileDTO;
import it.macros.security.data.UserDTO;

public interface SecurityRepository {
	
	//--Access--
	public AccessDTO findAccessByUsername(String username);	
	public void insertAccess(AccessDTO access);
	public void updateAccess(AccessDTO access);
	public void deleteAccess(AccessDTO access);
	//--User--
	public UserDTO findUserByUsername(String username);
	public UserDTO findUserByEmail(String username);	
	public void insertUser(UserDTO user);
	//--Privileges--	
	public List<PrivilegesDTO> findPrivilegesByUsername(String username);
	//--Profiles--
	void insertProfile(ProfileDTO profile);
}