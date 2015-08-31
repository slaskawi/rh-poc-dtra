package mx.redhat.ericsson.dtra.infrastructure.tx;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mx.redhat.ericsson.dtra.infrastructure.client.GenericEntity;

import org.hibernate.Query;
import org.hibernate.Session;

/**
 * @author LuisGlz
 */

public abstract class Transaction implements TransactionLauncher<TransactionDTO>
{
	protected List<Object> params = null;
	protected List<Object> resultadoTX = null;
	protected TransactionDTO dtoResponse = null;
	
	protected SimpleDateFormat formatFecha = new SimpleDateFormat("dd/MM/yyyy");
	protected SimpleDateFormat formatFechaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	protected DecimalFormat formatDecimal2 = new DecimalFormat("##.00"); 
	
	public Transaction() 
	{
		this.resultadoTX = new ArrayList<Object>();
		dtoResponse = new TransactionDTO(resultadoTX);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends GenericEntity> List<T> busquedaGenerica(Session sesion,T objBuscar)throws Exception
	{
		ArrayList<T> data = new ArrayList<T>();
		try
		{
			Query query = sesion.createQuery(objBuscar.getQueryHql());
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
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println();
			throw e;
		}
		return data;
	}
	
	public Double getDoubleScale2(double valor)
	{
		return Math.round(valor * 100.0) / 100.0;
	}

}
