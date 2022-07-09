/*
 * File: CORSFilter.java
 * This class is created to handle all processing involved
 * in a PT. MMI Research.
 *
 * Copyright (c) 2015 Mitra Mandiri Informatika
 * Jl. Tebet Raya no. 11 B Jakarta Selatan
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary
 * information of Mitra Mandiri Informatika ("Confidential
 * Information").
 *
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the
 * license agreement you entered into with MMI.
 *
 * Date Author Version Changes
 * Apr 22, 2015	Agus Rochmad TR<mamat@mmi-pt.com> 		1.0 	Created
 */

/**
 * 
 */
package id.co.promise.procurement.utils;

import id.co.promise.procurement.security.RoleUserSession;
import id.co.promise.procurement.security.TokenSession;
import id.co.promise.procurement.security.UserSession;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;

public class CORSFilter implements Filter {

	final static Logger logger = Logger.getLogger(CORSFilter.class);
	final static String ERROR_MSG_LOGIN_REQUIRED = "PRMS-ERR-001: Login required!";
	final static String ERROR_MSG_INSUFFICIENT_PRIVILEGE = "PRMS-ERR-002: insufficient privilege!";
	
	@EJB
	RoleUserSession roleUserSession;
	
	@EJB
	TokenSession tokenSession;
	
	@EJB
	private UserSession userSession;
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletResponse httpResp = (HttpServletResponse) resp;
		HttpServletRequest httpReq = (HttpServletRequest) req;
		Cookie[] cookieList = httpReq.getCookies();
		httpResp.setHeader("Access-Control-Allow-Origin", "*");
		httpResp.setHeader("Access-Control-Allow-Methods", "OPTIONS,GET, POST");
		if (httpReq.getMethod().equalsIgnoreCase("OPTIONS")) {
			httpResp.setHeader("Access-Control-Allow-Headers", httpReq.getHeader("Access-Control-Request-Headers"));
		}
	
		String path = httpReq.getRequestURI();
		String token = httpReq.getHeader("Authorization");
		
		String rejectMsg = SecurityHelper.checkAccess(path.replace(httpReq.getContextPath(),""), token, roleUserSession, tokenSession);
		
	/*	logger.info("==> CORS FILTER, Method: " + httpReq.getMethod() +", TOKEN: " + token + ", Path: " + path + " Security Msg: " + (rejectMsg==null?"SUCCESS!": rejectMsg));
	*/	
		// untuk membypass security check, set rejectMsg = null;
		//rejectMsg = null;
		boolean isReject = false;
		if (rejectMsg == null) {
			if(cookieList != null) {
				for(Cookie cookie : cookieList) {
					if(!cookie.getName().equals("JSESSIONID") && cookie.getValue().equalsIgnoreCase("")) {
						isReject = true;
					}
				}				
			}
			if(isReject) {
				/* clean cookies */
				for (Cookie cookie : cookieList) {
					cookie.setValue(null);
					cookie.setMaxAge(0);
					cookie.setPath("/");
					httpResp.addCookie(cookie);
				}
				Cookie forumCookie = new Cookie("JSESSIONID", "");
				forumCookie.setValue(null);
				forumCookie.setMaxAge(0);
				forumCookie.setPath("/forum");
				httpResp.addCookie(forumCookie);
				httpResp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				httpResp.getWriter().println("<html><body><p>Error Page</p></body></html>");
				userSession.getLogOut(token);
			}else {
				chain.doFilter(httpReq, httpResp);				
			}
		} else {
			// SC_UNAUTHORIZED = 401
			httpResp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			httpResp.getWriter().println("<html><body><p>" + rejectMsg + "</p></body></html>");
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

	@Override
	public void destroy() {
	}

}
