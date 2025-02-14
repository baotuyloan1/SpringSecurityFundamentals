package com.baond.multipleauthenticationproviders.config;

import com.baond.multipleauthenticationproviders.config.filters.ApiKeyFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Value("${the.secret}")
    private String key;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic(Customizer.withDefaults())
                .addFilterBefore(new ApiKeyFilter(key), BasicAuthenticationFilter.class)
                .authorizeHttpRequests(a -> a.anyRequest().authenticated());
//                .authenticationManager()  or by adding a bean of type AuthenticationManager// override the default authentication manager
//                .authenticationProvider() // add the custom authentication provider, it doesn't override the AP, it adds one more to the collection
        return http.build();


    }
}
