package it.macros.security.utils;

import java.io.*;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.macros.security.beans.Esito;
import it.macros.security.beans.responses.GenericResponse;
import it.macros.security.constants.SecurityMessages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("serial")
public class JsonUtil implements Serializable {

	private static ObjectMapper mapper;

	@Autowired
	public void setTest(ObjectMapper mapper) {
		JsonUtil.mapper = mapper;
	}

	/**
	 * @param object
	 * @return String
	 */
	public static String jsonString(Object object) {

		String jsonString = null;

		try {
			jsonString = mapper.writeValueAsString(object);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonString;
	}

	/**
	 * @param message
	 * @param response
	 */
	public static void printMessage(SecurityMessages message, HttpServletResponse response) {

		response.setContentType("application/json");

		try {
			GenericResponse genericResponse = new GenericResponse();

			genericResponse.setEsito(new Esito(message.getCode(), message.name()));

			mapper.writeValue(response.getWriter(), genericResponse);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}