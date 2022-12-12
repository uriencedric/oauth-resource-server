package com.uriencedric.authorization.config;

import com.uriencedric.authorization.common.exception.AuthException;
import com.uriencedric.authorization.utils.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtTokenConverter extends JwtAccessTokenConverter {


    /**
     *
     * @param accessToken the current access token with its expiration and refresh token
     * @param authentication the current authentication including client and user details
     */
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        if (authentication.getOAuth2Request().getGrantType() != null && authentication.getOAuth2Request().getGrantType().equals(AppConstants.CLIENT_CREDENTIALS)) {
            Object authority = authentication.getOAuth2Request().getRequestParameters().get(AppConstants.USER_AUTHORITY);

            if (authority == null) {
                throw new AuthException(String.format("Missing field %s", AppConstants.USER_AUTHORITY));
            }

            final Map<String, Object> addInfo = new HashMap<>();
            addInfo.put(AppConstants.USER_AUTHORITY, authority);

            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(addInfo);
        }

        accessToken =  super.enhance(accessToken, authentication);
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(new HashMap<>());
        return accessToken;
    }
}
