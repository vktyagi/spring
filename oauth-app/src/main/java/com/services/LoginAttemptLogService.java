package com.services;

import com.models.User;


public interface LoginAttemptLogService {
    
    void verifyLoginAttempt(User user, String username, String password);
    void logFailedAttempt(User user, String username, String password, String ipAddress);
    
}
