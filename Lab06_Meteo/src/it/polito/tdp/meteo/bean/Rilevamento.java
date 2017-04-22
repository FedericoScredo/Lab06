package it.polito.tdp.meteo.bean;

import java.util.Date;

public class Rilevamento {

	private String localita;
	private Date data;
	private int umidita;

	public Rilevamento(String localita, Date data, int umidita) {
		super();
		this.localita = localita;
		this.data = data;
		this.umidita = umidita;
	}
	
	public Rilevamento(String localita,int umidita){
		this.localita=localita;
		this.umidita=umidita;
	}

	public String getLocalita() {
		return localita;
	}

	public void setLocalita(String localita) {
		this.localita = localita;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public int getUmidita() {
		return umidita;
	}

	public void setUmidita(int umidita) {
		this.umidita = umidita;
	}
	
	@SuppressWarnings("deprecation")
	public int getMese(){
		return data.getMonth();
	}
	
	public String dataInStringa(){
		return data.getYear()+"-"+data.getMonth()+"-"+data.getDay();
	}

	// @Override
	// public String toString() {
	// return localita + " " + data + " " + umidita;
	// }

	@Override
	public String toString() {
		return String.valueOf(umidita);
	}
}
