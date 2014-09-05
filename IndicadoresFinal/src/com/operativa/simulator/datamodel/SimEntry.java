package com.operativa.simulator.datamodel;

public class SimEntry implements Comparable<Object> {
	float valor;
	float cantidad;
	
	public SimEntry () {
	}
	
	public SimEntry (  float cantidad ,float valor ) {
		this.valor = valor ;
		this.cantidad = cantidad ;
	}
	
	public float getValor () { return valor ;}
	public float getCantidad () { return cantidad ;}
	
	public void setValor (float valor) { this.valor = valor ;}
	public void setCantidad (float cantidad) { this.cantidad = cantidad ;}

	@Override
	public int compareTo(Object arg0) {
		SimEntry aux = (SimEntry) arg0 ;
		return ((int)(this.valor - aux.valor));
	}
	
	
}
