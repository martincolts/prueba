package com.operativa.simulator;


import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import com.operativa.simulator.datamodel.SimEntry;




public class Montecarlo {
	
	
	private List<SimEntry> gen_vec_acum (List<SimEntry> datosFiltrados) {
		
	
		float acum = 0 ;
		
		for ( ListIterator<SimEntry> i = datosFiltrados.listIterator(); i.hasNext() ; ) {
			acum = acum + i.next().getCantidad();
		}
		List<SimEntry> aux = new LinkedList<SimEntry>();
		for ( ListIterator<SimEntry> i = datosFiltrados.listIterator(); i.hasNext() ; ) {
			SimEntry auxiliar = i.next();
			auxiliar.setCantidad(auxiliar.getCantidad()/acum);
			aux.add(auxiliar);
		}
		
		//System.out.println ("PROBABILIDADES");
//		for ( ListIterator<SimEntry> i = aux.listIterator(); i.hasNext() ; ) {
//			SimEntry e = i.next();
//			//System.out.println (e.getValor()+"  cantidad:"+e.getCantidad());
//		}
		
		
		List<SimEntry> aux2 = new LinkedList<SimEntry>();
		ListIterator<SimEntry> it = aux.listIterator();
		if (it.hasNext()) {
			SimEntry e = it.next();
			aux2.add(0,e);
			it.previous();
		}
		for ( ListIterator<SimEntry> i = aux.listIterator(); i.hasNext() ; ) {
			if (i.hasPrevious()) {
				i.previous();
				SimEntry auxiliar1 = i.next();
				//System.out.println ("Dato previo: "+auxiliar1.getValor()+" "+auxiliar1.getCantidad());
				SimEntry auxiliar2 = i.next();
				//System.out.println ("Dato actual: "+auxiliar2.getValor()+" "+auxiliar2.getCantidad());
				auxiliar2.setCantidad(auxiliar2.getCantidad()+auxiliar1.getCantidad());
				aux2.add(auxiliar2);
				
			} else
				i.next();
		}
		//System.out.println ("ACUMULADA");
//		for ( ListIterator<SimEntry> i = aux2.listIterator(); i.hasNext() ; ) {
//			SimEntry e = i.next();
//			System.out.println (e.getValor()+"  cantidad:"+e.getCantidad());
//		}
		return datosFiltrados ;
	}
	
	private float sacarDato (List<SimEntry> listaAcumulada) {
		
		float random =(float) Math.random();
		for (ListIterator<SimEntry> i = listaAcumulada.listIterator(); i.hasNext(); ) {
			SimEntry aux = i.next();
			if ( random < aux.getCantidad()) 
				return aux.getValor();
		}
			return 0 ;
	}
	
	private boolean converge (float media , float mediaAnt) {
		float dif = media-mediaAnt;
		dif = Math.abs(dif);
		if (dif < 0.5)
			return true ;
		else
			return false ;
	}

	public float simulator (List<SimEntry> datosFiltrados) {
		
		float media = 0 ;
		float mediaAnt = 1 ;
		int tiradas = 0 ;
		List <SimEntry> listaAcumulada = gen_vec_acum (datosFiltrados);
		float acum = 0 ;
		
		while (!converge(media,mediaAnt) || tiradas < 100) {
			tiradas++;
			float dato = sacarDato (listaAcumulada);
			acum = acum + dato ;
			mediaAnt = media ;
			media = acum / (float) tiradas ;
		}
		
		return media ;
		
	}

}
