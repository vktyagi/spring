package com.services.impl;

import java.util.Collection;

import javax.sql.DataSource;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

/**
 * Implementation of token services that stores tokens in a database.
 * 
 * @author dkmishra
 */
public class JDBCOauthTokenService extends JdbcTokenStore {

	public JDBCOauthTokenService(DataSource dataSource) {
		super(dataSource);

	}

	@Override
	protected OAuth2AccessToken deserializeAccessToken(byte[] token) {
		return super.deserializeAccessToken(token);

	}

	@Override
	protected OAuth2Authentication deserializeAuthentication(
			byte[] authentication) {
		return super.deserializeAuthentication(authentication);
	}

	@Override
	protected OAuth2RefreshToken deserializeRefreshToken(byte[] token) {
		return super.deserializeRefreshToken(token);
	}

	@Override
	protected String extractTokenKey(String arg0) {
		return super.extractTokenKey(arg0);
	}

	@Override
	public Collection<OAuth2AccessToken> findTokensByClientId(String arg0) {
		return super.findTokensByClientId(arg0);
	}

	/*
	 * @Override public Collection<OAuth2AccessToken>
	 * findTokensByUserName(String arg0) { return
	 * super.findTokensByUserName(arg0); }
	 */

	@Override
	public OAuth2AccessToken getAccessToken(OAuth2Authentication arg0) {
		return super.getAccessToken(arg0);
	}

	@Override
	public OAuth2AccessToken readAccessToken(String arg0) {
		return super.readAccessToken(arg0);
	}

	@Override
	public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
		return super.readAuthentication(token);
	}

	@Override
	public OAuth2Authentication readAuthentication(String arg0) {
		return super.readAuthentication(arg0);
	}

	@Override
	public OAuth2Authentication readAuthenticationForRefreshToken(
			OAuth2RefreshToken token) {
		return super.readAuthenticationForRefreshToken(token);
	}

	@Override
	public OAuth2Authentication readAuthenticationForRefreshToken(String arg0) {
		return super.readAuthenticationForRefreshToken(arg0);
	}

	@Override
	public OAuth2RefreshToken readRefreshToken(String arg0) {
		return super.readRefreshToken(arg0);
	}

	@Override
	public void removeAccessToken(OAuth2AccessToken token) {
		super.removeAccessToken(token);
	}

	@Override
	public void removeAccessToken(String tokenValue) {
		super.removeAccessToken(tokenValue);
	}

	@Override
	public void removeAccessTokenUsingRefreshToken(
			OAuth2RefreshToken refreshToken) {
		super.removeAccessTokenUsingRefreshToken(refreshToken);
	}

	@Override
	public void removeAccessTokenUsingRefreshToken(String refreshToken) {
		super.removeAccessTokenUsingRefreshToken(refreshToken);
	}

	@Override
	public void removeRefreshToken(OAuth2RefreshToken token) {
		super.removeRefreshToken(token);
	}

	@Override
	public void removeRefreshToken(String token) {
		super.removeRefreshToken(token);
	}

	@Override
	protected byte[] serializeAccessToken(OAuth2AccessToken token) {
		return super.serializeAccessToken(token);
	}

	@Override
	protected byte[] serializeAuthentication(OAuth2Authentication authentication) {
		return super.serializeAuthentication(authentication);
	}

	@Override
	protected byte[] serializeRefreshToken(OAuth2RefreshToken token) {
		return super.serializeRefreshToken(token);
	}

	@Override
	public void setAuthenticationKeyGenerator(
			AuthenticationKeyGenerator authenticationKeyGenerator) {
		super.setAuthenticationKeyGenerator(authenticationKeyGenerator);
	}

	@Override
	public void setDeleteAccessTokenFromRefreshTokenSql(
			String deleteAccessTokenFromRefreshTokenSql) {
		super.setDeleteAccessTokenFromRefreshTokenSql(deleteAccessTokenFromRefreshTokenSql);
	}

	@Override
	public void setDeleteAccessTokenSql(String deleteAccessTokenSql) {
		super.setDeleteAccessTokenSql(deleteAccessTokenSql);
	}

	@Override
	public void setDeleteRefreshTokenSql(String deleteRefreshTokenSql) {
		super.setDeleteRefreshTokenSql(deleteRefreshTokenSql);
	}

	@Override
	public void setInsertAccessTokenSql(String insertAccessTokenSql) {
		super.setInsertAccessTokenSql(insertAccessTokenSql);
	}

	@Override
	public void setInsertRefreshTokenSql(String insertRefreshTokenSql) {
		super.setInsertRefreshTokenSql(insertRefreshTokenSql);
	}

	@Override
	public void setSelectAccessTokenAuthenticationSql(
			String selectAccessTokenAuthenticationSql) {
		super.setSelectAccessTokenAuthenticationSql(selectAccessTokenAuthenticationSql);
	}

	@Override
	public void setSelectAccessTokenFromAuthenticationSql(
			String selectAccessTokenFromAuthenticationSql) {
		super.setSelectAccessTokenFromAuthenticationSql(selectAccessTokenFromAuthenticationSql);
	}

	@Override
	public void setSelectAccessTokenSql(String selectAccessTokenSql) {
		super.setSelectAccessTokenSql(selectAccessTokenSql);
	}

	@Override
	public void setSelectRefreshTokenAuthenticationSql(
			String selectRefreshTokenAuthenticationSql) {
		super.setSelectRefreshTokenAuthenticationSql(selectRefreshTokenAuthenticationSql);
	}

	@Override
	public void setSelectRefreshTokenSql(String selectRefreshTokenSql) {
		super.setSelectRefreshTokenSql(selectRefreshTokenSql);
	}

	@Override
	public void storeAccessToken(OAuth2AccessToken token,
			OAuth2Authentication authentication) {
		super.storeAccessToken(token, authentication);
	}

	@Override
	public void storeRefreshToken(OAuth2RefreshToken refreshToken,
			OAuth2Authentication authentication) {
		super.storeRefreshToken(refreshToken, authentication);
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public boolean equals(Object arg0) {
		return super.equals(arg0);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public String toString() {
		return super.toString();
	}

}