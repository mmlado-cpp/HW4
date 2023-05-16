package persistence;

import java.sql.Date;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import domain.Address;
import domain.Customer;
import domain.Order;

public class OrderDataAccess {
	public static boolean addOrder(int number,  Date date, String item, double price, int customerId) {
		SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Customer.class).addAnnotatedClass(Address.class).addAnnotatedClass(Order.class).buildSessionFactory();
		boolean flag = false;
		Session session = factory.getCurrentSession();
		
		try {
			session.beginTransaction();
			
			Order tempOrder = new Order(number, date, item, price);
			Customer tempCustomer = session.get(Customer.class, customerId);
			tempCustomer.addOrder(tempOrder);
			
			session.save(tempOrder);
			session.save(tempCustomer);
			
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
	
	public static Order searchOrder(int number) {
		SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Customer.class).addAnnotatedClass(Address.class).addAnnotatedClass(Order.class).buildSessionFactory();
		Order order = null;
		Session session = factory.getCurrentSession();
		
		try {
			session.beginTransaction();
			
			order = session.get(Order.class, number);
			
			session.getTransaction().commit();
		} catch(Exception e) {
			 System.out.println("Problem creating session factory");
		     e.printStackTrace();
		} finally {
			factory.close();
		
		}
		return order;
	}
	
	public static boolean updateOrder(int id, Date date, String item, double price, int customerId) {
		SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Customer.class).addAnnotatedClass(Address.class).addAnnotatedClass(Order.class).buildSessionFactory();
		boolean flag = false;
		Session session = factory.getCurrentSession();
		
		try {
			session.beginTransaction();
			
			Order tempOrder = session.get(Order.class, id);
			Customer oldCustomer = tempOrder.getCustomer();
			oldCustomer.removeOrder(tempOrder);
			Customer tempCustomer = session.get(Customer.class, customerId);
			tempCustomer.addOrder(tempOrder);
			
			tempOrder.setDate(date);
			tempOrder.setItem(item);
			tempOrder.setPrice(price);
			
			session.save(tempOrder);
			session.save(oldCustomer);
			session.save(tempCustomer);
			
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
	
	public static boolean deleteOrder(int id) {
		SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Customer.class).addAnnotatedClass(Address.class).addAnnotatedClass(Order.class).buildSessionFactory();
		boolean flag = false;
		Session session = factory.getCurrentSession();
		
		try {
		session.beginTransaction();
		
		Order tempOrder = session.get(Order.class, id);
		Customer tempCustomer = tempOrder.getCustomer();
		tempCustomer.removeOrder(tempOrder);
		
		session.save(tempCustomer);
		
		session.delete(tempOrder);
		
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
}