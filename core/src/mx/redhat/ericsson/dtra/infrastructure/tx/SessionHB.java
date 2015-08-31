package mx.redhat.ericsson.dtra.infrastructure.tx;

import java.util.Date;
import java.util.List;

import mx.redhat.ericsson.dtra.infrastructure.client.GenericEntity;

public interface SessionHB
{
	public Date getFecha() throws Exception;
	public <T extends GenericEntity> List<T> busquedaGenerica(T objBuscar) throws Exception;
	public <T extends GenericEntity> T busquedaPorId(T objBuscar) throws Exception;
	public <T extends GenericEntity> T guardaActualiza(T objBuscar) throws Exception;
	public TransactionDTO ejecutaTransaccion(TransactionDTO dto) throws Exception;
//	public Session getSesionHB() throws Exception;
//	public Constantes iniciaConstantes()throws Exception;;
}
