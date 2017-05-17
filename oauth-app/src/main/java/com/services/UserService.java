package com.services;

import com.exception.InvalidateRoleException;
import com.models.User;

public interface UserService {
	public User validatedUserRole(String userRole)throws InvalidateRoleException ;

}
