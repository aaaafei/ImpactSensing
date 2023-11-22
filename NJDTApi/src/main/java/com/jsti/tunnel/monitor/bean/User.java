package com.jsti.tunnel.monitor.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 文件名：User .
 */
@Data
public class User implements UserDetails {
	private Integer id;
	// 登录名
	private String username;
	// 姓名
	private String truename;
	// 密码
	private String password;

	// 组织机构ID
	private Integer organid;

	private Integer parent_organid;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date delete_time;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date invalidtime;

	
	// 组织机构ID
	private String organname;
	// 班次
	private Integer classid;
	
	// 班组
	private Integer teamid;
	// 电话号码
	private String phone;
	// 状态（-1-删除;1未删除）
	private int status;
	// 手机号
	private String mobile;
	// 短号
	private String cornet;
	// 手机号是否为外地 0：不是 1：是
	private String mobileaddress;
	// 座机号是否为外地 0：不是 1：是
	private String phoneadderss;
	// 工号
//	private String worknumber;
	// 工号+密码组合后的值
	private String workidpassword;
	// 是否内部人员(1-是 2-否)
	private String isinner;
	// 是否是经理
	private String ismanage;
	// 宅电
	private String homephone;
	// 宅电是否为外地 0：不是 1：是
	private String homephoneadderss;
	// 手机号2
	private String mobile2;
	// 手机号2是否为外地 0：不是 1：是
	private String ismobileaddress;
	// 生日
	private Date birth;
	// 年龄
	private Integer age;
	// 职位ID
	private Integer positionid;
	// 职位ID
	private String position_name;
	// 性别
	private String sex;
	// 工号
	private String workno;
	// 身份证号
	private String idno;

	private String token;
	private String email;
	private String pic;
	
	private String fee_station_name ;
	
	//道管中心ID
	private Integer road_manager_id;
	
	private List<String> roleids;

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	private List<Role> roles;
	private List<? extends GrantedAuthority> authorities;

	private List<Integer> position_ids;
	

	public User() {
       super();
	}

	public User(int id, String truename) {
		super();
		this.truename = truename;
		this.id = id;
	}
	
	public User(String username, String password, List<GrantedAuthority> grantedAuthorities) {
		super();
		this.username = username;
		this.password = password;
		this.authorities = grantedAuthorities;
	}

	@JsonIgnore
	public Integer getId() {
		return id;
	}


//	@JsonIgnore
//	@Override
//	public String getPassword() {
//		return password;
//	}

	@Override
	public String getUsername() {
		return username;
	}

	 // 账户是否未过期
    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 账户是否未锁定
    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    
    // 密码是否未过期
    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 账户是否激活
    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

	// 返回分配给用户的角色列表
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	public void setGrantedAuthorities(List<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}
	

}
