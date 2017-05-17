package com.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.dao.ApiEndPointDao;
import com.models.APIEndPoint;

@Repository
public class ApiEndPointDaoImpl implements ApiEndPointDao {
    @Autowired
    @Qualifier("sessionFactorySecurity")
    private SessionFactory sessionFactorySecurity;

    @SuppressWarnings("unchecked")
    @Override
    public APIEndPoint getApiEndPointInfo(String uri, String method) throws Exception {
	Query query = null;
	List<APIEndPoint> listOfApiEndPoints = new ArrayList<APIEndPoint>();
	Session session = sessionFactorySecurity.openSession();
	query = session.createQuery("from APIEndPoint where endPoint=? and method=?").setParameter(0, uri).setParameter(1, method);
	listOfApiEndPoints = query.list();
	if (listOfApiEndPoints.size() > 0) {
	    return listOfApiEndPoints.get(0);
	} else {
	    String resources[] = uri.split("/");
	    String modifiedUri = "/" + resources[1] + "/" + resources[2] + "/" + resources[3] + "/%";
	    String q="from APIEndPoint where endPoint like'"+modifiedUri+"' and method='"+method+"'";
	    query = session.createQuery(q);
	    listOfApiEndPoints = query.list();
	    if (listOfApiEndPoints.size() > 0) {
		return listOfApiEndPoints.get(0);
	    } else {
		return null;
	    }
	}

    }

}
