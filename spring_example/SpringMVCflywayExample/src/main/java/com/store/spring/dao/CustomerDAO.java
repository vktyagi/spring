package com.store.spring.dao;

import java.util.List;

import com.store.model.Customer;

/**
 * Defines DAO operations for the contact model.
 * @author www.codejava.net
 *
 */
public interface CustomerDAO {
	
	public void saveOrUpdate(Customer contact);
	
	public void delete(int contactId);
	
	public Customer get(int contactId);
	
	public List<Customer> list();
}
