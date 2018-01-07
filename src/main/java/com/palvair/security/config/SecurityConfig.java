package com.palvair.security.config;

import com.palvair.jpa.ConnectionRepository;
import com.palvair.security.UserDetailsService;
import com.palvair.security.filter.RegisterTokenFilter;
import com.palvair.security.filter.RetrieveTokenFilter;
import com.palvair.security.token.TokenAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Created by widdy on 20/12/2015.
 */
@EnableWebSecurity
@Configuration
@ComponentScan("com.palvair")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public SecurityConfig() {
        super(true);
    }


    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private ConnectionRepository connectionRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                /*.exceptionHandling().and()
				.anonymous().and()
				.servletApi().and()
				.headers().cacheControl().and()*/
                .authorizeRequests()

                //allow anonymous resource requests
                .antMatchers("/").permitAll()
                .antMatchers("/favicon.ico").permitAll()
                .antMatchers("/resources/**").permitAll()

                //allow anonymous POSTs to login
                .antMatchers(HttpMethod.POST, "/api/login").permitAll()

                //allow anonymous GETs to API
                .antMatchers(HttpMethod.GET, "/api/**").permitAll()

                //defined Admin only API area
                .antMatchers("/admin/**").hasRole("ADMIN")

                //all other request need to be authenticated
                .anyRequest().hasRole("USER").and()

                // custom JSON based authentication by POST of {"username":"<name>","password":"<password>"} which sets the token header upon authentication
                .addFilterBefore(new RegisterTokenFilter("/api/login", tokenAuthenticationService, userDetailsService, authenticationManager(), connectionRepository), UsernamePasswordAuthenticationFilter.class)

                // custom Token based authentication based on the header previously given to the client
                .addFilterBefore(new RetrieveTokenFilter(tokenAuthenticationService), UsernamePasswordAuthenticationFilter.class)/*.exceptionHandling().and()
				.anonymous().and()
				.servletApi().and()
				.headers().cacheControl()*/;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
        //auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder()).withUser("admin").password(new BCryptPasswordEncoder().encode("admin")).roles("ADMIN");
    }

    @Override
    protected UserDetailsService userDetailsService() {
        return userDetailsService;
    }

}
