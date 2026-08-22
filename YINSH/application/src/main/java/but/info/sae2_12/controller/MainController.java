package but.info.sae2_12.controller;


import java.net.URL;
import java.util.ResourceBundle;
import but.info.sae2_12.model.Model;
import but.info.sae2_12.model.Team;
import coordinates.Mode;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;

public class MainController implements Initializable{
	private final ObjectProperty<Color> couleurGris = new SimpleObjectProperty<>(Color.GRAY);
	private final ObjectProperty<Color> couleurGrisFonce = new SimpleObjectProperty<>(Color.DARKGRAY);
	private final ObjectProperty<Color> couleurGrisClair = new SimpleObjectProperty<>(Color.LIGHTGRAY);
		
	private final ObjectProperty<Model> model = new SimpleObjectProperty<>(new Model(null));
	private final ObjectProperty<InteractionMode> interactionMode = new SimpleObjectProperty<InteractionMode>(new ModeClassique(this));

	private final BooleanProperty winner = new SimpleBooleanProperty(false);
	private final ObjectProperty<Mode> mode = new SimpleObjectProperty<>(Mode.POINTY);
	private final BooleanProperty coordinate = new SimpleBooleanProperty(false);

	private final DoubleProperty epaisseurBordure = new SimpleDoubleProperty(1.0);

	@FXML
	protected CentralController centralController;
	@FXML
	protected ControleurJeu gameController;
	@FXML
	protected AIController aIController;
	@FXML
	protected BottomController bottomController;
	@FXML
	protected MenuController menuController;
	public ObjectProperty<Color> couleurGrisProperty() {
		return couleurGris;
	}
	public Color getCouleurGris() {
		return couleurGris.get();
	}
	public void setCouleurGris(Color c) {
		couleurGris.set(c);
	}


	public ObjectProperty<Color> couleurGrisFonceProperty() {
		return couleurGrisFonce;
	}
	public Color getCouleurGrisFonce() {
		return couleurGrisFonce.get();
	}
	public void setCouleurGrisFonce(Color c) {
		couleurGrisFonce.set(c);
	}


	public ObjectProperty<Color> couleurGrisClairProperty() {
		return couleurGrisClair;
	}
	public Color getCouleurGrisClair() {
		return couleurGrisClair.get();
	}
	public void setCouleurGrisClair(Color c) {
		couleurGrisClair.set(c);
	}


	public DoubleProperty epaisseurBordureProperty() {
		return epaisseurBordure;
	}
	public Double getEpaisseurBordure() {
		return epaisseurBordure.get();
	}
	public void setEpaisseurBordure(double e) {
		epaisseurBordure.set(e);
	}


	public ObjectProperty<Model> modelProperty() {
		return model;
	}
	public Model getModel() {
		return model.get();
	}
	public void setModel(Model m) {
		model.set(m);
	}


	public ObjectProperty<InteractionMode> interactionModeProperty() {
		return interactionMode;
	}
	public InteractionMode getInteractionMode() {
		return interactionMode.get();
	}
	public void setInteractionMode(InteractionMode im) {
		interactionMode.set(im);
	}


	public BooleanProperty winnerProperty() {
		return winner;
	}
	public Boolean getWinner() {
		return winner.get();
	}
	public void setWinner(Boolean b) {
		winner.set(b);
	}


	public ObjectProperty<Mode> modeProperty() {
		return mode;
	}
	public Mode getMode() {
		return mode.get();
	}
	public void setMode(Mode mo) {
		mode.set(mo);
	}


	public BooleanProperty coordinateProperty() {
		return coordinate;
	}
	public Boolean getCoordinate() {
		return coordinate.get();
	}
	public void setCoordinate(Boolean c) {
		coordinate.set(c);
	}






	public void initialize(URL location, ResourceBundle resources) { 
		centralController.setParams(this, getCouleurGrisFonce(), getCouleurGris(), getCouleurGrisClair());
		gameController.setMainController(this);
		aIController.setMainController(this);
		bottomController.setMainController(this);
		menuController.setMainController(this);

		interactionMode.bind(Bindings.createObjectBinding(() -> { 
			if (gameController.checkEditionProperty().get()) {
				return new ModeEdition(this);
			} else if (model.get() != null && !model.get().getCurrentState().getLines().isEmpty()) {
				return new ModeLigne(this);
			} else {
				return new ModeClassique(this);
			}
		}, gameController.checkEditionProperty(), model));

		winner.addListener((obs, oldVal, newVal) -> {
			if (newVal) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Gagnant");
				Team gagnant = model.get().getTurn().other();
				alert.setContentText("Le gagnant du jeu est : " + gagnant.getColor());
				alert.show();
			}
		});
	}
}
