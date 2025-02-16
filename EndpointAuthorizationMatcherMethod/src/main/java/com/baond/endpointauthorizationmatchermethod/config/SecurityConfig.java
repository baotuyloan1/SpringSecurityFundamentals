package com.baond.endpointauthorizationmatchermethod.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        var uds = new InMemoryUserDetailsManager();

        var u1 = User.withUsername("baond")
                .password(passwordEncoder().encode("12345"))
                .authorities("read") // --> GrantedAuthority interface
                .build();

        var u2 = User.withUsername("join")
                .password(passwordEncoder().encode("12345"))
                .authorities("write")
                .build();

        uds.createUser(u1);
        uds.createUser(u2);
        return uds;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        http.httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers(mvc.pattern("/test1"), mvc.pattern("/test2")).authenticated()
//                        .requestMatchers(mvc.pattern("/test2")).hasAuthority("read")\
//                                .requestMatchers(mvc.pattern(HttpMethod.GET, "/demo/**")).hasAuthority("read")
                                .requestMatchers("/test/test1").authenticated()
                                .anyRequest().permitAll()

                )
                .csrf(c -> c.disable()) // Don't do this in real-world apps
        ;
        return http.build();
    }

    // demo/anything/*/something --> /demo/anything/abc/
    // demo/anything/*/something --> /demo/anything/xyz/something
}
