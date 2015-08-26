package mx.redhat.ericsson.dtra.infraestructura.tx;

import java.util.Date;
import java.util.List;

import mx.redhat.ericsson.dtra.infraestructura.client.EntidadGenerica;

public interface SessionHB
{
	public Date getFecha() throws Exception;
	public <T extends EntidadGenerica> List<T> busquedaGenerica(T objBuscar) throws Exception;
	public <T extends EntidadGenerica> T busquedaPorId(T objBuscar) throws Exception;
	public <T extends EntidadGenerica> T guardaActualiza(T objBuscar) throws Exception;
	public TransaccionDTO ejecutaTransaccion(TransaccionDTO dto) throws Exception;
//	public Session getSesionHB() throws Exception;
//	public Constantes iniciaConstantes()throws Exception;;
}
