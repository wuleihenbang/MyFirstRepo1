package com.wl.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtils {
	
	private static SessionFactory factory;
	
	static{
		try{
			Configuration cfg = new Configuration();
			factory = cfg.configure().buildSessionFactory();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static SessionFactory getSessionFactory(){
		return factory;
	}
	
	public static Session getSession(){
		return factory.openSession();
	}
	
	public static void closeSession(Session session){
		if(session != null){
			if(session.isOpen()){
				session.close();
			}
		}
	}

}
