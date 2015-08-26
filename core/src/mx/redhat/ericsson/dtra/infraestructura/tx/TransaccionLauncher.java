package mx.redhat.ericsson.dtra.infraestructura.tx;

import java.util.List;

import org.hibernate.Session;

/**
 * @author LuisGlz
 */

public interface TransaccionLauncher<T extends TransaccionDTO>
{
	public T ejecutaTransaccion(Session sesion) throws Exception;
	public void inicializaParametros(List<Object> parametros);
}
