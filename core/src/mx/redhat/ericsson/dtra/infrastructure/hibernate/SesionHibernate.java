package mx.redhat.ericsson.dtra.infrastructure.hibernate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import mx.redhat.ericsson.dtra.infrastructure.client.GenericEntity;
import mx.redhat.ericsson.dtra.infrastructure.client.NamesTransaction;
import mx.redhat.ericsson.dtra.infrastructure.tx.ConfigTransaction;
import mx.redhat.ericsson.dtra.infrastructure.tx.SessionHB;
import mx.redhat.ericsson.dtra.infrastructure.tx.TransactionDTO;
import mx.redhat.ericsson.dtra.infrastructure.tx.TransactionLauncher;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @author LuisGlz
 */

public class SesionHibernate implements SessionHB
{

	private static SesionHibernate instance = null;
	private Session sessionHB = null;
	public String tipo; 

	private SesionHibernate()
	{
		
	}

	public static SesionHibernate getInstance()
	{
		if (instance == null)
		{
			instance = new SesionHibernate();
		}
		return instance;
	}

	public Date getFecha() throws Exception
	{
		Session db = null;
		try
		{
			sessionHB = ConfigHibernate.getSession();
			SQLQuery qry = sessionHB.createSQLQuery(" SELECT SYSDATE AS HOY FROM DUAL ");
			qry.addScalar("HOY");
			Object fecha = qry.uniqueResult();

			if (fecha == null)
			{
				throw new Exception("Error al obtener la fecha.");
			}
			return (Date) fecha;
		}
		catch (SQLException e)
		{
			System.out.println(e);
			throw e;
		}
		finally
		{
			System.gc();
			if (db != null)
			{
				db.close();
			}
		}

	}

	@SuppressWarnings("unchecked")
	public <T extends GenericEntity> List<T> genericResearch(T objBuscar) throws Exception
	{
		sessionHB = ConfigHibernate.getSession();

		ArrayList<T> data = new ArrayList<T>();
		Transaction tx = null;
		try
		{
			tx = sessionHB.beginTransaction();
			
			Query query = sessionHB.createQuery(objBuscar.getQueryHql());
			Map<Object, Object> params = objBuscar.getParams();
			if ((params != null) && (params.size() > 0))
			{
				query.setProperties(params);
			}
			query.setMaxResults(objBuscar.MAX_RESULTS);
			
			List<T> rs = query.list();
			for (T obj : rs)
			{
				data.add(obj);
			}

			tx.rollback();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			if (tx != null)
			{
				try
				{
					tx.rollback();
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}

			System.out.println();
			throw e;
		}
		finally
		{
			System.gc();
			if (sessionHB != null)
			{
				ConfigHibernate.closeSession();
			}
		}
		return data;
	}
	
	public TransactionDTO executeTransaction(TransactionDTO transactionDTO) throws Exception
	{
		sessionHB = null;
		Transaction tx = null;
		sessionHB = ConfigHibernate.getSession();

		try
		{
			tx = sessionHB.beginTransaction();
			String canonicalName = NamesTransaction.getInstance().getHmIds().get(transactionDTO.getClassTx());
			TransactionLauncher<TransactionDTO> objTx = ConfigTransaction.getInstance().createTransaction(canonicalName);
			//objTx.initParams(transaccionDTO.getParamsTx());
			transactionDTO = (TransactionDTO) objTx.executeTransaction(sessionHB);
			transactionDTO.setClassTx(null);
			transactionDTO.setParamsTx(null);
			tx.commit();

			return transactionDTO;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			if (tx != null)
			{
				tx.rollback();
			}
			throw e;
		}
		finally
		{
			System.out.println();
			System.gc();
			try
			{
				if (sessionHB != null)
				{
					sessionHB.close();
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}

		}
	}

//	public ArrayList<Constante> getConstantes() throws Exception
//	{
//		Constante c = new Constante();
//		c.initbusscaTodos();
//		return busquedaGenerica(c);
//	}

	@SuppressWarnings("unchecked")
	public <T extends GenericEntity> T selectById(T objQuest) throws Exception
	{
		sessionHB = ConfigHibernate.getSession();

		Transaction tx = null;
		T result = null;
		try
		{
			tx = sessionHB.beginTransaction();

			result = (T) sessionHB.get(objQuest.getClass(), objQuest.getID());
			tx.rollback();
		}
		catch (Exception e)
		{
			if (tx != null)
			{
				try
				{
					tx.rollback();
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
			e.printStackTrace();
			throw e;
		}
		finally
		{
			System.gc();
			if (sessionHB != null)
			{
				sessionHB.close();
			}
		}

		return result;

	}

	@SuppressWarnings("unchecked")
	public <T extends GenericEntity> T busquedaGenericaSimple(T objBuscar)throws Exception 
	{
		sessionHB = ConfigHibernate.getSession();

		T data = null;
		Transaction tx = null;
		try
		{
			tx = sessionHB.beginTransaction();
			
			Query query = sessionHB.createQuery(objBuscar.getQueryHql());
			Map<Object, Object> params = objBuscar.getParams();

			if ((params != null) && (params.size() > 0))
			{
				query.setProperties(params);
			}

			Object rs = query.uniqueResult();
			data = (T)rs;

			tx.rollback();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			if (tx != null)
			{
				try
				{
					tx.rollback();
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}

			System.out.println();
			throw e;
		}
		finally
		{
			System.gc();
			if (sessionHB != null)
			{
				sessionHB.close();
			}
		}
		return data;
	}

	@Override
	public <T extends GenericEntity> T createOrUpdate(T objGuardar)throws Exception 
	{

		sessionHB = ConfigHibernate.getSession();
		Transaction tx = null;

		try
		{
			tx = sessionHB.beginTransaction();

			sessionHB.saveOrUpdate(objGuardar);

			tx.commit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			if (tx != null)
			{
				tx.rollback();
			}
			throw e;
		}
		finally
		{
			System.gc();
			if (sessionHB != null)
			{
				try
				{
					sessionHB.close();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		return objGuardar;
	}

//	@Override
//	public Session getSesionHB() throws Exception
//	{
//		if(sessionHB == null)
//		{
//			throw new Exception("The Session Hibernate is null......getSesionHB()..");
//		}
//		return sessionHB;
//	}
	
}
