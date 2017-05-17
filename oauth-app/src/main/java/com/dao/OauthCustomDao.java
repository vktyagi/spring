package com.dao;

import com.models.User;



public interface OauthCustomDao {
	User findByUserName(String username);
	void update(User user);
	int removeActiveTokenForUser(String userName) throws Exception;
}