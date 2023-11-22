package com.jsti.tunnel.monitor.util;

import com.jsti.tunnel.monitor.bean.Role;
import com.jsti.tunnel.monitor.bean.User;

public class AuthorUtil {
	
	public static boolean hasRole(User user, String role) {
		for(Role r:user.getRoles()) {
			if(role.equals(r.getName())){
				return true;
			}
		}
		return false;
	}
	
	public static boolean containsRole(User user, String role) {
		for(Role r:user.getRoles()) {
			if(r.getName().contains(role)){
				return true;
			}
		}
		return false;
	}
}
