package com.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PERMISSIONS")
public class Permission{
    @Id
    @Column(name ="ID")
    Integer id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "CREATED_BY")
    private String createdBy;
    /*@Column(name = "CREATED_AT")
    private Timestamp createdAt;*/
    @Column(name = "UPDATED_BY")
    private String updatedBy;
   /* @Column(name = "UPDATED_AT")
    private Timestamp updatedAt;*/

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getCreatedBy() {
	return createdBy;
    }

    public void setCreatedBy(String createdBy) {
	this.createdBy = createdBy;
    }

    /*public Timestamp getCreatedAt() {
	return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
	this.createdAt = createdAt;
    }*/

    public String getUpdatedBy() {
	return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
	this.updatedBy = updatedBy;
    }

   /* public Timestamp getUpdatedAt() {
	return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
	this.updatedAt = updatedAt;
    }*/

}
