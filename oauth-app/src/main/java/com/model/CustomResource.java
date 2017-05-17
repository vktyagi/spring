package com.model;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.apigateway.model.Resource;

public class CustomResource extends Resource {

    private static final long serialVersionUID = 1L;

    List<CustomResource> childResources;

    public CustomResource(Resource resource) {
	this.setId(resource.getId());
	this.setParentId(resource.getParentId());
	this.setPath(resource.getPath());
	this.setPathPart(resource.getPathPart());
    }

    public List<CustomResource> getChildResources() {
        if(null == childResources) {
            return new ArrayList<CustomResource>();
        }
	return childResources;
    }

    public void setChildResources(List<CustomResource> childResources) {
        this.childResources = childResources;
    }
    
}
