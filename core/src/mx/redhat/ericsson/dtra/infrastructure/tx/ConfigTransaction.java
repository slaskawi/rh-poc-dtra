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

	/**
	 * NO VAMOS ACUPAR SPRING PARA LAS TRANSACCIONES :(
	 * 
	@SuppressWarnings("unchecked")
	public TransaccionLauncher<TransaccionDTO> creaTransaccionSpring(TransaccionClavesValue claveTx) throws Exception
	{
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("com/bean/fdc/infraestructura/spring/ConfigSpringTX.xml");
		ClassTransacciones servicio = applicationContext.getBean("misTransacciones", ClassTransacciones.class);
		
		String className = servicio.getClassName(claveTx.getValue());

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
	*/
	
	@SuppressWarnings({ "unchecked"})
	public TransactionLauncher<TransactionDTO> creaTransaccion(String miTx)throws Exception
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
