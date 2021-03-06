package com.vr.oauth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.vr.oauth.exception.ApplicationException;
import com.vr.oauth.model.User;
import com.vr.oauth.repository.UserRepository;

/**
 * @author sachin
 */
@Service("userDetailsService")
public class UserDetailService implements UserDetailsService {

	private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(UserDetailService.class);

	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		LOG.info("Authenticate user {}", username);
		User user;

		try {
			user = userRepository.findByEmail(username);
			if(user != null) {
				user.setPassword("{bcrypt}"+user.getPassword());
			}
		} catch (Exception repositoryProblem) {
			throw new InternalAuthenticationServiceException(repositoryProblem.getMessage(), repositoryProblem);
		}

		if (user == null) {
			LOG.error("User not found with username {}", username);
			throw new InternalAuthenticationServiceException("Wrong credentials. Please try again.");
		}

		if (user.isLocked()) {
			LOG.error("User account is locked. username {}", username);
			throw new ApplicationException("Account is locked.");
		}

		if (!user.isActive()) {
			LOG.error("User account is not active. username {}", username);
			throw new ApplicationException("User is not active.");
		}

		return user;
	}

	@Autowired
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
}
