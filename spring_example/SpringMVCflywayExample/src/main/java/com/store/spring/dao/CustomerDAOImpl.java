package com.store.spring.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.store.model.Customer;

/**
 * An implementation of the ContactDAO interface.
 * @author www.codejava.net
 *
 */
public class CustomerDAOImpl implements CustomerDAO {

	private JdbcTemplate jdbcTemplate;
	
	public CustomerDAOImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public void saveOrUpdate(Customer contact) {
		if (contact.getId() > 0) {
			// update
			String sql = "UPDATE customer SET name=?, email=?, address=?, "
						+ "telephone=? WHERE cust_id=?";
			jdbcTemplate.update(sql, contact.getName(), contact.getEmail(),
					contact.getAddress(), contact.getTelephone(), contact.getId());
		} else {
			// insert
			String sql = "INSERT INTO customer (name, email, address, telephone)"
						+ " VALUES (?, ?, ?, ?)";
			jdbcTemplate.update(sql, contact.getName(), contact.getEmail(),
					contact.getAddress(), contact.getTelephone());
		}
		
	}

	@Override
	public void delete(int contactId) {
		String sql = "DELETE FROM customer WHERE cust_id=?";
		jdbcTemplate.update(sql, contactId);
	}

	@Override
	public List<Customer> list() {
		String sql = "SELECT * FROM customer";
		List<Customer> listContact = jdbcTemplate.query(sql, new RowMapper<Customer>() {

			@Override
			public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
				Customer aContact = new Customer();
	
				aContact.setId(rs.getInt("cust_id"));
				aContact.setName(rs.getString("name"));
				aContact.setEmail(rs.getString("email"));
				aContact.setAddress(rs.getString("address"));
				aContact.setTelephone(rs.getString("telephone"));
				
				return aContact;
			}
			
		});
		
		return listContact;
	}

	@Override
	public Customer get(int contactId) {
		String sql = "SELECT * FROM customer WHERE cust_id=" + contactId;
		return jdbcTemplate.query(sql, new ResultSetExtractor<Customer>() {

			@Override
			public Customer extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					Customer contact = new Customer();
					contact.setId(rs.getInt("cust_id"));
					contact.setName(rs.getString("name"));
					contact.setEmail(rs.getString("email"));
					contact.setAddress(rs.getString("address"));
					contact.setTelephone(rs.getString("telephone"));
					return contact;
				}
				
				return null;
			}
			
		});
	}

}
