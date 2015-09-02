package mx.redhat.ericsson.dtra.infrastructure.tx;

import java.util.Date;
import java.util.List;

import mx.redhat.ericsson.dtra.infrastructure.client.GenericEntity;

public interface SessionHB
{
	public Date getFecha() throws Exception;
	public <T extends GenericEntity> List<T> genericResearch(T objBuscar) throws Exception;
	public <T extends GenericEntity> T selectById(T objBuscar) throws Exception;
	public <T extends GenericEntity> T createOrUpdate(T objBuscar) throws Exception;
	public TransactionDTO executeTransaction(TransactionDTO dto) throws Exception;
//	public Session getSesionHB() throws Exception;
//	public Constantes initConstants()throws Exception;
}
