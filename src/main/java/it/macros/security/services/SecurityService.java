package it.macros.security.services;

import java.util.*;

import it.macros.security.data.AccessDTO;
import it.macros.security.data.PrivilegesDTO;
import it.macros.security.data.ProfileDTO;
import it.macros.security.data.UserDTO;
import it.macros.security.services.exceptions.ServiceException;

public interface SecurityService {
	
	//--ACCESS--
	public AccessDTO getAccessByUsername(String username) throws ServiceException;
	public void insertAccess(AccessDTO access) throws ServiceException;
	public void updateAccess(AccessDTO access) throws ServiceException;
	public void deleteAccess(AccessDTO access) throws ServiceException;
	//--USER--
	public UserDTO findUserByEmail(String email) throws ServiceException;
	public UserDTO findUserByUsername(String username) throws ServiceException;
	public void insertUser(UserDTO user) throws ServiceException;
	//--PRIVILEGES--
	public List<PrivilegesDTO> getPrivilegesByUsername(String username) throws ServiceException;
	//--PROFILES--
	void insertProfile(ProfileDTO profile) throws ServiceException;
	
	
	
}