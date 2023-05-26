package com.example.security1.config;


import com.example.security1.auth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.SecurityFilterChain;

import static org.hibernate.criterion.Restrictions.and;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/user/**").authenticated() // user주소에 대해서 인증을 요구한다
                .antMatchers("/manager/**").access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") // manager주소는 ROLE_MANAGER권한이나 ROLE_ADMIN권한이 있어야 접근할 수 있다.
                .antMatchers("/admin/**").hasRole("ADMIN") // admin주소는 ROLE_ADMIN권한이 있어야 접근할 수 있다.
                .anyRequest().permitAll() // 나머지주소는 인증없이 접근 가능하다
//            .and()
//            .formLogin()
//                .loginPage("/loginForm")
//                .usernameParameter("id")
//                .passwordParameter("pw")
//                .loginProcessingUrl("/login")
//                .defaultSuccessUrl("/")
//                .failureUrl("/loginForm")
            .and()
                .oauth2Login()
                    .loginPage("/loginForm")
                    .defaultSuccessUrl("/")
                    .failureUrl("/loginForm")
                    .userInfoEndpoint()
                    .userService(principalOauth2UserService);
//            .logout()
//                .logoutUrl("/logout")
//                .logoutSuccessUrl("/");


        return http.build();
    }

}
