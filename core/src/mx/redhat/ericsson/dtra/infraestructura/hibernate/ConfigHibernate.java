package mx.redhat.ericsson.dtra.infraestructura.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

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
			Configuration configuracion = new Configuration().configure(new ConfigHibernate().getClass().getResource("/com/vsp/infraestructura/server/hibernate/hibernate.cfg.xml"));
			ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuracion.getProperties()).build();
			sessionFactory = configuracion.buildSessionFactory(serviceRegistry);
		}
		catch (Exception e)
		{
			System.err.println("###################################################################");
			System.err.println("##########ERROR AL CREAR SESSIONFACTORY HIBERNATE FODECO###########");
			System.err.println("###################################################################");
			System.err.println("###########################LGONZALEZ###############################");
			System.err.println("###################################################################");
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
