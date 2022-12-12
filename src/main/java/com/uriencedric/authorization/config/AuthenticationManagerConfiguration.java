package com.uriencedric.authorization.config;

import com.uriencedric.authorization.utils.encoder.PBKDF2PasswordEncoder;
import com.uriencedric.authorization.service.UserService;
import com.uriencedric.authorization.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.NoSuchAlgorithmException;

@Configuration
@PropertySource("classpath:application-${spring.profiles.active}.yml")
public class AuthenticationManagerConfiguration extends GlobalAuthenticationConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Value("${server.oauth.security.salt}")
    private String salt;

    @Value("${server.oauth.security.alg}")
    private String algorithm;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws NoSuchAlgorithmException {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() throws NoSuchAlgorithmException {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() throws NoSuchAlgorithmException {
        if (AppConstants.BCRYPT_ALG.equals(this.algorithm)) {
            return new BCryptPasswordEncoder(AppConstants.BCRYPT_ITERATION_COUNT);
        }
        return new PBKDF2PasswordEncoder(this.salt);
    }

}
