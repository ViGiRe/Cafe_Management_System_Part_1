package com.app.cafe.cafe_management_system.JWT;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class jwtFilter extends OncePerRequestFilter{
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private CustomerUserDetailService customerUserDetailService;

	Claims claims = null;
	private String userName = null;
	
	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain)
			throws ServletException, IOException {
		
		if(httpServletRequest.getServletPath().matches("/user/login|/user/signup|/user/forgotpassword")){
			filterChain.doFilter(httpServletRequest, httpServletResponse);
		}
		
		else {
			String authorizationHeader = httpServletRequest.getHeader("Authorization");
			String token = null;
			
			if(authorizationHeader !=null && authorizationHeader.startsWith("Bearer ")) {
				token = authorizationHeader.substring(7);
				userName = jwtUtil.extrctUserName(token);
				claims = jwtUtil.extractAllClaims(token);
			}
			if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = customerUserDetailService.loadUserByUsername(userName);
				if(jwtUtil.validateToken(token, userDetails)) {
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
							new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}
			}
			
			filterChain.doFilter(httpServletRequest, httpServletResponse);
		}
	}
	public Boolean isAdmin() {
		return "admin".equalsIgnoreCase((String)claims.get("role"));
	}
	
	public Boolean isUser() {
		return "user".equalsIgnoreCase((String)claims.get("role"));
	}
	
	public String getCurrentUser() {
		return userName;
	}
}
