package mx.redhat.ericsson.dtra.infrastructure.tx;

import java.util.List;

import org.hibernate.Session;

/**
 * @author LuisGlz
 */

public interface TransactionLauncher<T extends TransactionDTO>
{
	public T executeTransaction(Session sesion) throws Exception;
}
