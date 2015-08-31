package mx.redhat.ericsson.dtra.infrastructure.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 * @author LGONZALEZ
 */

public class ConfigHibernate
{
	private static final ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();
	private static SessionFactory sessionFactory = null;
	
	private ConfigHibernate()
	{
		
	}

	public static Session getSession() throws HibernateException
	{
		Session session =  threadLocal.get();
		if (session == null || !session.isOpen())
		{
			if (sessionFactory == null)
			{
				sessionFactoryAndRegistry();
			}
			session = (sessionFactory != null) ? sessionFactory.openSession() : null;
			threadLocal.set(session);
		}
		return session;
	}

	public static void sessionFactoryAndRegistry()
	{
		try
		{
			Configuration configuration = new Configuration().configure(new ConfigHibernate().getClass().getResource("/mx/redhat/ericsson/dtra/infraestructura/server/hibernate/hibernate.cfg.xml"));
			ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
			sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		}
		catch (Exception e)
		{
			System.err.println("##################################################################");
			System.err.println("############## SESSIONFACTORY HIBERNATE NOT CREATE ###############");
			System.err.println("##################################################################");
			e.printStackTrace();
		}
	}

	/**
	 * Close the single hibernate session instance.
	 * 
	 * @throws HibernateException
	 */
	public static void closeSession() throws HibernateException
	{
		Session session =  threadLocal.get();
		threadLocal.set(null);

		if (session != null)
		{
			session.close();
		}
	}

	/**
	 * return session factory
	 * 
	 */
	public static org.hibernate.SessionFactory getSessionFactory()
	{
		return sessionFactory;
	}
}
