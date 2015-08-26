package mx.redhat.ericsson.dtra.infraestructura.hibernate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import mx.redhat.ericsson.dtra.infraestructura.client.EntidadGenerica;
import mx.redhat.ericsson.dtra.infraestructura.client.NamesTransaction;
import mx.redhat.ericsson.dtra.infraestructura.tx.ConfigTransaccion;
import mx.redhat.ericsson.dtra.infraestructura.tx.SessionHB;
import mx.redhat.ericsson.dtra.infraestructura.tx.TransaccionDTO;
import mx.redhat.ericsson.dtra.infraestructura.tx.TransaccionLauncher;

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
	public <T extends EntidadGenerica> List<T> busquedaGenerica(T objBuscar) throws Exception
	{
		sessionHB = ConfigHibernate.getSession();

		ArrayList<T> data = new ArrayList<T>();
		Transaction tx = null;
		try
		{
			tx = sessionHB.beginTransaction();
			
			Query query = sessionHB.createQuery(objBuscar.getQueryHql());
			Map<Object, Object> params = objBuscar.getParametros();
			if ((params != null) && (params.size() > 0))
			{
				query.setProperties(params);
			}
			query.setMaxResults(objBuscar.MAXIMO_CONSULTA);
			
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
	
	public TransaccionDTO ejecutaTransaccion(TransaccionDTO transaccionDTO) throws Exception
	{
		sessionHB = null;
		Transaction tx = null;
		sessionHB = ConfigHibernate.getSession();

		try
		{
			tx = sessionHB.beginTransaction();
			String canonicalName = NamesTransaction.getInstance().getHmIds().get(transaccionDTO.getClassTx());
			TransaccionLauncher<TransaccionDTO> objTx = ConfigTransaccion.getInstance().creaTransaccion(canonicalName);
			objTx.inicializaParametros(transaccionDTO.getParametrosTx());
			transaccionDTO = (TransaccionDTO) objTx.ejecutaTransaccion(sessionHB);
			transaccionDTO.setClassTx(null);
			transaccionDTO.setParametrosTx(null);
			tx.commit();

			return transaccionDTO;
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
	public <T extends EntidadGenerica> T busquedaPorId(T objBuscar) throws Exception
	{
		sessionHB = ConfigHibernate.getSession();

		Transaction tx = null;
		T resultado = null;
		try
		{
			tx = sessionHB.beginTransaction();

			resultado = (T) sessionHB.get(objBuscar.getClass(), objBuscar.getID());
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

		return resultado;

	}

	@SuppressWarnings("unchecked")
	public <T extends EntidadGenerica> T busquedaGenericaSimple(T objBuscar)throws Exception 
	{
		sessionHB = ConfigHibernate.getSession();

		T data = null;
		Transaction tx = null;
		try
		{
			tx = sessionHB.beginTransaction();
			
			Query query = sessionHB.createQuery(objBuscar.getQueryHql());
			Map<Object, Object> params = objBuscar.getParametros();

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
	public <T extends EntidadGenerica> T guardaActualiza(T objGuardar)throws Exception 
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
