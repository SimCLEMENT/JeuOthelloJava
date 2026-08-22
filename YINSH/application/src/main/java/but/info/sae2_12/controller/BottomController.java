package but.info.sae2_12.controller;



import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class BottomController {
	private MainController mainController;
	
	@FXML
	private Label labelTour;
	@FXML
	private Label labelMode;
	
	public void setMainController(MainController mainController) {
		this.mainController = mainController;
		
		mainController.modelProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal != null) {
				labelTour.setText("Tour : " + newVal.getTurn());
				newVal.currentStateProperty().addListener((obs2, old2, new2) -> {
				    if (new2 != null) {
				        labelTour.setText("Tour : " + new2.turn());
				    }
				});
			}
		});
		
		mainController.interactionModeProperty().addListener((obs, oldVal, newVal) -> {
			labelMode.setText("Mode : " + newVal.getClass().getSimpleName());
		});
	}
}
