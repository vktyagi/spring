package com.model;

import java.io.Serializable;

public class Attributes implements Serializable {
    private static final long serialVersionUID = 1L;
    private String verb;
    private String path;

    public String getVerb() {
	return verb;
    }

    public void setVerb(String verb) {
	this.verb = verb;
    }

    public String getPath() {
	return path;
    }

    public void setPath(String path) {
	this.path = path;
    }

}
