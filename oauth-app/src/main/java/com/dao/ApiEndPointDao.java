package com.dao;

import com.models.APIEndPoint;

public interface ApiEndPointDao {
public APIEndPoint getApiEndPointInfo(String resourceId,String method)throws Exception;
}
