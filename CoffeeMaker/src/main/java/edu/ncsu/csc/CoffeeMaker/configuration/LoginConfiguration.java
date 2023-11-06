package edu.ncsu.csc.CoffeeMaker.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * This is the class to allow certian pages to be visible on logins. Currently
 * it allows a user to see any pages for login. If you want security you should
 * go to the SecSecurity class to modify the login page to be signin.html and
 * follow this tutorial. Tutorial:
 * "https://www.tutorialspoint.com/spring_security/spring_security_form_login_with_database.htm"
 * Just skip login limits for now since that is unneeded.
 */
@Configuration
@EnableWebSecurity
public class LoginConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure ( final HttpSecurity http ) throws Exception {
        http.authorizeRequests().antMatchers( "/*" ).permitAll().and().csrf().disable();

    }

}
