package com.asdc.smarticle.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class is used to commence an authentication scheme
 * @author Sarthak Patel
 * @version 1.0
 * @since 2022-02-22
 */

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint{

	/**
	 * This class is used to commence an authentication scheme
	 * @param request is the instance of HttpServletRequest provides with servelet request information
	 * @param response is the instance of HttpServletResponse provides the response from the servelet
	 * @param authException is the instance of AuthenticationException throws the exception occur while authenticating the applicaton
	 */
	@Override
	  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
	      throws IOException, ServletException {

	    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
	    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

	    final Map<String, Object> body = new HashMap<>();
	    body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
	    body.put("error", "Unauthorized");
	    body.put("message", authException.getMessage());
	    body.put("path", request.getServletPath());

	    final ObjectMapper mapper = new ObjectMapper();
	    mapper.writeValue(response.getOutputStream(), body);
	  }
}
