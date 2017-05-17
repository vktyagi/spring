package com.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.OauthCustomDao;
import com.exception.InvalidateRoleException;
import com.models.User;
import com.services.UserService;

@Service("userServics")
public class UserServiceImpl implements UserService {
	@Autowired
	OauthCustomDao oauthCustomDao;

	@Override
	public User validatedUserRole(String userRole)
			throws InvalidateRoleException {
		//User user = oauthCustomDao.findByUserName(SecurityContextHolder.getContext()
				//.getAuthentication().getName());

		return null;
	}
}
