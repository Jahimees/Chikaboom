package net.chikaboom.config;

import net.chikaboom.controller.auth.handler.LoginFailureHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
//@EnableMethodSecurity(
//        prePostEnabled = true,
//        securedEnabled = true,
//        jsr250Enabled = true)
public class WebSecurityConfig {

    private final LoginFailureHandler loginFailureHandler;

    @Autowired
    public WebSecurityConfig(LoginFailureHandler loginFailureHandler) {
        this.loginFailureHandler = loginFailureHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz ->
                        authz
                                .requestMatchers("/chikaboom/personality/**").hasAnyRole("MASTER", "CLIENT")
                                .requestMatchers("/chikaboom/account/**").permitAll()
                                .requestMatchers("/", "/login").permitAll()
                                .requestMatchers("/**").permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/chikaboom/main#login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/chikaboom/main")
                        .failureHandler(loginFailureHandler)
                        .usernameParameter("nickname")
                        .passwordParameter("password")
                        .permitAll())
                .logout(logout -> {
                    logout.permitAll();
                    logout.logoutSuccessUrl("/chikaboom/main");
                })
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
