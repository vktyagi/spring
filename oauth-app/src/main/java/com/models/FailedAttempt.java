package com.models;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BLOCKED_FAILED_ATTEMPTS")
public class FailedAttempt implements Serializable {
    private static final long serialVersionUID = 4271574405684820436L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "USERNAME")
    private String username;
    @Column(name = "IP_ADDRESS")
    private String ipAddress;
    @Column(name = "N_ATTEMPTS")
    private Integer nAttempts;
    @Column(name = "UPDATED_AT")
    private Timestamp updatedAt;
    @Column(name = "FIRST_FAILED_ATTEMPTED_AT")
    private Timestamp firstFailledAttemptAt;
    @Column(name = "BLOCKED_AT")
    private Timestamp blockedAt;
    @Column(name = "CREATED_AT")
    private Timestamp createdAt;

    public FailedAttempt() {
    }

    public FailedAttempt(String username, String ipAddress, Integer nAttempts, Timestamp firstFailledAttemptAt, Timestamp blockedAt, Timestamp createdAt) {
	this.username = username;
	this.ipAddress = ipAddress;
	this.nAttempts = nAttempts;
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

    public Integer getnAttempts() {
	return nAttempts;
    }

    public void setnAttempt(Integer nAttempts) {
	this.nAttempts = nAttempts;
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
	return "FailedAttempt [username=" + username + ", ipAddress=" + ipAddress + ", nAttempt=" + nAttempts + ", updatedAt=" + updatedAt + ", firstFailledAttemptAt=" + firstFailledAttemptAt
		+ ", blockedAt=" + blockedAt + ", createdAt=" + createdAt + "]";
    }
}
