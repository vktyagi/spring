package com.models;

import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "USERS")
public class User {
    @Id
    @Column(name = "ID")
    private Integer id;
    @Column(name = "USERNAME")
    private String userName;
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "FNAME")
    private String fName;
    @Column(name = "LNAME")
    private String lName;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "LOCATION")
    private String location;
    @Column(name = "CUSTOM_INFO")
    private String customInfo;
    @Column(name = "ENABLED")
    private boolean enabled;
    @Column(name = "LOCKED")
    private boolean locked;
    /*
     * @Column(name = "IS_UM_V2") private boolean isUmV2;
     */
    @Column(name = "N_FAILED_ATTEMPTS")
    private int nFailedAttempt;
    @Column(name = "CREATED_BY")
    private String createdBy;
    @Column(name = "CREATED_AT")
    private Timestamp createdAt;
    @Column(name = "UPDATED_BY")
    private String updatedBy;
    @Column(name = "UPDATED_AT")
    private Timestamp updatedAt;
    /*
     * @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
     * 
     * @JoinTable(name = "users_permissions", joinColumns = { @JoinColumn(name =
     * "user_id") }, inverseJoinColumns = { @JoinColumn(name = "permission_id")
     * }) private Set<Permission> permissions;
     */
    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id") })
    private Set<Role> role;
    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    @JoinTable(name = "users_markets", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "market_id") })
    private Set<Permission> marketPermissions;

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getUserName() {
	return userName;
    }

    public void setUserName(String userName) {
	this.userName = userName;
    }

    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public String getfName() {
	return fName;
    }

    public void setfName(String fName) {
	this.fName = fName;
    }

    public String getlName() {
	return lName;
    }

    public void setlName(String lName) {
	this.lName = lName;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getLocation() {
	return location;
    }

    public void setLocation(String location) {
	this.location = location;
    }

    public String getCustomInfo() {
	return customInfo;
    }

    public void setCustomInfo(String customInfo) {
	this.customInfo = customInfo;
    }

    public boolean isEnabled() {
	return enabled;
    }

    public void setEnable(boolean enabled) {
	this.enabled = enabled;
    }

    public boolean isLocked() {
	return locked;
    }

    public void setLocked(boolean locked) {
	this.locked = locked;
    }

    public int getnFailedAttempt() {
	return nFailedAttempt;
    }

    public void setnFailedAttempt(int nFailedAttempt) {
	this.nFailedAttempt = nFailedAttempt;
    }

    public String getCreatedBy() {
	return createdBy;
    }

    public void setCreatedBy(String createdBy) {
	this.createdBy = createdBy;
    }

    public Timestamp getCreatedAt() {
	return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
	this.createdAt = createdAt;
    }

    public String getUpdatedBy() {
	return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
	this.updatedBy = updatedBy;
    }

    public Timestamp getUpdatedAt() {
	return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
	this.updatedAt = updatedAt;
    }

    public Set<Role> getRole() {
	return role;
    }

    public void setRole(Set<Role> role) {
	this.role = role;
    }

    public Set<Permission> getMarketPermissions() {
	return marketPermissions;
    }

    public void setMarketPermissions(Set<Permission> marketPermissions) {
	this.marketPermissions = marketPermissions;
    }

}
