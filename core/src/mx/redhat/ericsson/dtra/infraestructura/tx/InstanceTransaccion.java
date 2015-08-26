package mx.redhat.ericsson.dtra.infraestructura.tx;


public class InstanceTransaccion
{
	private static InstanceTransaccion config = null;
	
	public static InstanceTransaccion getInstance()
	{
		if(config != null)
		{
			config = new InstanceTransaccion();
		}
		return config;
	}
	
	@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
	private TransaccionLauncher<TransaccionDTO> creaTransaccion(Class miTx)throws Exception
	{
		String className = miTx.getCanonicalName();
		try
		{
			return (TransaccionLauncher<TransaccionDTO>) Class.forName(className).newInstance();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
	}
}
