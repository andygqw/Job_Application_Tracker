package com.gw.JobApplicationTracker.component;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.core.userdetails.User;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.provisioning.InMemoryUserDetailsManager;
// import org.springframework.security.web.SecurityFilterChain;

import com.gw.JobApplicationTracker.service.CustomUserDetailsService;

import static org.springframework.security.config.Customizer.withDefaults;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) throws Exception {

        logger.warn("At least we at WebFilterChain");
        // http
        //     .authorizeExchange(authorizeRequests ->
        //         authorizeRequests
        //             .pathMatchers("api/login", "api/register").permitAll()
        //             .anyExchange().authenticated()
                    
        //     )
        //     .httpBasic(withDefaults())
        //     .formLogin(formLogin ->
        //         formLogin
        //             .loginPage("/login")
        //             .permitAll()
        //             .defaultSuccessUrl("/dashboard", true)
        //     )
        //     .logout(logout ->
        //         logout
        //             .permitAll()
        //             .logoutSuccessUrl("/index")
        //     );

        // return http.build();
        http
            .authorizeExchange()
                .pathMatchers("/login", "/register").permitAll()
                .anyExchange().authenticated()
                .and()
                .httpBasic().and()
                .formLogin();
                return http.build();
    }

    // @Bean
    // public UserDetailsService userDetailsService() {
    //     InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
    //     manager.createUser(User.withUsername("user")
    //         .password("{noop}password")
    //         .roles("USER")
    //         .build());
    //     manager.createUser(User.withUsername("admin")
    //         .password("{noop}admin")
    //         .roles("ADMIN")
    //         .build());
    //     return manager;
    // }

    // @Bean
    // public ReactiveUserDetailsService userDetailsService() {
    //     logger.warn("Are we in userDetailsService?");
    //     return new CustomUserDetailsService();
    // }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
