package com.jsti.system.securityTools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.jsti.tunnel.monitor.service.UserService;

/**
 * 文件名：WebSecurityConfig
 * 版本信息：日期：2017/3/31 .
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled=true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationTokenFilter();
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());//user Details Service验证
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
        web.ignoring().antMatchers("/css/**","/js/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	
    	http
         // 由于使用的是JWT，我们这里不需要csrf
         .csrf().disable()
         .cors().and()
         // 基于token，所以不需要session
         .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
         .authorizeRequests()
         //.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
         // 允许对于网站静态资源的无授权访问
         .antMatchers(
                 HttpMethod.GET,
                 "/test/*",
                 "/*.html",
                 "/favicon.ico",
                 "/**/*.html",
                 "/**/*.css",
                 "/**/*.js"
         ).permitAll()
         // 对于获取token的rest api要允许匿名访问
         .antMatchers("/user/auth/**","/app/index/**","/file/**","/druid/**","/healCalculateTriger/**").permitAll()
         // 除上面外的所有请求全部需要鉴权认证
         .anyRequest().authenticated();
    	
    	// 禁用缓存
        http.headers().cacheControl();
        
        http.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

    }

    /**
     * 设置用户密码的加密方式为MD5加密
     * @return
     */
    @Bean
    public Md5PasswordEncoder passwordEncoder() {
        return new Md5PasswordEncoder();
    }
    
  
}