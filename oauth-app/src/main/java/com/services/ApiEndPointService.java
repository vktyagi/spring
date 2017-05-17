package com.services;

import java.util.ArrayList;

import com.models.APIEndPoint;

public interface ApiEndPointService {

    public APIEndPoint getApiEndPointInfo(String resourceId, String method) throws Exception;

    public ArrayList<String> getPermissionString(String uri, String method, String restApiId) throws Exception;
}
