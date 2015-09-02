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
	protected List<Object> resultsTX = null;
	protected TransactionDTO dtoResponse = null;
	
	protected SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
	protected SimpleDateFormat formatDateHour = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	protected DecimalFormat formatDecimal2 = new DecimalFormat("##.00"); 
	
	public Transaction() 
	{
		this.resultsTX = new ArrayList<Object>();
		dtoResponse = new TransactionDTO(resultsTX);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends GenericEntity> List<T> busquedaGenerica(Session sesion,T objQuest)throws Exception
	{
		ArrayList<T> data = new ArrayList<T>();
		try
		{
			Query query = sesion.createQuery(objQuest.getQueryHql());
			Map<Object, Object> params = objQuest.getParams();
			if ((params != null) && (params.size() > 0))
			{
				query.setProperties(params);
			}
			query.setMaxResults(objQuest.MAX_RESULTS);
			
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
