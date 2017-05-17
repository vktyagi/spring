package com.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.dao.OauthCustomDao;
import com.models.User;

@Repository
public class OauthCustomDaoImpl implements OauthCustomDao {
    @Autowired
    @Qualifier("sessionFactorySecurity")
    private SessionFactory sessionFactorySecurity;

    @SuppressWarnings("unchecked")
    @Override
    public User findByUserName(String uName) {
	List<User> users = new ArrayList<User>();
	Session session = sessionFactorySecurity.openSession();
	users = session.createQuery("from User where userName=?").setParameter(0, uName.trim()).list();
	session.close();
	if (users.size() > 0) {
	    return users.get(0);
	} else {
	    return null;
	}
    }

    @Override
    public void update(User user) {
	Session session = sessionFactorySecurity.openSession();
	session.beginTransaction();
	session.update(user);
	session.getTransaction().commit();
	session.close();
    }

   /* public List<Permission> getPermissionsByRole(Role role) {

	Session session = sessionFactorySecurity.openSession();

	List<Permission> permissions = session.createQuery("SELECT r.permission FROM Role r WHERE  r.umRoleId = ?").setParameter(0, role.getRoleId()).list();
	// String qu =
	// "SELECT r.permission FROM Role r JOIN FETCH Permission p WHERE  r.umRoleId = ?")

	
	 * String q =
	 * "SELECT p.id, p.permission FROM role_permissions rp, permission p WHERE rp.role_id=? AND rp.permissions_id=p.id"
	 * ;
	 * 
	 * List<Object> list = session.createSQLQuery(q).setParameter(0,
	 * role.getRoleId()).list();
	 * 
	 * List<Permission> permissions = new ArrayList<Permission>();
	 

	session.close();

	return permissions;
    }*/

    @Override
    public int removeActiveTokenForUser(String uName) throws Exception{
	Session session = sessionFactorySecurity.openSession();
	Query sqlQuery = session.createSQLQuery("delete from oauth_access_token where user_name=?").setParameter(0, uName);
	int res = sqlQuery.executeUpdate();
	session.close();
	return res;
    }
}
