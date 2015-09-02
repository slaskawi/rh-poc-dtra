package mx.redhat.ericsson.dtra.infrastructure.tx;


/**
 * @author LuisGlz
 */
public class ConfigTransaction
{
	private static ConfigTransaction instance = null;

	private ConfigTransaction()
	{
		
	}
	
	public static ConfigTransaction getInstance() 
	{
		if (instance == null)
		{
			instance = new ConfigTransaction();
		}
		return instance;
	}

	@SuppressWarnings({ "unchecked"})
	public TransactionLauncher<TransactionDTO> createTransaction(String miTx)throws Exception
	{
		String className = miTx;
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
