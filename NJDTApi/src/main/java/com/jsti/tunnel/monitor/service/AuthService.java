package com.jsti.tunnel.monitor.service;

import com.jsti.tunnel.monitor.bean.User;

public interface AuthService {
	 User login(String username, String password);
	 String refresh(String oldToken);
}
