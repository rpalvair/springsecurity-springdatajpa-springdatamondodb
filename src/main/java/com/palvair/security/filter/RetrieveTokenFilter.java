package com.palvair.security.filter;

import com.palvair.security.token.TokenAuthenticationService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class RetrieveTokenFilter extends GenericFilterBean {

    private final TokenAuthenticationService tokenAuthenticationService;

    public RetrieveTokenFilter(TokenAuthenticationService taService) {
        this.tokenAuthenticationService = taService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final Authentication authentication = tokenAuthenticationService.getAuthentication((HttpServletRequest) servletRequest);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(servletRequest, servletResponse); // always continue
    }
}
