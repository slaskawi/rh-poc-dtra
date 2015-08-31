package mx.redhat.ericsson.dtra.infrastructure.tx;


public class InstanceTransaction
{
	private static InstanceTransaction config = null;
	
	public static InstanceTransaction getInstance()
	{
		if(config != null)
		{
			config = new InstanceTransaction();
		}
		return config;
	}
	
	@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
	private TransactionLauncher<TransactionDTO> creaTransaccion(Class miTx)throws Exception
	{
		String className = miTx.getCanonicalName();
		try
		{
			return (TransactionLauncher<TransactionDTO>) Class.forName(className).newInstance();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
	}
}
