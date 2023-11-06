package edu.ncsu.csc.CoffeeMaker.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Manage the login flow of the system using a filterChain to restrict allowed
 * inputs
 */
@Configuration
@EnableWebSecurity
public class SecSecurityConfig {

    /**
     * Build our security chain for the http request with the default page of
     * signin and allowing the login API call
     *
     * @param http
     *            the protocol builder we get to start
     * @return a filter chain for the system to control how signins work
     * @throws Exception
     *             if the http is formatted incorrectly
     */
    @Bean
    public SecurityFilterChain filterChain ( final HttpSecurity http ) throws Exception {
        http.authorizeRequests().antMatchers( "/customer/**" ).hasRole( "CUSTOMER" ).antMatchers( "/barista/**" )
                .hasRole( "BARISTA" ).antMatchers( "/manager/**" ).hasRole( "MANAGER" ).and().formLogin()
                .loginPage( "/signin.html" ).loginProcessingUrl(
                        "/login" )/*
                                   * .defaultSuccessUrl( "/index.html", true )
                                   */;
        return http.build();
    }
}
