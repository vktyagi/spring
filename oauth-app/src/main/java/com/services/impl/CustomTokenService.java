package com.services.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;

import com.dao.OauthCustomDao;
import com.exception.TokenFailureException;
import com.models.ExceptionModel;

public class CustomTokenService extends DefaultTokenServices {
	@Autowired
	HttpServletRequest request;
	@Autowired
	OauthCustomDao oauthCustomDao;

	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
	}

	@Override
	public OAuth2AccessToken createAccessToken(
			OAuth2Authentication authentication) throws AuthenticationException {
		OAuth2AccessToken oAuth2AccessToken = null;
		
		try {
			oAuth2AccessToken = super.createAccessToken(authentication);
			
		} catch (Exception ex) {
		    
		    try {
			oauthCustomDao.removeActiveTokenForUser(authentication.getUserAuthentication().getName());
			oAuth2AccessToken = super.createAccessToken(authentication);
		    } catch (Exception e) {
			ExceptionModel em=new ExceptionModel();
			em.setStatus("500");
			em.setError("unknown error");
			em.setMessage("Exception while creating access token!"+e.getMessage());
			request.setAttribute("em",em);
			throw new TokenFailureException("Exception while creating access token!"+e.getMessage());
		    }
		}
		return oAuth2AccessToken;
	}

}
