package com.operativa.simulator.datamodel;

import java.util.Comparator;

public class ComparadorFecha implements Comparator<IndicatorEntry>{

	@Override
	public int compare(IndicatorEntry arg0, IndicatorEntry arg1) {
		if (arg0.getDate().after(arg1.getDate()))
			return 1;
		else return -1;
	}
	

}
