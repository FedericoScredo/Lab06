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
	private int x;

	public Model() {
		dao=new MeteoDAO();
		rilevamenti=new LinkedList<Rilevamento>();
		cittadatabase=new HashSet<Citta>();
		x=0;
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
		List<Citta> temp=new LinkedList<Citta>();
		List<Citta> best=new LinkedList<Citta>();
		String s="";
		List<Citta> sol=ricorsione(0,temp,best);
		s=s+"costo "+punteggioSoluzione(sol)+"\n";
		for (Citta c:sol){
			s=s+c.getNome()+"\n"+c.getNome()+"\n"+c.getNome()+"\n";
		}
		return s;
	}
	
	private List<Citta> ricorsione(int livello,List<Citta> temp,List<Citta> best){
		if (controllaParziale(temp)==true){
			if (punteggioSoluzione(temp)<=punteggioSoluzione(best) || punteggioSoluzione(best)==0){
				x++;
				System.out.println("TROVATA UNA SOLUZIONE numero: "+x+" con punteggio: "+punteggioSoluzione(temp));
				best.clear();
				best.addAll(temp);
			}
		}
		else{
			//System.out.println("entrato");
			//System.out.println(dao.getCitta()+" "+dao.getCitta().size());
			for (Citta c:dao.getCitta()){
				if (numeroGiorniVisitata(c,temp)<=6){
					//System.out.println(livello+" citta "+c);				
					//System.out.println("provo ad aggiungerla");
					temp.add(c);
					//System.out.println("ricorsione a livello successivo "+(livello+1));
					ricorsione(livello+3,temp,best);
					//System.out.println(livello+" la rimuovo");
					temp.remove(temp.size()-1);
				}
			}
		}
		return best;
	}

	private Double punteggioSoluzione(List<Citta> soluzioneCandidata) {
		double score = 0.0;
		int x=1;
		for (Citta c:soluzioneCandidata){
			score=score+(100*numeroGiorniVisitata(c,soluzioneCandidata))+getRilevamento(c,2013,mese,x);
			x++;
		}
		return score;
	}

	private boolean controllaParziale(List<Citta> parziale) {
		if (parziale.size()==5){
			for (Citta c:parziale){
				if (numeroGiorniVisitata(c,parziale)>6 || numeroGiorniVisitata(c,parziale)<3)
					return false;
			}
			return true;
		}
		else
			return false;
	}
	
	private int numeroGiorniVisitata(Citta c,List<Citta> temp){
		int x=0;
		if (temp==null){
			return 0;
		}
		else
		{
			for (Citta citta:temp){
				if (citta.getNome().compareTo(c.getNome())==0){
					x=x+3;
				}
			}
			return x;
		}
	}
	
	@SuppressWarnings("deprecation")
	public double getRilevamento(Citta citta,int anno,int mese,int giorno){
		return dao.getRilevamento(citta.getNome(), anno, mese, giorno).getUmidita();
	}

}
