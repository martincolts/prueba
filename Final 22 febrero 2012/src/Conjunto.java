import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;


public class Conjunto extends Vestimenta {

	Vector<Vestimenta> vests;
	
	public Vector<Vestimenta> aplanar (){
		Vector<Vestimenta> aux = new Vector <Vestimenta>();
		for (int i = 0 ; i < vests.size(); i++){
			aux.addAll( vests.elementAt(i).aplanar());
		}
		return aux ;
	}
	
	@Override
	public Object get(String key) {
		
		Vector<Vestimenta> aux = aplanar ();
		
		Hashtable <Object,Integer> aux2 = new Hashtable <Object, Integer>();
		
		for (int i = 0 ; i < aux.size(); i++){
			if (aux2.containsKey(aux.elementAt(i).get(key))){
				Integer in = new Integer( aux2.get(aux.elementAt(i).get(key)));
				in++;
				aux2.put((aux.elementAt(i).get(key)), in);
			}
			else
				aux2.put(aux.elementAt(i).get(key), 1);
		}
		
		Enumeration e =  aux2.keys();
		Object o1 = e.nextElement();
		
		for (;e.hasMoreElements();){
			Object o2 = e.nextElement();
			if ( aux2.get(o1) < aux2.get(o2))
				o1 = o2 ;
			
		}
		return o1 ;
	}

}
