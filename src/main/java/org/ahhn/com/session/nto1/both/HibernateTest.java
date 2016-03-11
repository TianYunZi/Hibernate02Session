package org.ahhn.com.session.nto1.both;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

/**
 * Created by XJX on 2016/3/6.
 */
public class HibernateTest {

	private SessionFactory sessionFactory;
	private Session session;
	private Transaction transaction;

	@Before
	public void init() {
		Configuration configuration = new Configuration().configure();
		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
		sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
	}

	@After
	public void destroy() {
		transaction.commit();
		session.close();
		sessionFactory.close();
	}

	@Test
	public void testOrderBy() {
		Customer customer = (Customer) session.get(Customer.class, 1);
		Set<Order> orders = customer.getOrders();
		System.out.println(orders.iterator().next().getOrderName());
	}

	@Test
	public void testCascade() {
		Customer customer = (Customer) session.get(Customer.class, 2);
		//session.delete(customer);

		customer.getOrders().clear();
	}

	@Test
	public void testUpdate() {
		Customer customer = (Customer) session.get(Customer.class, 1);
		customer.getOrders().iterator().next().setOrderName("MBA");
	}

	@Test
	public void testOneToManyGet() {
		//1. 对 n 的一端的集合使用延迟加载
		Customer customer = (Customer) session.get(Customer.class, 1);
		System.out.println(customer.getCustomerName());

		//2. 返回的多的一端的集合时 Hibernate 内置的集合类型.
		//该类型具有延迟加载和存放代理对象的功能.
		System.out.println(customer.getOrders().getClass());

		//session.close();
		//3. 可能会抛出 LazyInitializationException 异常
		System.out.println(customer.getOrders().size());
		//4. 再需要使用集合中元素的时候进行初始化.

	}

	@Test
	public void testBidirection() {
		Customer customer = new Customer();
		customer.setCustomerName("CC");

		Order order1 = new Order();
		order1.setOrderName("ORDER-5");
		order1.setCustomer(customer);

		Order order2 = new Order();
		order2.setOrderName("ORDER-6");
		order2.setCustomer(customer);

		customer.getOrders().add(order1);
		customer.getOrders().add(order2);

		session.save(customer);
//		session.save(order1);
//		session.save(order2);

		//执行  save 操作: 先插入 Customer, 再插入 Order, 3 条 INSERT, 2 条 UPDATE
		//因为 1 的一端和 n 的一端都维护关联关系. 所以会多出 UPDATE
		//可以在 1 的一端的 set 节点指定 inverse=true, 来使 1 的一端放弃维护关联关系!
		//建议设定 set 的 inverse=true, 建议先插入 1 的一端, 后插入多的一端
		//好处是不会多出 UPDATE 语句

		//		session.save(order1);
//		session.save(order2);

		//先插入 Order, 再插入 Cusomer, 3 条 INSERT, 4 条 UPDATE
//		session.save(order1);
//		session.save(order2);
//
//		session.save(customer);
	}
}
