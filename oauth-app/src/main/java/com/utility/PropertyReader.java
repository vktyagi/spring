package com.utility;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertyReader {
    @Value(value = "#{application.elapsedTimeInMS}")
    public long elapsedTime;

    @Value(value = "#{application.blockingPeriodInMS}")
    public long blockingPeriod;

    @Value(value = "#{application.maxBlockedAttemptAllowed}")
    public int maxBlockAttempt;
    
    @Value(value = "#{application.maxLockedAttemptAllowed}")
    public int maxlockAttempt;
    
    @Value(value = "#{application.memcacheExpirationTimeInSeconds}")
    public int memcacheExpirationTimeInSec;
    
    
    
    public long getElapsedTime() {
        return elapsedTime;
    }
    

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public long getBlockingPeriod() {
        return blockingPeriod;
    }

    public void setBlockingPeriod(long blockingPeriod) {
        this.blockingPeriod = blockingPeriod;
    }

    public int getMaxBlockedAttemptAllowed() {
        return maxBlockAttempt;
    }

    public void setMaxBlockedAttemptAllowed(int maxBlockedAttemptAllowed) {
        this.maxBlockAttempt = maxBlockedAttemptAllowed;
    }

    public int getMaxLockedAttemptAllowed() {
        return maxlockAttempt;
    }

    public void setMaxLockedAttemptAllowed(int maxLockedAttemptAllowed) {
        this.maxlockAttempt = maxLockedAttemptAllowed;
    }


    public int getMemcacheExpirationTimeInSec() {
        return memcacheExpirationTimeInSec;
    }


    public void setMemcacheExpirationTimeInSec(int memcacheExpirationTimeInSec) {
        this.memcacheExpirationTimeInSec = memcacheExpirationTimeInSec;
    }

    
}
