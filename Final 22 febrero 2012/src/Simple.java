import java.util.Hashtable;
import java.util.Vector;


public class Simple extends Vestimenta {

	Hashtable <String,  Object> atrs;
	
	public Object get(String key) {
		
		return atrs.get(key);
		
	}
	
	public Vector<Vestimenta> aplanar (){
		Vector<Vestimenta> aux = new Vector<Vestimenta>();
		aux.add(this);
		return aux ;
	}
	
	public void put (String key , Object elemento){
		atrs.put(key, elemento);
	}

}
