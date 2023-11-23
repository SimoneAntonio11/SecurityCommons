package it.macros.security;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.commons.lang3.time.DateUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import it.macros.security.constants.SecurityMessages;
import it.macros.security.data.AccessDTO;
import it.macros.security.data.PrivilegesDTO;
import it.macros.security.data.UserDTO;
import it.macros.security.services.SecurityService;
import it.macros.security.services.exceptions.SecurityException;
import it.macros.security.services.exceptions.ServiceException;
import it.macros.security.utils.JsonUtil;
import it.macros.security.utils.TokenUtil;

public class SecurityFilter extends OncePerRequestFilter {

	@Value("${jwt.header}")
	private String tokenHeader;

	@Value("${jwt.scheme}")
	private String tokenScheme;

	@Value("${jwt.refreshing}")
	private Integer refreshing;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private TokenUtil tokenUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

		try {
			String token = getToken(request);

			UserDTO user = tokenUtil.getUserDetails(token);

			AccessDTO access = null;

			if(user != null) {
				access = securityService.getAccessByUsername(user.getUsername());
			}

			if(access == null || access.getLogout() != null || !access.getToken().equals(token)) {
				JsonUtil.printMessage(SecurityMessages.ERRORE_AUTENTICAZIONE, response);
				return;
			}

			if(!authorize(request, user.getUsername())) {
				JsonUtil.printMessage(SecurityMessages.ERRORE_AUTORIZZAZIONE, response);
				return;
			}

			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

			SecurityContextHolder.getContext().setAuthentication(authentication);

			/*
			 * if refreshing is greater than 0, the token is updated after "n" seconds from the creation date
			 * Otherwise, the token is updated in every request
			 */
			if(DateUtils.addSeconds(access.getAccessDate(), refreshing).before(new Date())) {
				token = refresh(token, access);
			}

			response.setHeader(tokenHeader, tokenScheme + token);

			chain.doFilter(request, response);

		} catch(ServiceException e) {
			JsonUtil.printMessage(SecurityMessages.ERRORE_GENERICO, response);
		}
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException
	{
		Collection<String> excludeUrl = new ArrayList<String>();

		excludeUrl.add("/public/**");

		return excludeUrl.stream().anyMatch(p -> new AntPathMatcher().match(p, request.getServletPath()));
	}

	/**
	 * @param request
	 * @return String
	 */
	private String getToken(HttpServletRequest request) {

		String token = null;

		String currentToken = request.getHeader(tokenHeader);

		if(currentToken != null && currentToken.startsWith(tokenScheme)) {
			token = currentToken.replace(tokenScheme, "");
		}

		return token;
	}

	/**
	 * @param request
	 * @param username
	 * @return Boolean
	 * @throws SecurityException
	 */
	private Boolean authorize(HttpServletRequest request, String username) throws ServiceException {

	
		if(request.getServletPath().startsWith("/menu") || 
			request.getServletPath().equals("/profilo-anagrafica")) { 
			return true;
		}

		List<PrivilegesDTO> privileges = securityService.getPrivilegesByUsername(username);

		for(PrivilegesDTO privilege : privileges) {
			if(request.getServletPath().matches(privilege.getPath()) && request.getMethod().equals(privilege.getMethod())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @param token
	 * @param access
	 * @return String
	 * @throws ServiceException
	 */
	private String refresh(String token, AccessDTO access) throws ServiceException {

		String refreshedToken = tokenUtil.refreshToken(token);

		access.setToken(refreshedToken);
		access.setAccessDate(tokenUtil.getCreatedDate(refreshedToken));
		access.setLogout(null);

		securityService.updateAccess(access);

		return refreshedToken;
	}
}