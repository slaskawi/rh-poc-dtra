package mx.redhat.ericsson.dtra.infrastructure.tx;

import java.io.Serializable;
import java.util.List;

import mx.redhat.ericsson.dtra.infrastructure.client.NamesTX;

/**
 * @author LuisGlz
 */

@SuppressWarnings("serial")
public class TransactionDTO implements Serializable
{
	protected List<Object> paramsTx = null;
	protected List<Object> resultsTx = null;
//	protected TransaccionClavesValue idTransaccion = null; //SPRING :(
	protected NamesTX classTx = null;
	
	public TransactionDTO() {
	}
	
	public TransactionDTO(List<Object> resultsTx) {
		this.resultsTx = resultsTx;
	}

	public List<Object> getParamsTx() {
		return paramsTx;
	}

	public void setParamsTx(List<Object> paramsTx) {
		this.paramsTx = paramsTx;
	}

	public List<Object> getResultsTx() {
		return resultsTx;
	}

	public void setResultsTx(List<Object> resultsTx) {
		this.resultsTx = resultsTx;
	}

	public NamesTX getClassTx() {
		return classTx;
	}

	public void setClassTx(NamesTX classTx) {
		this.classTx = classTx;
	}

}
