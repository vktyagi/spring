package com.dao.impl;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.dao.FailedAttemptDao;
import com.models.FailedAttempt;

@Repository
public class FailedAttemptDaoImpl implements FailedAttemptDao {
    Calendar calendar = Calendar.getInstance();
    java.sql.Timestamp currentTimeStamp = new java.sql.Timestamp(calendar.getTime().getTime());
    @Autowired
    @Qualifier("sessionFactorySecurity")
    private SessionFactory sessionFactorySecurity;

    @Override
    public FailedAttempt markFailedAttempt(FailedAttempt failedAttempt) {
	failedAttempt.setUpdatedAt(currentTimeStamp);
	failedAttempt.setCreatedAt(currentTimeStamp);
	Session session = sessionFactorySecurity.openSession();
	session.save(failedAttempt);
	session.close();
	return failedAttempt;
    }

    @Override
    public FailedAttempt updateFailedAttempt(FailedAttempt failedAttempt) {
	failedAttempt.setUpdatedAt(currentTimeStamp);
	Session session = sessionFactorySecurity.openSession();
	session.beginTransaction();
	session.update(failedAttempt);
	session.getTransaction().commit();
	session.close();
	return failedAttempt;
    }

    @SuppressWarnings("unchecked")
    @Override
    public FailedAttempt getFailedAttempt(String username, String ipAddress) {

	Session session = sessionFactorySecurity.openSession();
	List<FailedAttempt> failedAttempts = session.createQuery("FROM FailedAttempt WHERE username=? AND ipAddress=?").setParameter(0, username).setParameter(1, ipAddress).list();
	session.flush();
	session.close();

	if (failedAttempts.size() > 0) {
	    return failedAttempts.get(0);
	}
	return null;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public FailedAttempt getFailedAttempt(String username) {

	Session session = sessionFactorySecurity.openSession();
	List<FailedAttempt> failedAttempts = session.createQuery("FROM FailedAttempt WHERE username=?").setParameter(0, username).list();
	session.flush();
	session.close();

	if (failedAttempts.size() > 0) {
	    return failedAttempts.get(0);
	}
	return null;
    }
    @Override
    public void removeFailedAttempt(FailedAttempt failedAttempt) {
	Session session = sessionFactorySecurity.openSession();
	session.beginTransaction();
	session.delete(failedAttempt);
	session.getTransaction().commit();
	session.close();
    }
}
