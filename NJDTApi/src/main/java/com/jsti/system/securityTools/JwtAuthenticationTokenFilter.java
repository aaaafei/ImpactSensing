package com.jsti.system.securityTools;

import com.alibaba.fastjson.JSON;
import com.jsti.tunnel.monitor.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private RedisCacheUtil redisCacheUtil;
	

	@Value("${jwt.header}")
	private String tokenHeader;
	
	@Value("${jwt.expiration}")
	private int expiration;
	

	@Value("${jwt.tokenHead}")
	private String tokenHead;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		String authHeader = request.getHeader(this.tokenHeader);
		if (authHeader != null && authHeader.startsWith(tokenHead)) {
			final String authToken = authHeader.substring(tokenHead.length()); // The  part after "JSTI "
			String username = jwtTokenUtil.getUsernameFromToken(authToken);

			logger.info("checking authentication " + username);

			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

				UserDetails u = redisCacheUtil.getUserByUserName(username);
				// 验证是否跟缓存中有无
				if (u != null) {
					UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

					User u_t = (User) JSON.parseObject(JSON.toJSONString(u),User.class) ; 
	                //判定token中的用户名是否数据库中一致 是与缓存中的用户token一致
					if (jwtTokenUtil.validateToken(authToken, userDetails)&& authToken.equals(u_t.getToken())) {
						((User)userDetails).setToken(u_t.getToken());
						redisCacheUtil.setUser(username, userDetails, expiration);
						UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
								userDetails, null, userDetails.getAuthorities());
						authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						logger.info("authenticated user " + username + ", setting security context");
						SecurityContextHolder.getContext().setAuthentication(authentication);
					}
				}
			}
		}

		chain.doFilter(request, response);
	}
}