package it.macros.security.services.impl;

import java.util.*;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.macros.security.data.AccessDTO;
import it.macros.security.data.PrivilegesDTO;
import it.macros.security.data.ProfileDTO;
import it.macros.security.data.UserDTO;
import it.macros.security.repositories.SecurityRepository;
import it.macros.security.services.SecurityService;
import it.macros.security.services.exceptions.ServiceException;

@Slf4j
@Service
public class SecurityServiceImpl implements SecurityService {

	@Autowired
	private SecurityRepository securityRepository;
	
	/*----ACCESS----*/

	@Override
	@Transactional(readOnly = true)
	public AccessDTO getAccessByUsername(String username) throws ServiceException {

		AccessDTO access = null;

		try {
			access = securityRepository.findAccessByUsername(username);
		} catch(Exception e) {
			log.error("Exception occurs {}", e.getMessage());
			throw new ServiceException(e);
		}

		return access;
	}
	
	@Override
	@Transactional
	public void insertAccess(AccessDTO access) throws ServiceException {

		try {
			securityRepository.insertAccess(access);
		} catch(Exception e) {
			log.error("Exception occurs {}", e.getMessage());
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional
	public void updateAccess(AccessDTO access) throws ServiceException {

		try {
			securityRepository.updateAccess(access);
		} catch(Exception e) {
			log.error("Exception occurs {}", e.getMessage());
			throw new ServiceException(e);
		}
	}

	@Override
	@Transactional
	public void deleteAccess(AccessDTO access) throws ServiceException {

		try {
			securityRepository.deleteAccess(access);
		} catch(Exception e) {
			log.error("Exception occurs {}", e.getMessage());
			throw new ServiceException(e);
		}
	}
	
	/*----USER----*/
	
	@Override
	@Transactional(readOnly = true)
	public UserDTO findUserByEmail(String email) throws ServiceException {

		UserDTO user = null;

		try {
			
			user = securityRepository.findUserByEmail(email);
		} catch(Exception e) {
			log.error("Exception occurs {}", e.getMessage());
			throw new ServiceException(e);
		}

		return user;
	}
	
	@Override
	@Transactional(readOnly = true)
	public UserDTO findUserByUsername(String username) throws ServiceException {

		UserDTO user = null;

		try {
			user = securityRepository.findUserByUsername(username);
			
		} catch(Exception e) {
			log.error("Exception occurs {}", e.getMessage());
			throw new ServiceException(e);
		}

		return user;
	}
	
	@Override
	@Transactional
	public void insertUser(UserDTO user) throws ServiceException {

		try {
			securityRepository.insertUser(user);
		} catch(Exception e) {
			log.error("Exception occurs {}", e.getMessage());
			throw new ServiceException(e);
		}
	}
	
	/*----PROFILES----*/
	
	@Override
	@Transactional
	public void insertProfile(ProfileDTO profile) throws ServiceException {

		try {
			securityRepository.insertProfile(profile);
		} catch(Exception e) {
			log.error("Exception occurs {}", e.getMessage());
			throw new ServiceException(e);
		}
	}
	
	/*----PRIVILEGES----*/

	@Override
	@Transactional(readOnly = true)
	public List<PrivilegesDTO> getPrivilegesByUsername(String username) throws ServiceException {

		List<PrivilegesDTO> privileges = new ArrayList<PrivilegesDTO>();

		try {
			privileges = securityRepository.findPrivilegesByUsername(username);
		} catch(Exception e) {
			log.error("Exception occurs {}", e.getMessage());
			throw new ServiceException(e);
		}

		return privileges;
	}

	
}