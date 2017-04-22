package it.polito.tdp.meteo;

import java.util.Date;

import it.polito.tdp.meteo.bean.Citta;

public class TestModel {

	public static void main(String[] args) {

		Model m = new Model();
		
		System.out.println(m.getUmiditaMedia(12));
		System.out.println(m.getRilevamento(new Citta("Genova"),2013,01,02));
		System.out.println(m.trovaSequenza(2));
		
		
	}

}
