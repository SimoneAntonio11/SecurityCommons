package it.macros.security.services.impl;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.macros.security.data.UserDTO;
import it.macros.security.repositories.SecurityRepository;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private SecurityRepository securityRepository;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		UserDTO user = null;

		try {
			user = securityRepository.findUserByUsername(username);

			if(user == null) {
				throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
			}

		} catch(Exception e) {
			log.error("Exception occurs {}", e.getMessage());
			throw e;
		}

		return user;
	}
}