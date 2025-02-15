package com.baond.endpointauthorization.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,  HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector).servletPath("/path");

        http.httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(a ->
//                        a.anyRequest().permitAll()
//                        a.anyRequest().authenticated()
//                        a.anyRequest().denyAll()
//                        a.anyRequest().hasAuthority("write")
//                        a.anyRequest().hasAnyAuthority("read", "write")
//                        a.anyRequest().hasRole("ADMIN")
//                        a.anyRequest().hasAnyRole("ADMIN", "MANAGER")
                        a.anyRequest().access(new WebExpressionAuthorizationManager("isAuthenticated() and hasAuthority('write') and hasRole('MANAGER')")) // SpEL -> authorization rules
                ); // endpoint level authorization





//        matcher method + authorization rule
//        1. which mater methods should you use and how (anyRequest(), mvcMatchers(), antMatchers(), regexMatchers())
//        2. how to apply different authorization rules.
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        var uds = new InMemoryUserDetailsManager();

        var u1 = User.withUsername("baond")
                .password(passwordEncoder().encode("12345"))
             //   .roles("ADMIN") // equivalent with and authority named "ROLE_ADMIN", can'tuse authorities() and roles() together, because the last function will override the first.
                .authorities("read")
                .build();

        var u2 = User.withUsername("david")
                .password(passwordEncoder().encode("12345"))
                .authorities("write", "ROLE_MANAGER").build();

        uds.createUser(u1);
        uds.createUser(u2);

        return uds;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
