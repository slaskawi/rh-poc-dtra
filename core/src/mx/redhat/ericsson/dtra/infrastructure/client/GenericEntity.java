package mx.redhat.ericsson.dtra.infrastructure.client;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LuisGlz
 */

@SuppressWarnings("serial")
public abstract class GenericEntity implements IQuerys, Serializable {
	
	protected String queryHql = "";
	protected Map<Object, Object> params = new HashMap<Object, Object>();
	protected String toString = "";

	public int MAX_RESULTS = Integer.MAX_VALUE;
	
}
