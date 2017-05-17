package com.services.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.stereotype.Service;

import com.constant.UserAttributes;
import com.dao.OauthCustomDao;
import com.exception.PermissionDeniedException;
import com.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.models.APIEndPoint;
import com.models.ExceptionModel;
import com.models.Permission;
import com.models.Role;
import com.models.User;
import com.services.ApiEndPointService;
import com.services.ApiService;
import com.utility.PropertyReader;

@Service("apiService")
public class ApiServiceImpl implements ApiService {
    private static final Logger logger = LoggerFactory.getLogger(ApiServiceImpl.class);
    private static final String CAD_ROLE_ID = "cad_role_id_";
    @Autowired
    PropertyReader propertyReader;
    @Autowired
    OauthCustomDao oauthCustomDao;
    @Autowired
    ApiEndPointService apiEndPointService;
    ExceptionModel em = new ExceptionModel();

    @Autowired
    CustomTokenService tokenServices;

    @Override
    public String creatJWTToken(User user) throws Exception {

	Map<String, Object> claims = new HashMap<String, Object>();
	Map<String, Object> header = new HashMap<String, Object>();
	String headers = "";
	String payload = "";
	if (null != user) {
	    logger.info("Generating JWT for user: " + user.getUserName());
	    header.put("alg", "NONE");
	    header.put("typ", "JWT");
	    claims.put(UserAttributes.ORGANIZATION.toString(), "xyz");
	    claims.put(UserAttributes.ROLE.toString(), getPermissionFromRole(user, user.getRole(), user.getMarketPermissions()));
	    claims.put(UserAttributes.APPLICATIONTIER.toString(), "Unlimited");
	    claims.put(UserAttributes.VERSION.toString(), "v1.0");
	    claims.put(UserAttributes.ISS.toString(), "wso2.org/products/am");
	    claims.put(UserAttributes.APPLICATIONNAME.toString(), "XYZAPPName");
	    claims.put(UserAttributes.ENDUSER.toString(), user.getUserName() + "@carbon.super");
	    claims.put(UserAttributes.ENDUSERTENANTID.toString(), null);
	    claims.put(UserAttributes.GIVENNAME.toString(), user.getfName());
	    claims.put(UserAttributes.SUBSCRIBER.toString(), "integration_test_user");
	    claims.put(UserAttributes.TIER.toString(), null);
	    claims.put(UserAttributes.EMAIL.toString(), user.getEmail());
	    claims.put(UserAttributes.LASTNAME.toString(), user.getlName());
	    claims.put(UserAttributes.FIRSTNAME.toString(), user.getfName());
	    claims.put(UserAttributes.EXP.toString(), Instant.now().plus(10L, ChronoUnit.DAYS).getEpochSecond());
	    claims.put(UserAttributes.APPLICATIONID.toString(), "6");
	    claims.put(UserAttributes.USERTYPE.toString(), "APPLICATION_USER");
	    claims.put(UserAttributes.APICONTEXT.toString(), "/cm/v1.0");
	    claims.put(UserAttributes.CUSTOMDATA.toString(), null != user.getCustomInfo() ? user.getCustomInfo() : "{}");
	    claims.put(UserAttributes.LOCATION.toString(), user.getLocation());
	    claims.put(UserAttributes.KEYTYPE.toString(), "PRODUCTION");
	    ObjectMapper mapper = new ObjectMapper();
	    Base64.Encoder encoder = Base64.getEncoder();
	    headers = encoder.encodeToString(mapper.writeValueAsBytes(header));
	    payload = encoder.encodeToString(mapper.writeValueAsBytes(claims));
	}
	logger.info("Generated JWT for user: " + user.getUserName());
	return headers + "." + payload + ".";
    }

    @SuppressWarnings("unused")
    private String getPermissionString(Set<Permission> permissions) {
	List<String> permissionsList = new ArrayList<String>();
	if (permissions.size() > 0) {
	    for (Permission p : permissions) {
		permissionsList.add(p.getName());
	    }
	}
	String permissionString = StringUtils.join(permissionsList.toArray(), ',');
	return permissionString;
    }

    private String getPermissionFromRole(User user, Set<Role> roles, Set<Permission> marketPermissions) {
	Set<String> permissionsSet = new HashSet<String>();
	/*
	 * if (!user.isUmV2()) { for (Permission p : user.getPermissions()) {
	 * permissionsSet.add(p.getName()); } } else {
	 */
	logger.info("Associating Role's permissions to JWT");
	if (roles.size() > 0) {
	    for (Role role : roles) {
		for (Permission p : role.getPermissions()) {
		    permissionsSet.add(p.getName());
		}
		permissionsSet.add(CAD_ROLE_ID+role.getId());
	    }
	    // }
	}
	logger.info("Associating market permissions to JWT");
	if (null != marketPermissions && marketPermissions.size() > 0) {
	    for (Permission p : marketPermissions) {
		permissionsSet.add(p.getName());
	    }
	}
	String permissionString = StringUtils.join(permissionsSet.toArray(), ',');
	return permissionString;
    }

    // This method will be of no use
    public Object getJWT1(HttpServletRequest request, String uName, String method, String resourceId) throws Exception {
	ExceptionModel em = null;
	String jwtToken = null;
	boolean flag = false;
	try {
	    em = new ExceptionModel();
	    APIEndPoint apiEndPoint = apiEndPointService.getApiEndPointInfo(resourceId, method);
	    if (null == apiEndPoint) {
		throw new ResourceNotFoundException("Resource '" + resourceId + "' and method '" + method + "' not found!");
	    }
	    User user = oauthCustomDao.findByUserName(uName);

	    if (null == apiEndPoint.getPermission()) {
		em.setStatus("200");
		request.setAttribute("em", em);
		return jwtToken = creatJWTToken(user);
	    }
	    // added role hierarchy
	    /* if (user.isUmV2()) { */
	    for (Role role : user.getRole()) {
		for (Permission permission : role.getPermissions()) {
		    if (permission.getName().equalsIgnoreCase(apiEndPoint.getPermission().getName())) {
			flag = true;
			break;
		    }
		}
	    }
	    /*
	     * } else { for (Permission permission : user.getPermissions()) { if
	     * (
	     * permission.getName().equalsIgnoreCase(apiEndPoint.getPermission()
	     * .getName())) { flag = true; break; } } }
	     */

	    if (flag) {
		jwtToken = creatJWTToken(user);
		em.setStatus("200");
		request.setAttribute("em", em);
	    } else {
		throw new PermissionDeniedException("User " + uName + " does not have permissions associated with endpoint: " + apiEndPoint + " and method: " + method);
	    }

	} catch (Exception e) {
	    logger.error("Exception: " + e.getMessage());
	    em.setStatus("500");
	    em.setException("unknown exception");
	    em.setError(e.toString());
	    request.setAttribute("em", em);
	    return new ObjectMapper().writeValueAsString(em);
	}
	return jwtToken;
    }

    @Override
    public Object getJWT(HttpServletRequest request, String uName, String resourceId, String oAuthToken) throws Exception {
	ExceptionModel em = null;
	String jwtToken = null;
	try {
	    boolean flag = false;
	    em = new ExceptionModel();
	    // get method arn and find path, restapiId from it.
	    String[] arnPartials = resourceId.split(":");
	    String[] apiGatewayArnPartials = arnPartials[5].split("/", 4);
	    String restApiId = apiGatewayArnPartials[0];
	    String httpMethod = apiGatewayArnPartials[2];
	    String resource = "";
	    if (apiGatewayArnPartials.length == 4) {
		resource = "/" + apiGatewayArnPartials[3];
	    }

	    logger.info("Request for Resource " + resource + " and method '" + httpMethod + " and rest API id is :" + restApiId + " for username: " + uName);

	    logger.info("Retrieving the list of permissions strings for endpoint: " + resource + " and method: " + httpMethod);
	    ArrayList<String> permissionString = apiEndPointService.getPermissionString(resource, httpMethod, restApiId);
	    logger.info("Retrieved the #" + permissionString.size() + " permissions strings on endpoint: " + resource + " and method: " + httpMethod);
	    logger.info("Finding the role and permissions for user:" + uName);
	    User user = oauthCustomDao.findByUserName(uName);

	    if (user == null) {
		logger.error("Cannot retrieve role and permissions for user: " + uName + " as user does not exist.");
		logger.info("Revoking token for non existing user " + uName);
		tokenServices.revokeToken(oAuthToken);
		logger.info("Revoked token for non existing user " + uName);
		throw new UnauthorizedUserException("Unauthorized");
	    }

	    if (permissionString.size() == 0) {
		em.setStatus("200");
		request.setAttribute("em", em);
		return jwtToken = creatJWTToken(user);
	    }
	    // added role hierarchy
	    /* if (user.isUmV2()) { */
	    for (Role role : user.getRole()) {
		for (Permission p : role.getPermissions()) {
		    logger.debug("permission: " + p.getName());
		    if (permissionString.contains(p.getName())) {
			flag = true;
			break;
		    }
		}
	    }
	    /*
	     * } else { for (Permission p : user.getPermissions()) {
	     * logger.debug("permission: " + p.getName()); if
	     * (permissionString.contains(p.getName())) { flag = true; break; }
	     * } }
	     */

	    if (flag) {

		jwtToken = creatJWTToken(user);
		em.setStatus("200");
		request.setAttribute("em", em);
	    } else {
		throw new PermissionDeniedException("User " + uName + " does not have permissions associated with endpoint: " + resource + " and method: " + httpMethod);
	    }

	} catch (Exception e) {
	    logger.error("Error while getting JWT for user " + uName + " and resource: " + resourceId + " Exception: " + e.getMessage());
	    throw (e);
	}
	return jwtToken;
    }

}
