package com.vr.oauth.model.security;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

@Document(collection = "refresh_tokens")
public class RefreshToken implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	private String tokenId;
	private OAuth2RefreshToken oAuth2RefreshToken;
	private String authentication;

	public RefreshToken() {

	}

	public RefreshToken(OAuth2RefreshToken oAuth2RefreshToken, OAuth2Authentication authentication) {
		this.oAuth2RefreshToken = oAuth2RefreshToken;
		this.authentication = SerializableObjectConverter.serialize(authentication);
		this.tokenId = oAuth2RefreshToken.getValue();
	}

	public String getTokenId() {
		return tokenId;
	}

	public OAuth2RefreshToken getoAuth2RefreshToken() {
		return oAuth2RefreshToken;
	}

	public OAuth2Authentication getAuthentication() {
		return SerializableObjectConverter.deserialize(authentication);
	}

	public void setAuthentication(OAuth2Authentication authentication) {
		this.authentication = SerializableObjectConverter.serialize(authentication);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}