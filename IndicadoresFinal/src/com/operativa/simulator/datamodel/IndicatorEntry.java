package com.operativa.simulator.datamodel;

import java.util.Date;


public class IndicatorEntry implements Comparable<IndicatorEntry> {

	private int id;
	private float value;
	private Date date;
	private String state;

	public IndicatorEntry(int id, float value, Date date, String state) {
		super();
		this.id = id;
		this.value = value;
		this.date = date;
		this.state = state;
	}
 
	public IndicatorEntry() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public int compareTo(IndicatorEntry arg0) {
		if (this.value > arg0.getValue()) {
			return 1;
		} else if (this.value < arg0.getValue()) {
			return -1;
		}
		return 0;
	}
}
