package com.lhind.AnnualLeaveApp.security;

import com.lhind.AnnualLeaveApp.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserServiceImpl userServiceImpl;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final LoginSuccessHandler loginSuccessHandler;

    private DataSource dataSource;

    @Value("${spring.queries.users-query}")
    private String usersQuery;

    @Value("${spring.queries.roles-query}")
    private String rolesQuery;

    public WebSecurityConfiguration(UserServiceImpl userServiceImpl,
                                    BCryptPasswordEncoder bCryptPasswordEncoder,
                                    LoginSuccessHandler loginSuccessHandler,
                                    DataSource dataSource) {
        this.userServiceImpl = userServiceImpl;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.loginSuccessHandler = loginSuccessHandler;
        this.dataSource = dataSource;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .usersByUsernameQuery(usersQuery)
                .authoritiesByUsernameQuery(rolesQuery)
                .dataSource(dataSource)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/user/**")
                .hasAnyAuthority(ApplicationRoles.USER.name())
                .antMatchers("/api/supervisor/**")
                .hasAnyAuthority(ApplicationRoles.SUPERVISOR.name())
                .antMatchers("/api/admin/**")
                .hasAnyAuthority(ApplicationRoles.ADMIN.name())
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .permitAll()
                .passwordParameter("password")
                .usernameParameter("username")
                .successHandler(loginSuccessHandler)
                .loginPage("/api/login")
                .failureUrl("/api/login-error")
                .and()
                .logout()
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/logout")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                .clearAuthentication(true).and()
                .exceptionHandling()
                .accessDeniedPage("/api/access-denied");
    }

}
