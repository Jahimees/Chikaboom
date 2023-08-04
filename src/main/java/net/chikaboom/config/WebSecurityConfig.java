package net.chikaboom.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz ->
                        authz
//                                .requestMatchers("/chikaboom/personality/**").hasAnyRole("MASTER", "CLIENT")
                                .requestMatchers(HttpMethod.GET, "/login").permitAll()
                                .requestMatchers(HttpMethod.POST, "/login").permitAll()
                                .requestMatchers("/**").permitAll()
                )
                .formLogin(form -> {
                    form
                            .loginPage("/chikaboom/main#login")
                            .defaultSuccessUrl("/chikaboom/main")
                            .failureForwardUrl("/chikaboom/main")
                            .usernameParameter("nickname")
                            .passwordParameter("password")
                            .permitAll();
                })
                .logout(logout -> {
                    logout.permitAll();
                    logout.logoutSuccessUrl("/chikaboom/main");
                });
//                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
