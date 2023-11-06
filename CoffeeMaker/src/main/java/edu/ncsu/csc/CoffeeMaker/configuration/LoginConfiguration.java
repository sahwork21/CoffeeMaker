package edu.ncsu.csc.CoffeeMaker.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 *
 */
@Configuration
@EnableWebSecurity
public class LoginConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure ( final HttpSecurity http ) throws Exception {
        http.authorizeRequests().antMatchers( "/*" ).permitAll().and().csrf().disable();

    }

}
