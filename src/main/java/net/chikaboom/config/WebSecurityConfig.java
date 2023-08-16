package net.chikaboom.config;

import net.chikaboom.controller.error.handler.CustomAccessDeniedHandler;
import net.chikaboom.controller.error.handler.LoginFailureHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Класс-конфигурация, определяющий настройки Spring Security и его поведения
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    private final LoginFailureHandler loginFailureHandler;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    @Autowired
    public WebSecurityConfig(LoginFailureHandler loginFailureHandler, CustomAccessDeniedHandler accessDeniedHandler) {
        this.loginFailureHandler = loginFailureHandler;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz ->
                        authz
                                .requestMatchers("/**").permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/chikaboom/main#login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/chikaboom/main")
                        .failureHandler(loginFailureHandler)
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .permitAll()

                )
                .logout(logout -> {
                    logout.permitAll();
                    logout.logoutSuccessUrl("/chikaboom/main");
                })
                .exceptionHandling(exceptionHandler ->
                        exceptionHandler
                                .accessDeniedHandler(accessDeniedHandler)
                )
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
