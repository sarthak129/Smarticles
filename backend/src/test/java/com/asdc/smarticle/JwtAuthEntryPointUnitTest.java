package com.asdc.smarticle;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.asdc.smarticle.security.JwtAuthEntryPoint;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class JwtAuthEntryPointUnitTest {

	@Autowired
	JwtAuthEntryPoint authEntryPoint;

	@MockBean
	HttpServletRequest httpServletRequest;

	@MockBean
	HttpServletResponse httpServletResponse;

	@MockBean
	AuthenticationException authenticationException;

	@MockBean
	ServletOutputStream outputStream;

	@Test
	void testJwtAuthEntryPoint() throws StreamWriteException, DatabindException, IOException, ServletException {
		httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
		httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		final Map<String, Object> body = new HashMap<>();
		body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
		body.put("error", "Unauthorized");
		body.put("message", authenticationException.getMessage());
		body.put("path", httpServletRequest.getServletPath());

		Mockito.when(httpServletResponse.getOutputStream()).thenReturn(outputStream);

		final ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(httpServletResponse.getOutputStream(), body);

		authEntryPoint.commence(httpServletRequest, httpServletResponse, authenticationException);
	}

}
