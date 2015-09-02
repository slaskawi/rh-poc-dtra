package mx.redhat.ericsson.dtra.infrastructure.client;

import java.util.LinkedHashMap;

import mx.redhat.ericsson.dtra.transactions.TxTransaction;

public class NamesTransaction
{
	public static NamesTransaction obj = null;
	private LinkedHashMap<NamesTX, String> hmIds = new LinkedHashMap<NamesTX, String>();
	
	public NamesTransaction()
	{
		hmIds.put(NamesTX.txTransaction, TxTransaction.class.getCanonicalName());
	}
	
	public static NamesTransaction getInstance()
	{
		if(obj == null)
		{
			obj = new NamesTransaction();
		}
		return obj;
	}

	public LinkedHashMap<NamesTX, String> getHmIds() {
		return hmIds;
	}
}
