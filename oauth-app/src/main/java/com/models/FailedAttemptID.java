package com.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class FailedAttemptID implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "USERNAME")
    private String username;
    @Column(name = "IP_ADDRESS")
    private String ipAddress;

    public FailedAttemptID() {
    }

    public FailedAttemptID(String username, String ipAddress) {
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
	FailedAttemptID other = (FailedAttemptID) obj;
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
