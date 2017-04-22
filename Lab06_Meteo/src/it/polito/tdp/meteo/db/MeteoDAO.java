package it.polito.tdp.meteo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.Rilevamento;

public class MeteoDAO {

	public List<Rilevamento> getAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese, String localita) {
		List<Rilevamento> temp=new ArrayList<Rilevamento>();
		for (Rilevamento r:getAllRilevamenti()){
			if (r.getLocalita().compareTo(localita)==0 && r.getMese()==mese)
				temp.add(r);
		}
		return temp;
	}

	public Double getAvgRilevamentiLocalitaMese(int mese, String localita) {
		
		//final String sql1= "SELECT COUNT(Umidita) AS numero FROM situazione WHERE Localita=? AND Data BETWEEN ? AND ?";
		final String sql1= "SELECT AVG(Umidita) AS media FROM situazione WHERE Localita=? AND MONTH(Data)=?";
		
		double numero=0;
		
		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql1);
			st.setString(1,localita);
			st.setLong(2, mese);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				
				numero=rs.getDouble("media");
				
			}

			conn.close();

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
//		System.out.println(localita);
//		System.out.println("   "+numero);
//		System.out.println("   "+somma);
		
		return numero;
	}
	
	public Set<Citta> getCitta(){
		
		final String sql="SELECT DISTINCT(Localita) FROM Situazione";
		
		Set<Citta> temp=new HashSet<Citta>();
		
		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				
				Citta c=new Citta(rs.getString("Localita"));
				temp.add(c);

			}

			conn.close();

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		return temp;
	}
	
	public Rilevamento getRilevamento(String citta,int anno, int mese,int giorno){		
	
		final String sql="SELECT Umidita FROM Situazione WHERE DAY(Data)=? AND MONTH(Data)=? AND YEAR(Data)=? AND Localita=?";
		Rilevamento r=null;
		
		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1,giorno);
			st.setInt(2, mese);
			st.setInt(3, anno);
			st.setString(4, citta);
			ResultSet rs = st.executeQuery();
	
			while (rs.next()) {
				
				r=new Rilevamento(citta,rs.getInt("Umidita"));
	
			}
	
			conn.close();
	
		} catch (SQLException e) {
	
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		return r;
	}

}
