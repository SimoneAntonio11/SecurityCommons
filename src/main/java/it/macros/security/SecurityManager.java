package it.macros.security;

import java.io.*;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import it.macros.security.constants.SecurityMessages;
import it.macros.security.data.AccessDTO;
import it.macros.security.data.ProfileDTO;
import it.macros.security.data.UserDTO;
import it.macros.security.services.SecurityService;
import it.macros.security.services.exceptions.SecurityException;
import it.macros.security.services.exceptions.ServiceException;
import it.macros.security.services.exceptions.ServiceValidationException;
import it.macros.security.utils.TokenUtil;
import it.macros.security.utils.UserUtil;

@Component
@SuppressWarnings("serial")
public class SecurityManager implements Serializable {

	@Value("${jwt.header}")
	private String tokenHeader;

	@Value("${jwt.scheme}")
	private String tokenScheme;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private SecurityService securityService;
	
	@Autowired
	private PasswordEncoder encoder; //per codificare password manualmente

	@Autowired
	private TokenUtil tokenUtil;

	@Autowired
	private UserUtil userUtil;

	public String login(String username, String password, HttpServletResponse response) throws SecurityException {

		String token = null;

		try {
			 
			Authentication authentication =	authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

			UserDTO user = (UserDTO)authentication.getPrincipal();

			token = tokenUtil.generateToken(user);

			//Data di scadenza token
			//Date date = tokenUtil.getExpirationDate(token);

			AccessDTO access = securityService.getAccessByUsername(user.getUsername());

			/*
			 * if the user is already authorized, the new login overwrites the previous one and generating a new token
			 */
			if(access == null) {

				access = new AccessDTO(user.getId(), token, tokenUtil.getCreatedDate(token));

				securityService.insertAccess(access);

			} else {

				access.setToken(token);
				access.setAccessDate(tokenUtil.getCreatedDate(token));
				access.setLogout(null);

				securityService.updateAccess(access);
			}

			SecurityContextHolder.getContext().setAuthentication(authentication);

			response.setHeader(tokenHeader, tokenScheme + token);

		} catch(AuthenticationException e) {
			throw new SecurityException(SecurityMessages.ERRORE_AUTENTICAZIONE);
		} catch(ServiceException e) {
			throw new SecurityException(SecurityMessages.ERRORE_GENERICO);
		}

		return token;
	}
	
	public void registrati(String username, String email, String password, HttpServletResponse response) throws SecurityException {
        try {
          
            UserDTO user = new UserDTO(username, email, encoder.encode(password));
            securityService.insertUser(user);
            
            
            user = securityService.findUserByEmail(email);
            
            ProfileDTO profile = new ProfileDTO(2, user.getId(), new Date());
            securityService.insertProfile(profile);
            
            Authentication authentication =	authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

			String token = tokenUtil.generateToken(user);

			AccessDTO access = new AccessDTO(user.getId(), token, tokenUtil.getCreatedDate(token));
			securityService.insertAccess(access);

            SecurityContextHolder.getContext().setAuthentication(authentication);
            response.setHeader(tokenHeader, tokenScheme + token);

        } catch (ServiceException e) {
            throw new SecurityException(SecurityMessages.ERRORE_GENERICO);
        } catch (AuthenticationException e) {
            throw new SecurityException(SecurityMessages.ERRORE_AUTENTICAZIONE);
        }
    }

	public void logout(HttpServletRequest request, HttpServletResponse response) throws SecurityException {

		try {
			String token = request.getHeader(tokenHeader);

			if(token == null || !token.startsWith(tokenScheme)) {
				throw new ServiceValidationException(SecurityMessages.ERRORE_AUTENTICAZIONE);
			}

			token = token.replace(tokenScheme, "");

			/*
			 * if the token has expired, logout is not necessary because the user is already implicitly logged out
			 */
			if(!tokenUtil.isTokenExpired(token)) {

				String username = tokenUtil.getUsername(token);

				AccessDTO access = securityService.getAccessByUsername(username);

				/*
				 * Access is updated if necessary
				 */
				if(access != null && access.getLogout() == null) {

					access.setLogout(new Date());

					securityService.deleteAccess(access);
				}
			}

			response.setHeader(tokenHeader, null);

		} catch(ServiceException e) {
			throw new SecurityException(SecurityMessages.ERRORE_GENERICO);
		}
	}

	public Date verificaToken(HttpServletRequest request, HttpServletResponse response) throws SecurityException {

		String token = request.getHeader(tokenHeader);
		System.err.println(token);
		System.err.println(token);
		if(token == null || !token.startsWith(tokenScheme)) {
			throw new ServiceValidationException(SecurityMessages.ERRORE_AUTENTICAZIONE);
		}

		//tokenUtil.isTokenExpired(token);
		Date date = tokenUtil.getExpirationDate(token);
		System.err.println(date);
		System.err.println(date);
		System.err.println(date);

		return date;

	}

	public AccessDTO getAccess(String username) throws SecurityException {

		AccessDTO access = new AccessDTO();
		access = securityService.getAccessByUsername(username);
		return access;
	}

	public UserDTO getUser(String username) throws SecurityException {

		UserDTO user = new UserDTO();
		user = securityService.findUserByUsername(username);
		return user;
	}

	
    

	//metodo per ottenere l'username corrente dell'utente loggato tramite il token
	public String getUsername() throws SecurityException {

		String username = userUtil.getCurrentUserName();

		return username;
	}
}