package org.ahhn.com.session;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by XJX on 2016/3/6.
 */
public class HibernateTest {

	private SessionFactory sessionFactory;
	private Session session;
	private Transaction transaction;

	@Before
	public void init(){
		Configuration configuration=new Configuration().configure();
		ServiceRegistry serviceRegistry=new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
		sessionFactory=configuration.buildSessionFactory(serviceRegistry);
		session=sessionFactory.openSession();
		transaction=session.beginTransaction();
	}

	@After
	public void destroy(){
		transaction.commit();
		session.close();
		sessionFactory.close();
	}

	@Test
	public void testSessionCache(){
		Books books=(Books)session.get(Books.class,"1002");
		System.out.println(books);

		Books books2=(Books)session.get(Books.class,"1002");
		System.out.println(books2);
	}
}
