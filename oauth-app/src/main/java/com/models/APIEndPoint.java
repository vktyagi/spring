package com.models;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "API_ENDPOINTS")
public class APIEndPoint implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID")
    Integer id;
    @Column(name = "ENDPOINT")
    String endPoint;
    @Column(name = "DESC")
    String desc;
    @Column(name = "METHOD")
    String method;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "PERMISSION_ID")
    Permission permission;

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getEndPoint() {
	return endPoint;
    }

    public void setEndPoint(String endPoint) {
	this.endPoint = endPoint;
    }

    public String getDesc() {
	return desc;
    }

    public void setDesc(String desc) {
	this.desc = desc;
    }

    public String getMethod() {
	return method;
    }

    public void setMethod(String method) {
	this.method = method;
    }

    public Permission getPermission() {
	return permission;
    }

    public void setPermission(Permission permission) {
	this.permission = permission;
    }

}
