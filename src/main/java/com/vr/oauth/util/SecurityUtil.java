package com.vr.oauth.util;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vr.oauth.exception.ApplicationException;
import com.vr.oauth.model.User;

/**
 * @author sachin
 */
public class SecurityUtil {

	public static User loggedInUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication instanceof AnonymousAuthenticationToken) {
			throw new ApplicationException("No user session available.");
		}

		return (User) authentication.getPrincipal();
	}

	public static String loggedInUserEmail() {
		return loggedInUser().getEmail();
	}
}
