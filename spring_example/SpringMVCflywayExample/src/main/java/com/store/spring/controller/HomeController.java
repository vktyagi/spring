package com.store.spring.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.store.model.Customer;
import com.store.spring.dao.CustomerDAO;

/**
 * @author vtyagi
 *
 */
@Controller
public class HomeController {

	@Autowired
	private CustomerDAO CustomerDAO;
	
	@RequestMapping(value="/")
	public ModelAndView listCustomer(ModelAndView model) throws IOException{
		List<Customer> listCustomer = CustomerDAO.list();
		model.addObject("listCustomer", listCustomer);
		model.setViewName("customerList");
		
		return model;
	}
	
	@RequestMapping(value = "/newCustomer", method = RequestMethod.GET)
	public ModelAndView newCustomer(ModelAndView model) {
		Customer customer = new Customer();
		model.addObject("customer", customer);
		model.setViewName("CustomerForm");
		return model;
	}
	
	@RequestMapping(value = "/saveCustomer", method = RequestMethod.POST)
	public ModelAndView saveCustomer(@ModelAttribute Customer Customer) {
		CustomerDAO.saveOrUpdate(Customer);		
		return new ModelAndView("redirect:/");
	}
	
	@RequestMapping(value = "/deleteCustomer", method = RequestMethod.GET)
	public ModelAndView deleteCustomer(HttpServletRequest request) {
		int CustomerId = Integer.parseInt(request.getParameter("id"));
		CustomerDAO.delete(CustomerId);
		return new ModelAndView("redirect:/");
	}
	
	@RequestMapping(value = "/editCustomer", method = RequestMethod.GET)
	public ModelAndView editCustomer(HttpServletRequest request) {
		int CustomerId = Integer.parseInt(request.getParameter("id"));
		Customer Customer = CustomerDAO.get(CustomerId);
		ModelAndView model = new ModelAndView("CustomerForm");
		model.addObject("customer", Customer);
		
		return model;
	}
}
