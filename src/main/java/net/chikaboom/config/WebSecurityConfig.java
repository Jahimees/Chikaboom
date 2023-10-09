package net.chikaboom.config;

import lombok.RequiredArgsConstructor;
import net.chikaboom.controller.error.handler.CustomAccessDeniedHandler;
import net.chikaboom.controller.error.handler.CustomLoginSuccessHandler;
import net.chikaboom.controller.error.handler.CustomLogoutSuccessHandler;
import net.chikaboom.controller.error.handler.LoginFailureHandler;
import net.chikaboom.util.CustomWebAuthenticationDetailsSource;
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
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final LoginFailureHandler loginFailureHandler;
    private final CustomLoginSuccessHandler customLoginSuccessHandler;
    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomWebAuthenticationDetailsSource customWebAuthenticationDetailsSource;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz ->
                        authz
                                .requestMatchers("/**").permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/chikaboom/main#login")
                        .authenticationDetailsSource(customWebAuthenticationDetailsSource)
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/chikaboom/main")
                        .successHandler(customLoginSuccessHandler)
                        .failureHandler(loginFailureHandler)
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .permitAll()
                )
                .logout(logout -> {
                    logout.permitAll();
                    logout.logoutSuccessHandler(customLogoutSuccessHandler);
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
