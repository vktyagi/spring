package com.models;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@IdClass(value=BlockedFailedAttempt.BF_PK.class)
@Table(name = "BLOCKED_FAILED_ATTEMPTS")
public class BlockedFailedAttempt implements Serializable {
    private static final long serialVersionUID = 4271574405684820436L;

    @Id
    @Column(name = "USERNAME")
    private String username;
    @Id
    @Column(name = "IP_ADDRESS")
    private String ipAddress;
    @Column(name = "N_ATTEMPT")
    private Integer nAttempt;
    @Column(name = "UPDATED_AT")
    private Timestamp updatedAt;
    @Column(name = "FIRST_FAILLED_ATTEMPT_AT")
    private Timestamp firstFailledAttemptAt;
    @Column(name = "BLOCKED_AT")
    private Timestamp blockedAt;
    @Column(name = "CREATED_AT")
    private Timestamp createdAt;

    public BlockedFailedAttempt() {
    }

    public BlockedFailedAttempt(String username, String ipAddress, Integer nAttempt, Timestamp updatedAt, Timestamp firstFailledAttemptAt, Timestamp blockedAt, Timestamp createdAt) {
	this.username = username;
	this.ipAddress = ipAddress;
	this.nAttempt = nAttempt;
	this.updatedAt = updatedAt;
	this.firstFailledAttemptAt = firstFailledAttemptAt;
	this.blockedAt = blockedAt;
	this.createdAt = createdAt;
    }

    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    public String getIpAddress() {
	return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
	this.ipAddress = ipAddress;
    }

    public Integer getnAttempt() {
	return nAttempt;
    }

    public void setnAttempt(Integer nAttempt) {
	this.nAttempt = nAttempt;
    }

    public Timestamp getUpdatedAt() {
	return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
	this.updatedAt = updatedAt;
    }

    public Timestamp getFirstFailledAttemptAt() {
	return firstFailledAttemptAt;
    }

    public void setFirstFailledAttemptAt(Timestamp firstFailledAttemptAt) {
	this.firstFailledAttemptAt = firstFailledAttemptAt;
    }

    public Timestamp getBlockedAt() {
	return blockedAt;
    }

    public void setBlockedAt(Timestamp blockedAt) {
	this.blockedAt = blockedAt;
    }

    public Timestamp getCreatedAt() {
	return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
	this.createdAt = createdAt;
    }

    @Override
    public String toString() {
	return "FailedAttempt [username=" + username + ", ipAddress=" + ipAddress + ", nAttempt=" + nAttempt + ", updatedAt=" + updatedAt + ", firstFailledAttemptAt=" + firstFailledAttemptAt
		+ ", blockedAt=" + blockedAt + ", createdAt=" + createdAt + "]";
    }
    
    
    static class BF_PK implements Serializable {

	    private static final long serialVersionUID = 1L;

	    @Column(name = "USERNAME")
	    private String username;
	    @Column(name = "IP_ADDRESS")
	    private String ipAddress;

	    public BF_PK() {
	    }

	    public BF_PK(String username, String ipAddress) {
		this.username = username;
		this.ipAddress = ipAddress;
	    }

	    public String getUsername() {
		return username;
	    }

	    public void setUsername(String username) {
		this.username = username;
	    }

	    public String getIpAddress() {
		return ipAddress;
	    }

	    public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	    }

	    @Override
	    public String toString() {
		return "FailedAttemptID [username=" + username + ", ipAddress=" + ipAddress + "]";
	    }

	    @Override
	    public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ipAddress == null) ? 0 : ipAddress.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	    }

	    @Override
	    public boolean equals(Object obj) {
		if (this == obj)
		    return true;
		if (obj == null)
		    return false;
		if (getClass() != obj.getClass())
		    return false;
		BF_PK other = (BF_PK) obj;
		if (ipAddress == null) {
		    if (other.ipAddress != null)
			return false;
		} else if (!ipAddress.equals(other.ipAddress))
		    return false;
		if (username == null) {
		    if (other.username != null)
			return false;
		} else if (!username.equals(other.username))
		    return false;
		return true;
	    }
	 

    }
}
