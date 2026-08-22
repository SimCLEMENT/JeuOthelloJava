package but.info.sae2_12.controller;

import but.info.sae2_12.model.Model;
import but.info.sae2_12.model.factory.FactoryCube;
import but.info.sae2_12.model.factory.IFactory;
import but.info.sae2_12.model.state.IState;
import coordinates.Mode;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class ControleurJeu {
	private MainController mainController;
	@FXML
	private CheckBox checkEdition;
	@FXML
	private HBox boxEditeur;
	@FXML
	private CheckBox checkCoordinate;
	@FXML 
	private ListView<String> listViewMode;
	private IFactory factory = new FactoryCube();

	public void setMainController(MainController mainController) {
		this.mainController = mainController;

		mainController.coordinateProperty().bind(checkCoordinate.selectedProperty());
		mainController.couleurGrisProperty().bind(color1.valueProperty());
		mainController.couleurGrisFonceProperty().bind(color2.valueProperty());
		mainController.couleurGrisClairProperty().bind(color3.valueProperty());
		mainController.epaisseurBordureProperty().bind(sliderEpaisseur.valueProperty());

		listViewMode.getItems().addAll("Cubique", "2D");
		listViewMode.getSelectionModel().selectedItemProperty().addListener((obs, old, newVal) -> {
			if (newVal.equals("Cubique")) {
				mainController.setMode(Mode.FLAT);
			} else {
				mainController.setMode(Mode.POINTY);
			}
		});
	}
	
	public ToggleGroup getG1() { return G1; }
	public ToggleGroup getG2() { return G2; }
	
	public boolean isNoir;
	public boolean isAnneau;
	
	@FXML
    private ToggleGroup G1;

    @FXML
    private ToggleGroup G2;
	
	@FXML
	private ColorPicker color1;
	@FXML
	private ColorPicker color2;
	@FXML
	private ColorPicker color3;
	
	@FXML
	private Slider sliderEpaisseur;
	
	
	@FXML
	public BooleanProperty checkEditionProperty() {
		return checkEdition.selectedProperty();
	}
	
	@FXML
	public void initialize() {
	    boxEditeur.disableProperty().bind(checkEdition.selectedProperty().not());
	    color1.setValue(Color.GRAY);
	    color2.setValue(Color.DARKGRAY);
	    color3.setValue(Color.LIGHTGRAY);
	    sliderEpaisseur.setValue(1);
	}

	@FXML
	public void onRandom(ActionEvent event) {
		IState etat = factory.randomGame();
		mainController.setModel(new Model(etat));
	}

	@FXML
	public void onTestState(ActionEvent event) {
		IState etat = factory.testState();
		mainController.setModel(new Model(etat));
	}

	@FXML
	public void onBlackLineTest(ActionEvent event) {
		IState etat = factory.stateForBlackLineTest();
		mainController.setModel(new Model(etat));
	}

	@FXML
	public void onWhiteLineTest(ActionEvent event) {
		IState etat = factory.stateForWhiteLineTest();
		mainController.setModel(new Model(etat));
	}

	@FXML
	public void onModeEdition(ActionEvent event) {
	}
	
	
	@FXML
	public void onNoir(ActionEvent event) {
		isNoir = true;
	}
	@FXML
	public void onBlanc(ActionEvent event) {
		isNoir = false;
	}
	@FXML
	public void onAnneau(ActionEvent event) {
		isAnneau = true;
	}
	@FXML
	public void onPion(ActionEvent event) {
		isAnneau = false;
	}
}
