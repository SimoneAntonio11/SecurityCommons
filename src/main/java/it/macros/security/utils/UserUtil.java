package it.macros.security.utils;

import java.io.Serializable;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@SuppressWarnings("serial")
public class UserUtil implements Serializable {

	public String getCurrentUserName() {

		String username = null;

		try {

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

			username = authentication.getName();

		} catch(Exception e) {
			log.error("Could not load username from security context. The cause is: " + e.getMessage());
		}

		return username;
	}
}