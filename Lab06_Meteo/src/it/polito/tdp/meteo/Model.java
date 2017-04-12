package it.polito.tdp.meteo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import it.polito.tdp.meteo.bean.Rilevamento;
import it.polito.tdp.meteo.bean.SimpleCity;
import it.polito.tdp.meteo.db.MeteoDAO;


public class Model {

	private final static int COST = 50;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;

	public Model() {

	}

	public String getUmiditaMedia(String mese) {
		MeteoDAO dao=new MeteoDAO();
		return dao.calcolaUmiditaMedia(mese);
	}
	
		

	public String trovaSequenza(int mese) {
		List<SimpleCity> parziale=new LinkedList<SimpleCity>();
		List<SimpleCity> best=new LinkedList<SimpleCity>();
		int livello=1;
		scegli(parziale,livello,best, mese);
		String ris="";
			for(int i=1;i<best.size();i++){
				ris+= best.get(i).toString()+" ";
			} 
		return ris;
		/*return best.toString();*/
		
	}
	
	/**
	 * metodo ricorsivo vero e proprio
	 */
	private void scegli(List<SimpleCity> parziale, int livello, List<SimpleCity> best, int mese){
		MeteoDAO dao = new MeteoDAO();
		if(parziale.size()==NUMERO_GIORNI_TOTALI){
			if(punteggioSoluzione(parziale)<punteggioSoluzione(best)){
				best.clear();
				best.addAll(parziale);
			}
			return;
		}
		
		List <Rilevamento> rilevamentiGiorno = dao.getRilevamentiGiorno(mese,livello);
		for(Rilevamento r : rilevamentiGiorno){
			/*metodo deputato ai controlli*/
			if(controllaParziale(parziale)){
				SimpleCity sc = new SimpleCity (r.getLocalita(),r.getUmidita());
				parziale.add(sc);
				livello++;
				scegli(parziale,livello,best,mese);
				parziale.remove(sc);
			}
			else
				return;
		}
	}
	
	/**
	 * metodo che calcola il costo
	 */
	private Double punteggioSoluzione(List<SimpleCity> soluzioneCandidata) {
		double score = 0.0;
		for(SimpleCity sc: soluzioneCandidata){
			score+=sc.getCosto();
		}
			
		/*se la città che segue è diversa, vuol dire che mi sono spostato*/
		for(int i = 1; i<soluzioneCandidata.size();i++){
			if(!soluzioneCandidata.get(i).getNome().equals(soluzioneCandidata.get(i-1).getNome()))
				score += 100;
		}
		return score;
	}

	
	/**
	 * facciamo tutti i controlli qui dentro
	 */
	private boolean controllaParziale(List<SimpleCity> parziale) {
		/*devo calcoare per i primi 15 giorni del mese in input*/
		if(parziale.size()>NUMERO_GIORNI_TOTALI)
			return false;
		
			
		int contaTorino=0;
		int contaMilano=0;
		int contaGenova=0;
		for(SimpleCity ct : parziale){
			if(ct.getNome().equals("Torino"))
				contaTorino++;
			if(ct.getNome().equals("Milano"))
				contaMilano++;
			if(ct.getNome().equals("Genova"))
				contaGenova++;
		}
		/*in una città non mi posso fermare più di sei giorni*/
		if(contaTorino>NUMERO_GIORNI_CITTA_MAX || contaMilano >NUMERO_GIORNI_CITTA_MAX || contaGenova >NUMERO_GIORNI_CITTA_MAX)
			return false;
		
		/*ha senso fare questi controlli solo se ho già passato i 3 giorni*/
		if(parziale.size()>=NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN){
			int cont = 0; //contatore che tiene traccia dei giorni consecutivi trascorsi in una città
			for(int z = 0; z<parziale.size()-1;z++){
				cont++;
				/*scelta una città, il tecnico non si può spostare prima di aver trascorso 3 giorni consecutivi*/
				if(!parziale.get(z).getNome().equals(parziale.get(z+1).getNome()) && cont<NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN)
					return false;
				/*se mi sono spostato e sono trascorsi  3 giorni è ok, devo solo resettare il cont a 0*/
				if(!parziale.get(z).getNome().equals(parziale.get(z+1).getNome()))
					cont = 0;
				
			}
		}
		return true;
	}

	
	
	
	
	
	
	
	
}
