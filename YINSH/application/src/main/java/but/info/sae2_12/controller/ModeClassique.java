package but.info.sae2_12.controller;

import but.info.sae2_12.graphicElement.HexSquare;
import but.info.sae2_12.model.Model;
import javafx.scene.input.MouseEvent;

public class ModeClassique extends InteractionMode {

	private HexSquare selected;
	
	public ModeClassique(MainController mainController) {
		super(mainController);
	}

	@Override
	public void handleClick(MouseEvent event, HexSquare square) {
		Model model = mainController.modelProperty().get();
		if (selected != null && 
				model.getCurrentState().availableMoves(selected.getCoordinate()).contains(square.getCoordinate())) {
			model.moveRing(selected.getCoordinate(), square.getCoordinate());
			selected = null;
		} else if (selected == null) {
			selected = square;
		} else {
			selected = null;
		}
	}

	@Override
	public void entered(MouseEvent event, HexSquare square) {
		Model model = mainController.modelProperty().get();
		if (selected != null && 
				model.getCurrentState().availableMoves(selected.getCoordinate()).contains(square.getCoordinate())) {
			
			square.toggleIcon(true);
			
		} else {
			square.setFill(square.getColor().darker());
		}
	}

	@Override
	public void exited(MouseEvent event, HexSquare square) {
		square.toggleIcon(false);
		square.setFill(square.getColor());
	}

}
