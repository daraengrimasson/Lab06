package it.polito.tdp.meteo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
		
		return null;
	}

	/*public Double getAvgRilevamentiLocalitaMese(int mese, String localita) {

		return 0.0;
	}*/

	public String calcolaUmiditaMedia(String mese) {
		final String sql = "SELECT Localita, AVG(Umidita) AS UmiditaMedia FROM situazione WHERE Month(Data)=? GROUP BY Localita";

		String risultato="";

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, mese);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				risultato += rs.getString("Localita") +"\t"+ rs.getDouble("UmiditaMedia")+"\n";
			
			}

			conn.close();
			return risultato;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public List<Rilevamento> getRilevamentiGiorno(int mese, int livello) {
		final String sql = "SELECT Localita, Data, Umidita FROM situazione WHERE MONTH(Data)=? AND DAY(Data)=?";

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setString(1, Integer.toString(mese));
			st.setString(2, Integer.toString(livello));
			ResultSet rs = st.executeQuery();
			
			List<Rilevamento> ris = new ArrayList<Rilevamento>();

			while (rs.next()) {
				
				ris.add(new Rilevamento(rs.getString("Localita"),rs.getDate("Data"),rs.getInt("Umidita")));

			}

			conn.close();
			
			return ris;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	
	
	
	

}
