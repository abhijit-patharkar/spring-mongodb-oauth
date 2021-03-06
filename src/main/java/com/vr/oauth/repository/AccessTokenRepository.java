package com.vr.oauth.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.vr.oauth.model.security.AccessToken;

@Repository
public interface AccessTokenRepository extends MongoRepository<AccessToken, String> {

	AccessToken findByTokenId(String tokenId);

	AccessToken findByRefreshToken(String refreshToken);

	AccessToken findByAuthenticationId(String authenticationId);

	List<AccessToken> findByClientId(String clientId);

	List<AccessToken> findByClientIdAndUserName(String clientId, String userName);
}