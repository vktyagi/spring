package com.services.impl;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dao.FailedAttemptDao;
import com.dao.OauthCustomDao;
import com.exception.BlockedClientException;
import com.models.FailedAttempt;
import com.models.User;
import com.services.LoginAttemptLogService;
import com.utility.PropertyReader;

@Service("loginAttemptLogService")
public class LoginAttemptLogServiceImpl implements LoginAttemptLogService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginAttemptLogServiceImpl.class);


    @Autowired
    PropertyReader propertyReader;

    @Autowired
    private FailedAttemptDao failedAttemptDao;

    @Autowired
    private OauthCustomDao oauthCustomDao;

    @Override
    public void logFailedAttempt(User user, String username, String password, String ipAddress) {
	LOGGER.info("Verifying failed logging attempt for username: " + username + " and ip-address: " + ipAddress);

	// BlockedFailedAttempt bfa = new BlockedFailedAttempt(uName,
	// "127.0.0.2", 1, null, new Timestamp(currDate.getTime()), null, new
	// Timestamp(currDate.getTime()));

	// failedAttemptDao.markFailedAttempt(bfa);

	// TODO:
	// check if blocked if yes -> throw IPBlockedException
	// if NO -> match the password from db and request
	// if password matches == > let the request go ahead
	// if password not matches == > mark failed attempt let request go ahead

	Date currDate = Calendar.getInstance().getTime();

	FailedAttempt failedAttempt = failedAttemptDao.getFailedAttempt(username, ipAddress);

	if (null != failedAttempt) {
	    if (null != failedAttempt.getBlockedAt() && ((currDate.getTime() - failedAttempt.getBlockedAt().getTime()) < propertyReader.getBlockingPeriod())) {
		// BLOCKED_AT IS SET ==> IP-ADDRESS IS BLOCKED FOR USER
		throw new BlockedClientException("IP Address " + ipAddress + " is blocked for user " + username);

	    }
	    if (!user.getPassword().equals(password)) {

		if ((currDate.getTime() - failedAttempt.getFirstFailledAttemptAt().getTime()) > propertyReader.getElapsedTime()) {
		    // RESET FAILED ATTEMPT
		    failedAttempt.setBlockedAt(null);
		    failedAttempt.setFirstFailledAttemptAt(new Timestamp(currDate.getTime()));
		    failedAttempt.setnAttempt(1);
		} else {
		    // PASSWORD DID NOT MATCHES=> INCREASE THE NO. OF FAILED
		    // ATTEMPT
		    // BY 1.
		    failedAttempt.setnAttempt(failedAttempt.getnAttempts() + 1);

		    if (failedAttempt.getnAttempts() == propertyReader.getMaxBlockedAttemptAllowed()) {
			// MAX_FAILED_ATTEMPT_ALLOWED REACHED WITH IN
			// ELAPSED_TIME ==> BLOCKING THE IP-ADDRESS FOR USER.
			failedAttempt.setnAttempt(0);
			failedAttempt.setBlockedAt(new Timestamp(currDate.getTime()));
		    }
		    if (failedAttempt.getnAttempts() == 1) {
			failedAttempt.setBlockedAt(null);
			failedAttempt.setFirstFailledAttemptAt(new Timestamp(currDate.getTime()));
		    }
		}

		failedAttemptDao.updateFailedAttempt(failedAttempt);
	    } else {
		// PASSWORD MATCHES AFTER FAILED ATTEMPT EXIST -> FAILED ATTEMPT
		// WILL BE REMOMVED
		failedAttemptDao.removeFailedAttempt(failedAttempt);
	    }
	} else {
	    // NO ATTEMPT WAS THERE/ FIRST FAILED ATTEMPT
	    if (!user.getPassword().equals(password)) {
		failedAttemptDao.markFailedAttempt(new FailedAttempt(username, ipAddress, 1, new Timestamp(currDate.getTime()), null, new Timestamp(currDate.getTime())));
	    }
	}
    }

    public void verifyLoginAttempt(User user, String username, String password) {
	LOGGER.info("Verify login attempt for username: " + username);

	// Check if user is already locked throw exception user is locked
	// if no entry for that user and user password is wrong make first entry
	// into DB.
	// and if user has db entry then check number of attempt is less then
	// NO_OF_ALLOWED_ATTEMPT and current time is less then
	// (First_attempt+ATTEMPT_TIME)
	// update a db entry
	// if user has less then NO_OF_ALLOWED_ATTEMPT and current time is
	// greater then (First_attempt+ATTEMPT_TIME)
	// make new entry delete all previous entries
	// if user failed attempt equal to NO_OF_ALLOWED_ATTEMPT locked the
	// user.

	if (!user.getPassword().equals(password)) {
	    user.setnFailedAttempt(user.getnFailedAttempt() + 1);

	    if (user.getnFailedAttempt() == propertyReader.getMaxLockedAttemptAllowed()) {
		user.setLocked(true);
	    }
	    oauthCustomDao.update(user);
	} else if (user.getnFailedAttempt() != 0) {
	    user.setnFailedAttempt(0);
	    oauthCustomDao.update(user);
	}
	
    }

}
