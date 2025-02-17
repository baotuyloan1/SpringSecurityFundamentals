package com.baond.methodauthorization.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(prePostEnabled = true) // enable method level security, allow using @PreAuthorize in the controller
// @PreAuthorize @PostAuthorize @PostFilter @PreFilter : prePostEnabled = true
// @Secured : securedEnabled = true
// @RolesAllowed: jsr250Enabled = true
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(a ->
                        a.anyRequest().authenticated())
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        UserDetails u1 = User.withUsername("baond")
                .password(passwordEncoder().encode("12345"))
                .authorities("read")
                .build();

        UserDetails u2 = User.withUsername("join")
                .password(passwordEncoder().encode("12345"))
                .authorities("write")
                .build();


        InMemoryUserDetailsManager uds = new InMemoryUserDetailsManager(u1,u2);


        return uds;
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
