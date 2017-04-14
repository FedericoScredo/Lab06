package it.polito.tdp.meteo;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.Rilevamento;
import it.polito.tdp.meteo.bean.SimpleCity;
import it.polito.tdp.meteo.db.MeteoDAO;

public class Model {

	private final static int COST = 50;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	
	private MeteoDAO dao;
	private List<Rilevamento> rilevamenti;
	private Set<Citta> cittadatabase;
	private int mese;

	public Model() {
		dao=new MeteoDAO();
		rilevamenti=new LinkedList<Rilevamento>();
		cittadatabase=new HashSet<Citta>();
	}
	
	public void importaDati(){
		rilevamenti=dao.getAllRilevamenti();
		cittadatabase=dao.getCitta();
	}

	public String getUmiditaMedia(int mese) {
		
		String medie="";
		
		for (Citta c:dao.getCitta()){
			double media=dao.getAvgRilevamentiLocalitaMese(mese,c.getNome());
			medie+=c.getNome()+" "+media+"\n";
		}

		return medie;
	}

	public String trovaSequenza(int mese) {
		this.mese=mese;
		Set<Citta> temp=null;
		Set<Citta> best=null;
		String s="";
		Set<Citta> sol=ricorsione(0,temp,best);
		s=s+"costo"+punteggioSoluzione(sol)+"\n";
		for (Citta c:sol){
			s=s+c.getNome()+"\n";
		}
		return s;
	}
	
	private Set<Citta> ricorsione(int livello,Set<Citta> temp,Set<Citta> best){
		if (livello>15){
			if (punteggioSoluzione(temp)>=punteggioSoluzione(best))
				best.clear();
				best.addAll(temp);
		}
		else{
			System.out.println("entrato");
			System.out.println(dao.getCitta().size());
			for (Citta c:dao.getCitta()){
				System.out.println("   citta "+c.getNome());				
				if (numeroGiorniVisitata(c,temp)<=6){
					System.out.println("     provo ad aggiungerla");
					temp.add(new Citta(c.getNome()));
					System.out.println("     ricorsione a livello successivo");
					ricorsione(livello+3,temp,best);
					System.out.println("     la rimuovo");
					temp.remove(c);
				}
			}
		}
		return best;
	}

	private Double punteggioSoluzione(Set<Citta> soluzioneCandidata) {
		double score = 0.0;
		int x=1;
		for (Citta c:soluzioneCandidata){
			score=score+(100*numeroGiorniVisitata(c,soluzioneCandidata))+getRilevamento(c,2013,mese,x);
			x++;
		}
		return score;
	}

	private boolean controllaParziale(List<SimpleCity> parziale) {

		return true;
	}
	
	private int numeroGiorniVisitata(Citta c,Set<Citta> temp){
		int x=0;
		if (temp==null){
			return 0;
		}
		else
		{
			for (Citta citta:temp){
				if (citta.equals(c)){
					x++;
				}
			}
			return x;
		}
	}
	
	private double getRilevamento(Citta citta,int anno,int mese,int giorno){
		for (Rilevamento r:rilevamenti){
			if (r.getLocalita().compareTo(citta.getNome())==0 && r.getData().compareTo(new Date(anno,mese,giorno))==0)
				return r.getUmidita();
		}
		return 0;
	}

}
