package com.format_project.meeting.config;


import com.format_project.meeting.bean.MyCorsFilter;
import com.format_project.meeting.bean.JWTToken.JwtTokenFilter;
import com.format_project.meeting.bean.LoginAuthentication.LoginAuthenticationFilter;
import com.format_project.meeting.bean.LoginAuthentication.LoginFailureHandler;
import com.format_project.meeting.bean.LoginAuthentication.LoginSuccessHandler;
import com.format_project.meeting.service.impl.UserDetailsServiceImpl;

import org.apache.catalina.filters.CorsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Configuration
@EnableWebSecurity
// 允许Spring查找并自动应用此class于全局Web Security
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    public UserDetailsServiceImpl userDetailsService;
    // @Autowired
    // public LoginSuccessHandler successHandler;
    // @Autowired
    // public LoginFailureHandler failureHandler;
    @Override
    public void configure(AuthenticationManagerBuilder builder)throws Exception{
        // 重新配置AuthenticationManager
        builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());//调用了实例化passwordEncoder方法
    }
    @Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
    }
    @Bean
	public PasswordEncoder passwordEncoder() {
        
		return new BCryptPasswordEncoder();
    }
    
    @Bean
    public LoginSuccessHandler successHandler(){
        return new LoginSuccessHandler();
    }
    @Bean
    public LoginFailureHandler failureHandler(){
        return new LoginFailureHandler();
    }

    @Bean
    public LoginAuthenticationFilter loginAuthenticationFilter() throws Exception{
        LoginAuthenticationFilter authFilter =  new LoginAuthenticationFilter();
        // 相当于认证成功/失败的回调
        authFilter.setAuthenticationManager(authenticationManagerBean());
        authFilter.setAuthenticationSuccessHandler(successHandler());
        authFilter.setAuthenticationFailureHandler(failureHandler());
        return authFilter;
    }
    @Bean
    public MyCorsFilter myCorsFilter(){
        return new MyCorsFilter();
    }
    @Bean
    public JwtTokenFilter jwtFilter(){
        return new JwtTokenFilter();
    }

    /* @Bean
    public UrlBasedCorsConfigurationSource configuration(){
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setMaxAge(3*60*60L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    } */

    @Override
    protected void configure(HttpSecurity http)throws Exception{
        http.cors().and().csrf().disable()
        // dont authenticate this particular request
				// .authorizeRequests().antMatchers("/user/login").permitAll().
				// // all other requests need to be authenticated
				// anyRequest().authenticated().and()
        // .anyRequest().authenticated().and() //添加and()回到http，再配置
        // 明确指定角色对应的可访问的link地址
        /* .authorizeRequests().antMatchers("/user/**").hasRole("USER")
        .antMatchers("/admin/**").hasRole("ADMIN")
        .antMatchers("/login","/register").permitAll()
        */
        // .formLogin().loginPage("/login").and()
        // 用自定义拦截器代替默认拦截器
        .addFilterAt(loginAuthenticationFilter(),UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(myCorsFilter(),ChannelProcessingFilter.class)
        // jwtfilter拦截到的请求没有token，会放过，交给下一个过滤器
        // 看作是未登录
        .addFilterBefore(jwtFilter(),UsernamePasswordAuthenticationFilter.class);
    }

}
