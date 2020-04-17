package com.chengxu.common.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;


/**
 * @ClassName SecurityConfig
 * @Description 安全核心配置
 * @Author ruanchanglong
 * @Date 2020/4/117:19
 * @Version 1.0
 **/

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {

	/*	http.authorizeRequests().antMatchers("/", "/home").permitAll()
				//指定任何经过身份验证的用户都允许使用URL
				.anyRequest().authenticated()
				.and()
		//使用OAuth 2.0和/或OpenID Connect 1.0提供程序配置身份验证支持。
				//.oauth2Login()
				//.openidLogin()
				//表单验证登录
				.formLogin()
				.loginPage("/login")
				.permitAll()
				.and()
				//注销
				.logout()
				.permitAll();*/
		//不拦截任何请求
		http.authorizeRequests().antMatchers("/*").permitAll();
		// 所有的rest服务一定要设置为无状态，以提升操作效率和性能
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	/**
	 * 它将单个用户设置在内存中。该用户的用户名为“user”，密码为“password”，角色为“USER”。
	 * @param auth
	 * @throws Exception
	 */
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

		auth.inMemoryAuthentication()
				.withUser("user").password("password").roles("USER");
	}

}
