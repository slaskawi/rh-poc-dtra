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
	protected List<Object> parametrosTx = null;
	protected List<Object> resultadosTx = null;
//	protected TransaccionClavesValue idTransaccion = null; //SPRING :(
	protected NamesTX classTx = null;
	
	public TransactionDTO() {
	}
	
	public TransactionDTO(List<Object> resultado) {
		this.resultadosTx = resultado;
	}

//	public TransaccionClavesValue getIdTransaccion()
//	{
//		return idTransaccion;
//	}
//
//	public void setIdTransaccion(TransaccionClavesValue idTransaccion)
//	{
//		this.idTransaccion = idTransaccion;
//	}

	public List<Object> getParametrosTx() {
		return parametrosTx;
	}

	public void setParametrosTx(List<Object> parametrosTx) {
		this.parametrosTx = parametrosTx;
	}

	public List<Object> getResultadosTx() {
		return resultadosTx;
	}

	public void setResultadosTx(List<Object> resultadosTx) {
		this.resultadosTx = resultadosTx;
	}

	public NamesTX getClassTx() {
		return classTx;
	}

	public void setClassTx(NamesTX classTx) {
		this.classTx = classTx;
	}

}
