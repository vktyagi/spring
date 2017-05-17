package com.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.rubyeye.xmemcached.MemcachedClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.apigateway.AmazonApiGatewayClient;
import com.amazonaws.services.apigateway.model.GetMethodRequest;
import com.amazonaws.services.apigateway.model.GetMethodResult;
import com.amazonaws.services.apigateway.model.GetResourcesRequest;
import com.amazonaws.services.apigateway.model.GetResourcesResult;
import com.amazonaws.services.apigateway.model.Resource;
import com.dao.ApiEndPointDao;
import com.exception.ResourceNotFoundException;
import com.model.CustomResource;
import com.models.APIEndPoint;
import com.services.ApiEndPointService;
import com.utility.PropertyReader;

@Service("apiEndPointServiceImpl")
public class ApiEndPointServiceImpl implements ApiEndPointService {

    private static final Logger logger = LoggerFactory.getLogger(ApiEndPointServiceImpl.class);
    private static final String INTEGRATION_REQ_HEADER = "integration.request.header.cad-perm-header";
    private static final String REQ_HEADER = "method.request.header.cad-perm-header";
    private static final String SPLITREQ_HEADER = "method.request.header.";

    @Autowired
    ApiEndPointDao apiEndPointDao;

    @Autowired
    PropertyReader propertyReader;

    @Autowired
    MemcachedClient memcacheClient;

    @Autowired
    AmazonApiGatewayClient apiGatewayClient;

    @Override
    public APIEndPoint getApiEndPointInfo(String resourceId, String method) throws Exception {
	return apiEndPointDao.getApiEndPointInfo(resourceId, method);
    }

    public ArrayList<String> getPermissionString(String uri, String method, String restApiId) throws Exception {

	logger.info("Retrieving api-collection for rest-api-id: " + restApiId + " from memchache");
	GetResourcesResult getResourcesResult = memcacheClient.get(restApiId);

	if (null == getResourcesResult) {
	    logger.info("Retrieving api-collection for rest-api-id: " + restApiId + " from aws-api-gateway as it does not exist in memcache");
	    GetResourcesRequest getResourcesRequest = new GetResourcesRequest();
	    getResourcesRequest.withRestApiId(restApiId);
	    getResourcesRequest.setLimit(1000);
	    getResourcesResult = apiGatewayClient.getResources(getResourcesRequest);

	    memcacheClient.add(restApiId, propertyReader.getMemcacheExpirationTimeInSec(), getResourcesResult);
	    logger.info("Added api-collection for rest-api-id: " + restApiId + " into memcache");
	}

	logger.info("Retrieved api-collection for rest-api-id: " + restApiId + " with #" + getResourcesResult.getItems().size() + " endpoints.");

	ArrayList<String> listOfPermissionString = null;
	try {
	    String resourceId = getMatchedResource(getResourcesResult.getItems(), uri);
	    logger.info("resource-id" + resourceId + "postition: " + getResourcesResult.getPosition());
	    if (null == resourceId) {
		logger.error("Resource '" + uri + "' and method '" + method + "' not found!");
		throw new ResourceNotFoundException("Resource '" + uri + "' and method '" + method + "' not found!");
	    }

	    logger.info("Retrieving permission strings for resource-id: " + resourceId + " from memchache");
	    listOfPermissionString = memcacheClient.get(resourceId);

	    if (null == listOfPermissionString) {
		logger.info("Retrieving permission strings for resource-id: " + resourceId + " from aws-api-gateway as it does not exist in memcache");
		listOfPermissionString = getPermission(uri, method, restApiId, resourceId);

		memcacheClient.add(resourceId, propertyReader.getMemcacheExpirationTimeInSec(), listOfPermissionString);
		logger.info("Added permission strings for resource-id: " + resourceId + " into memcache");
	    }

	    logger.info("Retrieved #" + listOfPermissionString.size() + " permission strings for resource-id: " + resourceId + " with method " + method + ".");

	} catch (Exception e) {
	    logger.error("Exception while getting permission for endpoint: " + uri + " and method: ", e);
	    throw e;
	}
	return listOfPermissionString;
    }

    private String getMatchedResource(List<Resource> resources, String endpoint) {

	String[] pathParts = endpoint.split("/");

	CustomResource rootResource = new CustomResource(getParent(resources, pathParts));

	addChildResources(rootResource, resources);

	logger.info("Endpoint " + endpoint + " has #" + pathParts.length + "parts");

	CustomResource matched_r = matchPathPart(pathParts, rootResource, 1);

	if (matched_r != null) {
	    logger.info("matched resource-id: " + matched_r.getId() + " path: " + matched_r.getPath());
	    return matched_r.getId();
	} else {
	    logger.info("No matching resource found for endpoint: " + endpoint);
	    return null;
	}

    }

    private CustomResource matchPathPart(String[] pathParts, CustomResource resource, int pos) {

	if (resource.getChildResources().size() == 0) {
	    if (resource.getPathPart().equalsIgnoreCase("{proxy+}")) {
		// resource is a proxy resourceno furthur match required.
		return resource;
	    } else if (pos == pathParts.length) {
		// resource is leaf resource and is exactly matched with
		// endpoint.
		return resource;
	    } else {
		return null;
	    }
	}

	CustomResource path_child_r = null;
	for (CustomResource child_r : resource.getChildResources()) {
	    if (child_r.getPathPart().equals(pathParts[pos])) {
		if (pos == pathParts.length - 1) {
		    logger.debug("returning match resource: " + child_r.getId());
		    return child_r;
		} else {
		    logger.debug("calling child resource for: " + child_r.getId() + "and pos: " + pos + 1);
		    CustomResource cr = matchPathPart(pathParts, child_r, pos + 1);
		    if (cr != null) {
			return cr;
		    }
		}
	    }

	    if (child_r.getPathPart().startsWith("{")) {
		path_child_r = child_r;
	    }
	}

	if (null != path_child_r) {

	    if (pos == pathParts.length - 1) {
		logger.debug("returning match resource: " + path_child_r.getId());
		return path_child_r;
	    } else {
		logger.debug("calling child resource for: " + path_child_r.getId() + "and pos: " + pos + 1);
		return matchPathPart(pathParts, path_child_r, pos + 1);
	    }

	    /*
	     * logger.info("calling path child resource for: " +
	     * path_child_r.getId() + "and pos: " + pos + 1); return
	     * matchPathPart(pathParts, path_child_r, pos + 1);
	     */
	} else {
	    return null;
	}
    }

    private void addChildResources(CustomResource parent, List<Resource> resources) {

	List<Resource> leftResources = new ArrayList<Resource>();
	List<CustomResource> childResources = new ArrayList<CustomResource>();
	for (Resource resource : resources) {
	    if (parent.getId().equals(resource.getParentId())) {
		CustomResource child = new CustomResource(resource);
		childResources.add(child);
	    } else {
		leftResources.add(resource);
	    }
	}

	if (childResources.size() > 0) {
	    parent.setChildResources(childResources);
	    for (CustomResource child_r : parent.getChildResources()) {
		addChildResources(child_r, leftResources);
	    }
	} else {
	    logger.info("returning for resource-id: " + parent.getId());
	    return;
	}

    }

    private Resource getParent(List<Resource> resources, String[] keyPathArr) {
	for (Resource resource : resources) {
	    if (null == resource.getParentId()) {
		return new CustomResource(resource);
	    }

	}
	return null;
    }

    private ArrayList<String> getPermission(String uri, String method, String restApiId, String resourceId) throws Exception {
	ArrayList<String> listOfRequestParam = new ArrayList<String>();

	GetMethodRequest getMethodRequest = new GetMethodRequest().withRestApiId(restApiId).withResourceId(resourceId).withHttpMethod(method);
	GetMethodResult getMethodResult = apiGatewayClient.getMethod(getMethodRequest);

	Map<String, String> integRequestParamMap = getMethodResult.getMethodIntegration().getRequestParameters();
	Map<String, Boolean> requestParamMap = getMethodResult.getRequestParameters();

	if (integRequestParamMap != null && integRequestParamMap.containsKey(INTEGRATION_REQ_HEADER)) {
	    logger.debug("integRequestParamMap size is" + integRequestParamMap.size());
	    String permissionStringWithQuote = integRequestParamMap.get(INTEGRATION_REQ_HEADER);
	    if (permissionStringWithQuote != null && permissionStringWithQuote.length() > 0) {
		String permissionString = permissionStringWithQuote.replace("'", "");
		String permissionStringSplit[] = permissionString.split("\\$");
		for (String permission : permissionStringSplit) {
		    listOfRequestParam.add(permission);
		}
	    }
	} else {
	    logger.warn("No Permission string found in integration request headers");
	}
	if (requestParamMap != null && listOfRequestParam.size() == 0) {
	    logger.debug("requestParamMap size is" + requestParamMap.size());
	    Set<String> setOfRequestParameters = requestParamMap.keySet();
	    for (String requestParam : setOfRequestParameters) {
		if (requestParam.startsWith(REQ_HEADER)) {
		    String splitReqParam[] = requestParam.split(SPLITREQ_HEADER);
		    String splitReqParamFromDollar[] = splitReqParam[1].split("\\$\\$");
		    if(splitReqParamFromDollar.length>1){
                        String permString[] = splitReqParamFromDollar[1].split("\\$");
                        for (String string : permString) {
                            listOfRequestParam.add(string);
			}
		    }
		} else {
		    logger.warn("Permission string in request header is not associated");
		}
	    }
	}
	
	return listOfRequestParam;
    }
}
