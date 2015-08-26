package mx.redhat.ericsson.dtra.infraestructura.client;

import java.beans.Transient;
import java.util.Map;

/**
 * @author LuisGlz
 */

public interface IQuerys 
{
	@Transient
	public String getID();
	@Transient
	public String getQueryHql();
	@Transient
	public Map<Object, Object>getParametros();
}
