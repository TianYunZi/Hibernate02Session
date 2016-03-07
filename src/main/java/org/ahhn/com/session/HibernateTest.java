package org.ahhn.com.session;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.jdbc.Work;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

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
	public void testPropertyUpdate(){
		Books books1 = (Books) session.get(Books.class, "1004");
		System.out.println(books1.getNamePrice());
	}

	@Test
	public void testDynamicUpdate(){
		Books books1 = (Books) session.get(Books.class, "1004");
		books1.setBookName("Scala");
	}

	@Test
	public void testDoWork(){
		session.doWork(new Work() {
			public void execute(Connection connection) throws SQLException {
				System.out.println(connection);
			}
		});
	}

	/**
	 * evict: 从 session 缓存中把指定的持久化对象移除
	 */
	@Test
	public void testEvict() {
		Books books1 = (Books) session.get(Books.class, "1004");
		Books books2 = (Books) session.get(Books.class, "1005");

		books1.setBookName("Scala");
		books2.setBookName("Node.js");

		session.evict(books1);
	}

	/**
	 * delete: 执行删除操作. 只要 OID 和数据表中一条记录对应, 就会准备执行 delete 操作
	 * 若 OID 在数据表中没有对应的记录, 则抛出异常
	 * <p>
	 * 可以通过设置 hibernate 配置文件 hibernate.use_identifier_rollback 为 true,
	 * 使删除对象后, 把其 OID 置为  null
	 */
	@Test
	public void testDelete() {

		Books books = (Books) session.get(Books.class, "1006");
		session.delete(books);

		System.out.println(books);
	}

	/**
	 * 注意:
	 * 1. 若 OID 不为 null, 但数据表中还没有和其对应的记录. 会抛出一个异常.
	 * 2. 了解: OID 值等于 id 的 unsaved-value 属性值的对象, 也被认为是一个游离对象
	 */
	@Test
	public void testSaveOrUpdate() {
		Books news = new Books("1005", "C", 90);

		session.saveOrUpdate(news);
	}

	/**
	 * update:
	 * 1. 若更新一个持久化对象, 不需要显示的调用 update 方法. 因为在调用 Transaction
	 * 的 commit() 方法时, 会先执行 session 的 flush 方法.
	 * 2. 更新一个游离对象, 需要显式的调用 session 的 update 方法. 可以把一个游离对象
	 * 变为持久化对象
	 * <p>
	 * 需要注意的:
	 * 1. 无论要更新的游离对象和数据表的记录是否一致, 都会发送 UPDATE 语句.
	 * 如何能让 updat 方法不再盲目的出发 update 语句呢 ? 在 .hbm.xml 文件的 class 节点设置
	 * select-before-update=true (默认为 false). 但通常不需要设置该属性.
	 * <p>
	 * 2. 若数据表中没有对应的记录, 但还调用了 update 方法, 会抛出异常
	 * <p>
	 * 3. 当 update() 方法关联一个游离对象时,
	 * 如果在 Session 的缓存中已经存在相同 OID 的持久化对象, 会抛出异常. 因为在 Session 缓存中
	 * 不能有两个 OID 相同的对象!
	 */
	@Test
	public void testUpdate() {
		Books books = (Books) session.get(Books.class, "1002");

		transaction.commit();
		session.close();

		session = sessionFactory.openSession();
		transaction = session.beginTransaction();

		//books.setBookName("Oracle");

		session.update(books);
	}

	/**
	 * get VS load:
	 * <p>
	 * 1. 执行 get 方法: 会立即加载对象.
	 * 执行 load 方法, 若不适用该对象, 则不会立即执行查询操作, 而返回一个代理对象
	 * <p>
	 * get 是 立即检索, load 是延迟检索.
	 * <p>
	 * 2. load 方法可能会抛出 LazyInitializationException 异常: 在需要初始化
	 * 代理对象之前已经关闭了 Session
	 * <p>
	 * 3. 若数据表中没有对应的记录, Session 也没有被关闭.
	 * get 返回 null
	 * load 若不使用该对象的任何属性, 没问题; 若需要初始化了, 抛出异常.
	 */
	@Test
	public void testLoad() {
		Books books = (Books) session.load(Books.class, "1002");
		//session.close();
		System.out.println(books + ": " + books.getClass().getName());
	}

	@Test
	public void testGet() {
		Books books = (Books) session.get(Books.class, "1002");
		//session.close();
		System.out.println(books + ": " + books.getClass().getName());
	}

	/**
	 * persist(): 也会执行 INSERT 操作
	 * <p>
	 * 和 save() 的区别 :
	 * 在调用 persist 方法之前, 若对象已经有 id 了, 则不会执行 INSERT, 而抛出异常
	 */
	@Test
	public void testPersist() {
		Books books = new Books();
		books.setIsbn("1004");
		books.setBookName("C++");
		books.setPrice(130);
		session.persist(books);
	}

	/**
	 * 1. save() 方法
	 * 1). 使一个临时对象变为持久化对象
	 * 2). 为对象分配 ID.
	 * 3). 在 flush 缓存时会发送一条 INSERT 语句.
	 * 4). 在 save 方法之前的 id 是无效的
	 * 5). 持久化对象的 ID 是不能被修改的!
	 */
	@Test
	public void testSave() {
		Books books = new Books();
		books.setIsbn("1003");
		books.setBookName("C++");
		books.setPrice(130);
		System.out.println(books);
		session.save(books);
		System.out.println(books);
	}

	//refresh(): 会强制发送 SELECT 语句, 以使 Session 缓存中对象的状态和数据表中对应的记录保持一致!
	@Test
	public void testReflush() {
		Books books = (Books) session.get(Books.class, "1002");
		System.out.println(books);

		session.refresh(books);
		System.out.println(books);
	}

	/**
	 * flush: 使数据表中的记录和 Session 缓存中的对象的状态保持一致. 为了保持一致, 则可能会发送对应的 SQL 语句.
	 * 1. 在 Transaction 的 commit() 方法中: 先调用 session 的 flush 方法, 再提交事务
	 * 2. flush() 方法会可能会发送 SQL 语句, 但不会提交事务.
	 * 3. 注意: 在未提交事务或显式的调用 session.flush() 方法之前, 也有可能会进行 flush() 操作.
	 * 1). 执行 HQL 或 QBC 查询, 会先进行 flush() 操作, 以得到数据表的最新的记录
	 * 2). 若记录的 ID 是由底层数据库使用自增的方式生成的, 则在调用 save() 方法时, 就会立即发送 INSERT 语句.
	 * 因为 save 方法后, 必须保证对象的 ID 是存在的!
	 */
	@Test
	public void testSessionFlush() {
		Books books = (Books) session.get(Books.class, "1002");
		books.setBookName("MySQL");
	}

	@Test
	public void testSessionCache() {
		Books books = (Books) session.get(Books.class, "1002");
		System.out.println(books);

		Books books2 = (Books) session.get(Books.class, "1002");
		System.out.println(books2);
	}
}
