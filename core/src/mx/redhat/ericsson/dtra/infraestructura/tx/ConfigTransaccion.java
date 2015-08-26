package mx.redhat.ericsson.dtra.infraestructura.tx;


/**
 * @author LuisGlz
 */
public class ConfigTransaccion
{
	private static ConfigTransaccion instance = null;

	private ConfigTransaccion()
	{
		
	}
	
	public static ConfigTransaccion getInstance() 
	{
		if (instance == null)
		{
			instance = new ConfigTransaccion();
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
	public TransaccionLauncher<TransaccionDTO> creaTransaccion(String miTx)throws Exception
	{
		String className = miTx;
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
