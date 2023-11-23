package it.macros.security.repositories.impl;

import java.util.*;

import javax.persistence.Query;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

import it.macros.security.data.AccessDTO;
import it.macros.security.data.PrivilegesDTO;
import it.macros.security.data.ProfileDTO;
import it.macros.security.data.UserDTO;
import it.macros.security.repositories.SecurityRepository;

@Repository
@SuppressWarnings("unchecked")
public class SecurityRepositoryImpl extends BaseRepositoryImpl implements SecurityRepository {

	/*----ACCESS----*/

	@Override
	public AccessDTO findAccessByUsername(String username) {

		AccessDTO access = null;

		Query query = entityManager.createNativeQuery(SqlStrings.ACCESS_BY_USERNAME);

		query.setParameter("username", username);

		List<Object[]> list = query.getResultList();

		if(!list.isEmpty()) {

			Object[] accesso = list.get(0);

			access = new AccessDTO(
				(Integer) accesso[0],
				(Integer) accesso[1],
				(String)  accesso[2],
				(Date)    accesso[3],
				(Date)    accesso[4]);
		}

		return access;
	}
	
	@Override
	public void insertAccess(AccessDTO access) {

		Query query = entityManager.createNativeQuery(SqlStrings.INSERT_ACCESS);

		query.setParameter("userId", access.getUserId());
		query.setParameter("token", access.getToken());
		query.setParameter("accessData", access.getAccessDate());

		query.executeUpdate();
	}

	@Override
	public void updateAccess(AccessDTO access) {

		Query query = entityManager.createNativeQuery(SqlStrings.UPDATE_ACCESS);

		query.setParameter("userId", access.getUserId());
		query.setParameter("token", access.getToken());
		query.setParameter("accessData", access.getAccessDate());
		query.setParameter("logout", access.getLogout());
		query.setParameter("id", access.getId());

		query.executeUpdate();
	}

	@Override
	public void deleteAccess(AccessDTO access) {

		Query query = entityManager.createNativeQuery(SqlStrings.DELETE_ACCESS);

		query.setParameter("logout", access.getLogout());
		query.setParameter("id", access.getId());

		query.executeUpdate();
	}

	/*----USER----*/
	
	@Override
	public UserDTO findUserByEmail(String email) {

	    UserDTO user = null;

	    Query query = entityManager.createNativeQuery(SqlStrings.USER_BY_EMAIL, UserDTO.class); 

	    query.setParameter("email", email);
	    
	    user = (UserDTO) query.getSingleResult();
	        
	    return user;
	}

	
	
	@Override
	public UserDTO findUserByUsername(String username) {

		UserDTO user = null;

		Query query = entityManager.createNativeQuery(SqlStrings.USER_BY_USERNAME);

		query.setParameter("username", username);

		List<Object[]> list = query.getResultList();

		if(!list.isEmpty()) {

			Object[] anagrafica = list.get(0);

			Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();

			for (Object[] item : list) {
				authorities.add(new SimpleGrantedAuthority((String)item[4]));
			}

			user = new UserDTO(
				(Integer) anagrafica[0],
				(String)  anagrafica[1],
				(String)  anagrafica[2],
				String.valueOf(anagrafica[3]).equals("1") ? true : false,
				authorities);
		}

		return user;
	}
	
	@Override
	public void insertUser(UserDTO user) {

		Query query = entityManager.createNativeQuery(SqlStrings.INSERT_NEW_USER);

		query.setParameter("username", user.getUsername());
		query.setParameter("email", user.getEmail());
		query.setParameter("password", user.getPassword());
		query.setParameter("attivo", true);

		query.executeUpdate();
	}
	
	/*----PROFILES----*/
	@Override
	public void insertProfile(ProfileDTO profile) {

		Query query = entityManager.createNativeQuery(SqlStrings.INSERT_PROFILE);

		query.setParameter("roleId", profile.getRoleId());
		query.setParameter("userId", profile.getUserId());
		query.setParameter("startDate", profile.getStartDate());
	

		query.executeUpdate();
	}
	
	/*----PRIVILEGES----*/
	
	@Override
	public List<PrivilegesDTO> findPrivilegesByUsername(String username) {

		List<PrivilegesDTO> privileges = null;

		Query query = entityManager.createNativeQuery(SqlStrings.PRIVILEGES_BY_USERNAME);

		query.setParameter("username", username);

		List<Object[]> list = query.getResultList();

		if(!list.isEmpty()) {

			privileges = new ArrayList<PrivilegesDTO>();

			for (Object[] item : list) {
				privileges.add(new PrivilegesDTO((String)item[0], (String)item[1]));
			}
		}

		return privileges;
	}
	


	
}