package com.euphorie.filters;


import java.io.IOException;
import java.util.ArrayList;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import com.euphorie.auth.service.JwtService;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class JwtAuthenticationFilter extends OncePerRequestFilter{

        private JwtService jwtService;
        public JwtAuthenticationFilter(JwtService jwtService) {
            this.jwtService = jwtService;
        }

        @Override
        protected void doFilterInternal( // ~ CanActivate
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
        ) throws ServletException, IOException {


                    // focus sur l'objet authorization
                    String authorization = request.getHeader("Authorization");

                    // si y a pas un bearer token on n'eglige et passe au method security ! 

                    if (authorization == null || !authorization.startsWith("Bearer ")) {

                        filterChain.doFilter(request, response);
                        return;
                    }

                    // si non la responsabiliter est de la valider
                    String token = authorization.substring(7);

                    Jwt payload  =  jwtService.valider(token);
                    
                    // creer l'objet authentification a mettre dans security context  pour que cet objet rentre en interaction avec beans de spring 
                    // (un cnstructeur parmis les autres qui permet de  imti authenticated a true)
                    Authentication authentication =    new JwtAuthenticationToken(  payload,
                                                                                    new ArrayList<GrantedAuthority>());

                    System.out.println("authenticated = " + authentication.isAuthenticated());
                    System.out.println("authorities = " + authentication.getAuthorities());


                    SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);
                     
                    // prochain filtre
                    filterChain.doFilter(request, response);
        }


}


