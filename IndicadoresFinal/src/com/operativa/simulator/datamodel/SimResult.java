package com.operativa.simulator.datamodel;

import java.util.Date;

public class SimResult {
	
	Date fin;
	Date inicio;
	Date fecha;
	int id;
	Integer nroCorrida;
	float resultado;
	
	public Date getFin() {
		return fin;
	}
	public void setFin(Date fin) {
		this.fin = fin;
	}
	public Date getInicio() {
		return inicio;
	}
	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Integer getNroCorrida() {
		return nroCorrida;
	}
	public void setNroCorrida(Integer nroCorrida) {
		this.nroCorrida = nroCorrida;
	}
	public float getResultado() {
		return resultado;
	}
	public void setResultado(float resultado) {
		this.resultado = resultado;
	}
	
	
	public SimResult(Date inicio, Date fin, Date fecha, int id,
			Integer nroCorrida, float resultado) {
		this.fin = fin;
		this.inicio = inicio;
		this.fecha = fecha;
		this.id = id;
		this.nroCorrida = nroCorrida;
		this.resultado = resultado;
	}
	
}
