package com.vr.oauth.converter;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

import com.mongodb.DBObject;
import com.vr.oauth.mapper.UserMapper;

@ReadingConverter
@SuppressWarnings("rawtypes")
public class AuthenticationTokenConverter implements Converter<DBObject, OAuth2Authentication> {

	@Override
	public OAuth2Authentication convert(DBObject source) {
		DBObject storedRequest = (DBObject) source.get("storedRequest");
		OAuth2Request oAuth2Request = new OAuth2Request((Map<String, String>) storedRequest.get("requestParameters"),
				(String) storedRequest.get("clientId"), null, true, new HashSet<>((List) storedRequest.get("scope")),
				null, null, null, null);
		DBObject userAuthorization = (DBObject) source.get("userAuthentication");
		Object principal = getPrincipalObject(userAuthorization.get("principal"));
		Authentication userAuthentication = new UsernamePasswordAuthenticationToken(principal,
				userAuthorization.get("credentials"), getAuthorities((List) userAuthorization.get("authorities")));
		OAuth2Authentication authentication = new OAuth2Authentication(oAuth2Request, userAuthentication);
		return authentication;
	}

	private Object getPrincipalObject(Object principal) {
		if (principal instanceof DBObject) {
			return UserMapper.mapMongoObject((DBObject) principal);
		} else {
			return principal;
		}
	}

	private Collection<GrantedAuthority> getAuthorities(List<Map<String, String>> authorities) {
		Set<GrantedAuthority> grantedAuthorities = new HashSet<>(authorities.size());
		for (Map<String, String> authority : authorities) {
			grantedAuthorities.add(new SimpleGrantedAuthority(authority.get("role")));
		}
		return grantedAuthorities;
	}

}