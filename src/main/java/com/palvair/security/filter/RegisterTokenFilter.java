package com.palvair.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.palvair.jpa.ConnectionRepository;
import com.palvair.security.model.Connection;
import com.palvair.security.model.User;
import com.palvair.security.model.UserAuthentication;
import com.palvair.security.token.TokenAuthenticationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegisterTokenFilter extends AbstractAuthenticationProcessingFilter {

    private final ConnectionRepository connectionRepository;
    private final TokenAuthenticationService tokenAuthenticationService;
    private final UserDetailsService userDetailsService;

    public RegisterTokenFilter(final String urlMapping,
                               final TokenAuthenticationService tokenAuthenticationService,
                               final UserDetailsService userDetailsService,
                               final AuthenticationManager authManager,
                               final ConnectionRepository connectionRepository) {
        super(new AntPathRequestMatcher(urlMapping));
        this.userDetailsService = userDetailsService;
        this.tokenAuthenticationService = tokenAuthenticationService;
        setAuthenticationManager(authManager);
        this.connectionRepository = connectionRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        final User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
        final Authentication loginToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        return getAuthenticationManager().authenticate(loginToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authentication) throws IOException, ServletException {
        // Lookup the complete User object from the database and create an Authentication for it
        final User authenticatedUser = (User) userDetailsService.loadUserByUsername(authentication.getName());
        System.out.println("authenticatedUser = " + authenticatedUser);

        final Authentication userAuthentication = new UserAuthentication(authenticatedUser);

        // Add the custom token as HTTP header to the response
        tokenAuthenticationService.addAuthentication(response, userAuthentication);

        final Connection connection = new Connection();
        Connection savedConnection = connectionRepository.saveAndFlush(connection);
        System.out.println("savedConnection = " + savedConnection);

        // Add the authentication to the Security context
        SecurityContextHolder.getContext().setAuthentication(userAuthentication);
    }
}
