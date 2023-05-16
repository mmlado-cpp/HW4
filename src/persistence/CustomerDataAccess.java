package persistence;

import java.util.List;
import java.util.ArrayList;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import domain.Address;
import domain.Customer;
import domain.Order;

public class CustomerDataAccess {
	public static boolean addCustomer(String name, String phone, String email, Address address) {
		SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Customer.class).addAnnotatedClass(Address.class).addAnnotatedClass(Order.class).buildSessionFactory();
		boolean flag = false;
		Session session = factory.getCurrentSession();
		
		try {
			session.beginTransaction();
			
			Customer tempCustomer = new Customer(name, phone, email, address);
			
			session.save(tempCustomer);
			session.save(address);
			
			session.getTransaction().commit();
			
			flag = true;
		} catch(Exception e) {
			 System.out.println("Problem creating session factory");
		     e.printStackTrace();
		} finally {
			factory.close();
		
		}
		return flag;
	}
	
	public static Customer searchCustomer(String name) {
		SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Customer.class).addAnnotatedClass(Address.class).addAnnotatedClass(Order.class).buildSessionFactory();
		Customer customer = null;
		Session session = factory.getCurrentSession();
		
		try {
			session.beginTransaction();
			
		    List<Customer> customers = getAllCustomers();
			
		    for(int i = 0; i < customers.size(); i++) {
		    	if(customers.get(i).getName().toUpperCase().startsWith(name.toUpperCase())) {
		    		customer = customers.get(i);
		    		break;
		    	}
		    }
		    
		    session.getTransaction().commit();
			
		} catch(Exception e) {
			 System.out.println("Problem creating session factory");
		     e.printStackTrace();
		} finally {
			factory.close();
		}
		return customer;
	}
	
	public static boolean updateCustomer(int id, String name, String phone, String email, Address address) {
		SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Customer.class).addAnnotatedClass(Address.class).addAnnotatedClass(Order.class).buildSessionFactory();
		boolean flag = false;
		Session session = factory.getCurrentSession();
		
		try {
			session.beginTransaction();
			
			Customer tempCustomer = session.get(Customer.class, id);
			Address tempAddress = tempCustomer.getAddress();
			
			tempCustomer.setName(name);
			tempCustomer.setPhone(phone);
			tempCustomer.setEmail(email);
			tempCustomer.setAddress(address);
			
			tempAddress.setStreet(address.getStreet());
			tempAddress.setCity(address.getCity());
			tempAddress.setState(address.getState());
			tempAddress.setZipCode(address.getZipCode());
			
			session.save(tempCustomer);
			session.save(tempAddress);
			
			session.getTransaction().commit();
			
			flag = true;
		} catch(Exception e) {
			 System.out.println("Problem creating session factory");
		     e.printStackTrace();
		} finally {
			factory.close();
		
		}
		return flag;
	}
	
	public static boolean deleteCustomer(int id) {
		SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Customer.class).addAnnotatedClass(Address.class).addAnnotatedClass(Order.class).buildSessionFactory();
		boolean flag = false;
		Session session = factory.getCurrentSession();
		
		try {
		session.beginTransaction();
		
		Customer tempCustomer = session.get(Customer.class, id);
		
		if(tempCustomer.getOrders() == null) {
			tempCustomer.setOrders(new ArrayList<Order>());
		}
		
		session.delete(tempCustomer);
		
		session.getTransaction().commit();
		
		flag = true;
		} catch(Exception e) {
			System.out.println("Problem creating session factory");
			e.printStackTrace();
		} finally {
			factory.close();
		}
		return flag;
	}
	
	public static List<Customer> getAllCustomers(){
		SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Customer.class).addAnnotatedClass(Address.class).addAnnotatedClass(Order.class).buildSessionFactory();
		List<Customer> customers = new ArrayList<Customer>();
		Session session = factory.getCurrentSession();
		
		try {
			session.beginTransaction();
			
			CriteriaBuilder builder = session.getCriteriaBuilder();
		    CriteriaQuery<Customer> criteria = builder.createQuery(Customer.class);
		    criteria.from(Customer.class);
		    customers = session.createQuery(criteria).getResultList();
		    session.getTransaction().commit();
		
		} catch(Exception e) {
			System.out.println("Problem creating session factory");
			e.printStackTrace();
		} finally {
			factory.close();
		}
		return customers;
	}
}