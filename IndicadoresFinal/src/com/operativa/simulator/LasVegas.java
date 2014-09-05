package com.operativa.simulator;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.operativa.simulator.datamodel.SimEntry;


public class LasVegas{

	

	public static float obtenerDatoLV(List<SimEntry> datosFiltrados){
		
		int x=(int) (Math.random()*(datosFiltrados.size()));
		return datosFiltrados.get(x).getValor();
	}
	
	public static void agregarDatoALista(List<SimEntry> lista,float dato){
		
		int i=0;
		for(i=0;i<lista.size() && lista.get(i).getValor()!=dato ; i++);
		
		if(i<lista.size())
			lista.get(i).setCantidad(lista.get(i).getCantidad()+1);
		else{
			SimEntry e = new SimEntry();
			e.setValor(dato);
			e.setCantidad(1);
			lista.add(e);
		}
	}
	
	public static boolean esPar(int l){
		if(l % 2 == 0 ) 
			return true;
		return false;
	}
	
	public static Cuartiles cargarCuartiles (List<SimEntry> lista){
		//NO ANDA PARA UNA LISTA DE MENOS DE 5 ELEMENTOS
		
		Cuartiles retorno = new Cuartiles();
		int longitud = lista.size();
		int c1 , c3 = 0;
		
		if( esPar(longitud) ){
			if( esPar(longitud/2) ){
				c1=  (int) ( ( lista.get((longitud/4)-1).getValor() + lista.get((longitud/4)).getValor() ) / 2 );
				c3= (int) (( lista.get((int) (longitud* 0.75)).getValor() + lista.get((int) ((longitud* 0.75) + 1)).getValor()) /2);
			}else{
				 c1=(int)lista.get((int) (Math.floor(longitud/4))).getValor();
				 c3=(int) lista.get((int) (Math.floor(longitud* 0.75))).getValor();
				}	
		}else{
			 int l=(longitud-1)/2;		
			 if( esPar(l)){
				c1=(int) ((lista.get(l/2).getValor()+lista.get((l/2)+1).getValor())/2);
				c3=(int) ((lista.get((int)((l/2)*3)).getValor()+lista.get(((int)((l/2)*3))+1).getValor())/2);
			 }else{
				  c1=(int) lista.get((int)(l/2)).getValor();
				  c3=(int) lista.get((int)(longitud*0.75)).getValor();
			}
		}
		
		retorno.setC1(c1);
		retorno.setC3(c3);
		return retorno ;
	}

	
	public float lasVegas (List<SimEntry> datosFiltrados){
		
		Collections.sort(datosFiltrados);
		int longitud=datosFiltrados.size();
		
		if(longitud>4){
			Integer mitad=(Integer)(longitud/2); 
			float mediaStandar=obtenerMedia(datosFiltrados);
			Integer cuartil1=cargarCuartiles(datosFiltrados).getC1();
			Integer cuartil3=cargarCuartiles(datosFiltrados).getC3();
			boolean poda;
			float media=0;
		
			while ( !converge(media,mediaStandar)) {
				List<SimEntry> aux = new LinkedList<SimEntry>();
				poda=false;
				for(int i=0;i<longitud && !poda;i++){
					if((i==mitad) && ( (obtenerMedia(aux)<cuartil1)||(obtenerMedia(aux)>cuartil3) ))
						poda=true;
					else{
						float dato=obtenerDatoLV(datosFiltrados);
						agregarDatoALista(aux,dato);
					}	
				}
				if(!poda)
					media=obtenerMedia(aux);//el else es innecesario porque ya va a tener cargada la 
										//media anterior que va a volver a fallar y arranca así un nuevo intento
			}
			return media;
		}
		
		return 0;
}

	
	private static boolean converge (float media , float mediaAnt) {
		float dif = media-mediaAnt;
		dif = Math.abs(dif);
		if (dif < 0.5)
			return true ;
		else
			return false ;
	}
	
	
	private static float obtenerMedia(List<SimEntry> aux) {
		
		float acum=0;
		float c=0;
		for(int i=0;i<aux.size();i++){
			acum+=aux.get(i).getValor()*aux.get(i).getCantidad();
			c+=aux.get(i).getCantidad();
		}
		return acum/c;
	}


}