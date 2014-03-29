package com.wl.hibernate;

import org.hibernate.Session;

import junit.framework.Assert;
import junit.framework.TestCase;

public class SessionTest extends TestCase {

	public void testHello1(){
		
		System.out.println("-----test-----");
		Assert.assertEquals("hello", "hello");
	}
	
	
	public void testReadByGetMethod(){
		Session session = null;
		try{
			session = HibernateUtils.getSession();
			session.beginTransaction();
			User user = (User)session.get(User.class, "402880ac450380b901450380baa60001");
			System.out.println("user.name" + user.getName());
			
			user.setName("龙哥");
			session.getTransaction().commit();
		}catch(Exception e){
			e.printStackTrace();
			session.getTransaction().rollback();
		}finally{
			HibernateUtils.closeSession(session);
		}
	}
}
