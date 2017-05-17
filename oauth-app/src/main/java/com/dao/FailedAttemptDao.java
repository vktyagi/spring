package com.dao;

import com.models.FailedAttempt;

public interface FailedAttemptDao {

    FailedAttempt markFailedAttempt(FailedAttempt failedAttempt);
    
    FailedAttempt updateFailedAttempt(FailedAttempt failedAttempt);

    FailedAttempt getFailedAttempt(String uName, String remoteAddr);

    void removeFailedAttempt(FailedAttempt failedAttempt);

    FailedAttempt getFailedAttempt(String username);

}
