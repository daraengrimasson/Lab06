package it.polito.tdp.meteo;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class MeteoController {
	
	Model model;
	public void setModel(Model model) {
		this.model=model;
		boxMese.getItems().addAll("01","02","03","04","05","06","07","08","09","10","11","12");
		if(boxMese.getItems().size()>0){
			boxMese.setValue(boxMese.getItems().get(0));
		}
	}
	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private ChoiceBox<String> boxMese;

	@FXML
	private Button btnCalcola;

	@FXML
	private Button btnUmidita;

	@FXML
	private TextArea txtResult;

	@FXML
	void doCalcolaSequenza(ActionEvent event) {
		if(boxMese.getValue()==null){
			txtResult.setText("Seleziona un mese");
			return;
		}
		
		txtResult.appendText(model.trovaSequenza(Integer.parseInt(boxMese.getValue()))+"\n");
	}

	
	
	@FXML
	void doCalcolaUmidita(ActionEvent event) {
		/*controllino*/
		if(boxMese.getValue()==null){
			txtResult.setText("Seleziona un mese");
			return;
		}
		
		
		String mese=boxMese.getValue();
		txtResult.appendText( model.getUmiditaMedia(mese) +"\n");
	}

	@FXML
	void initialize() {
		assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Meteo.fxml'.";
		assert btnCalcola != null : "fx:id=\"btnCalcola\" was not injected: check your FXML file 'Meteo.fxml'.";
		assert btnUmidita != null : "fx:id=\"btnUmidita\" was not injected: check your FXML file 'Meteo.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Meteo.fxml'.";
	}

	

}
