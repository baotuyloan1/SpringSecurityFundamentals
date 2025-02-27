package com.baond.customauthentication.config.security.filters;

import com.baond.customauthentication.config.security.authentication.CustomAuthentication;
import com.baond.customauthentication.config.security.manager.CustomAuthenticationManager;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class CustomAuthenticationFilter extends OncePerRequestFilter {

    private final CustomAuthenticationManager customAuthenticationManager;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

//        1.create an authentication object which is not yet authenticated
//        2.delegate the authentication object to the manger
//        3.get back the authentication form the manager
//        4.if the object is authenticated then send request to the next filter.
        String key = String.valueOf(request.getHeader("key"));
        CustomAuthentication customAuthentication = new CustomAuthentication(false, key);
        var a = customAuthenticationManager.authenticate(customAuthentication);

        if (a.isAuthenticated()){
            SecurityContextHolder.getContext().setAuthentication(a);
            filterChain.doFilter(request,response); // only when authentication worked

        }

    }
}
