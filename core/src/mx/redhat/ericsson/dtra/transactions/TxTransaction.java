package mx.redhat.ericsson.dtra.transactions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import mx.redhat.ericsson.dtra.infrastructure.tx.Transaction;
import mx.redhat.ericsson.dtra.infrastructure.tx.TransactionDTO;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

public class TxTransaction extends Transaction{

	private String region = null;
	private String marca = null;
	private String zona = null;
	private String clase = null;
	private String tipoRol = null;
	private String anio = null;
	private List<Object> params = null;
	
	@Override
	public TransactionDTO ejecutaTransaccion(Session sesion) throws Exception
	{
		//resultadoTX.add(valorMax);
		//resultadoTX.add(valorMax);
		return dtoResponse;
	}
	
	@Override
	public void inicializaParametros(List<Object> parametros)
	{
		this.params = parametros;
		//this.region = (String) parametros.get(0);
		//this.marca = (String) parametros.get(1);
	}

}