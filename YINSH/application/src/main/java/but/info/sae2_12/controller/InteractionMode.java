package but.info.sae2_12.controller;

import but.info.sae2_12.graphicElement.HexSquare;
import javafx.scene.input.MouseEvent;

public abstract class InteractionMode {
	
	protected MainController mainController;
	
	public InteractionMode(MainController mainController) {
		this.mainController = mainController;
	}
	
	public abstract void handleClick(MouseEvent event, HexSquare square);
	public abstract void entered(MouseEvent event, HexSquare square);
	public abstract void exited(MouseEvent event, HexSquare square);

	
	public MainController getMainController() {
		return mainController;
	}
}
