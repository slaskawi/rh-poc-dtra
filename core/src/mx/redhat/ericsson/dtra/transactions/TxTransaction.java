package mx.redhat.ericsson.dtra.transactions;

import java.util.List;

import mx.redhat.ericsson.dtra.infrastructure.tx.Transaction;
import mx.redhat.ericsson.dtra.infrastructure.tx.TransactionDTO;

import org.hibernate.Session;

public class TxTransaction extends Transaction{

	private String region = null;
	
	@Override
	public TransactionDTO executeTransaction(Session sesion) throws Exception
	{
		//resultsTX.add(region);
		return dtoResponse;
	}
	
}
