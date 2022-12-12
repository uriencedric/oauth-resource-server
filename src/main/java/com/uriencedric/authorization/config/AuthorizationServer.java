package com.uriencedric.authorization.config;

import com.uriencedric.authorization.service.ServiceUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.*;

@Slf4j
@Configuration
@EnableAuthorizationServer
@PropertySource("classpath:application-${spring.profiles.active}.yml")
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {

    @Value("${server.oauth.security.jwt.keys.public.path}")
    private String jwtPublicKeyPath;

	@Value("${server.oauth.security.jwt.keys.private.path}")
	private String jwtPrivateKeyPath;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private ServiceUserService serviceUserService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
		endpoints.authenticationManager(this.authenticationManager)
			.userDetailsService(this.userDetailsService)
			.reuseRefreshTokens(false)
			.accessTokenConverter(accessTokenConverter())
			.addInterceptor(handlerInterceptorAdapter());
	}

	public HandlerInterceptorAdapter handlerInterceptorAdapter() {
		return new HandlerInterceptorAdapter() {
			@Override
			public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
				response.setHeader("Access-Control-Allow-Origin", "*");
				response.setHeader("Access-Control-Allow-Methods", "GET");
				response.setHeader("Access-Control-Max-Age", "3600");
				response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization");
				return super.preHandle(request, response, handler);
			}
		};
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
        JwtTokenConverter jwtTokenConverter = new JwtTokenConverter();
		KeyFactory keyFactory;
		PrivateKey privateKey;
		try {
			keyFactory = KeyFactory.getInstance("RSA");

			// Load Private Key
			Path privateKeyPath = Paths.get(this.jwtPrivateKeyPath);
			byte[] privateKeyBytesArray = Files.readAllBytes(privateKeyPath);
			KeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKeyBytesArray);
			privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

		} catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException e) {
			throw new RuntimeException(String.format("Cannot import private key : %s", e.getMessage()));
		}

		PublicKey publicKey;
		try {
			Path publicKeyPath = Paths.get(this.jwtPublicKeyPath);
			byte[] publicKeyByteArray = Files.readAllBytes(publicKeyPath);
			X509EncodedKeySpec keySpecPub = new X509EncodedKeySpec(publicKeyByteArray);
			publicKey = keyFactory.generatePublic(keySpecPub);

		} catch (IOException | InvalidKeySpecException e) {
			throw new RuntimeException(String.format("Can't import public key : %s", e.getMessage()));
		}

		KeyPair keyPair = new KeyPair(publicKey, privateKey);
		jwtTokenConverter.setKeyPair(keyPair);

        return jwtTokenConverter;
	}

	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.withClientDetails(this.serviceUserService);
	}
}
