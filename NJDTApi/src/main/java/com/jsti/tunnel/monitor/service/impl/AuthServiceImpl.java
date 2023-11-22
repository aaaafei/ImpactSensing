package com.jsti.tunnel.monitor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.jsti.system.securityTools.JwtTokenUtil;
import com.jsti.system.securityTools.RedisCacheUtil;
import com.jsti.tunnel.monitor.bean.User;
import com.jsti.tunnel.monitor.service.AuthService;
import com.jsti.tunnel.monitor.service.BaseService;

@Service
public class AuthServiceImpl extends BaseService implements AuthService {


	    private AuthenticationManager authenticationManager;
	    private UserDetailsService userDetailsService;
	    private JwtTokenUtil jwtTokenUtil;
	    
	    
		@Value("${jwt.expiration}")
		private int expiration;
		

		@Autowired
		private RedisCacheUtil redisCacheUtil;
	    

	    @Value("${jwt.tokenHead}")
	    private String tokenHead;

	    @Autowired
	    public AuthServiceImpl(
	            AuthenticationManager authenticationManager,
	            UserDetailsService userDetailsService,
	            JwtTokenUtil jwtTokenUtil) {
	        this.authenticationManager = authenticationManager;
	        this.userDetailsService = userDetailsService;
	        this.jwtTokenUtil = jwtTokenUtil;
	    }
	  

	    @Override
	    public User login(String username, String password) {
	        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
	        final Authentication authentication = authenticationManager.authenticate(upToken);
	        SecurityContextHolder.getContext().setAuthentication(authentication);
	        User userDetails =(User) userDetailsService.loadUserByUsername(username);
	        
	        final String token = jwtTokenUtil.generateToken(userDetails);
	        userDetails.setToken(token);
	        
	        redisCacheUtil.setUser(userDetails.getUsername(), userDetails, expiration);
	       
	        
	        return userDetails;
	    }

	    @Override
	    public String refresh(String oldToken) {
	        final String token = oldToken.substring(tokenHead.length());
	        String username = jwtTokenUtil.getUsernameFromToken(token);
	        User user = (User) userDetailsService.loadUserByUsername(username);
	        if (jwtTokenUtil.canTokenBeRefreshed(token, null)){
	            return jwtTokenUtil.refreshToken(token);
	        }
	        return null;
	    }
	

}
