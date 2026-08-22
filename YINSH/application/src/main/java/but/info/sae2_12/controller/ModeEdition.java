package but.info.sae2_12.controller;

import but.info.sae2_12.graphicElement.HexSquare;
import but.info.sae2_12.model.Team;
import but.info.sae2_12.model.tokens.Pawn;
import but.info.sae2_12.model.tokens.Ring;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class ModeEdition extends InteractionMode {

	public ModeEdition(MainController mainController) {
		super(mainController);
	}

	@Override
	public void handleClick(MouseEvent event, HexSquare square) {
		if (event.getButton() == MouseButton.PRIMARY) {
			if (mainController.gameController.isNoir) {
				if (mainController.gameController.isAnneau ) {
					mainController.getModel().getBoard().put(square.getCoordinate(), new Ring(Team.BLACK));
				} else {
					mainController.getModel().getBoard().put(square.getCoordinate(), new Pawn(Team.BLACK));
				}
			} else {
				if (mainController.gameController.isAnneau ) {
					mainController.getModel().getBoard().put(square.getCoordinate(), new Ring(Team.WHITE));
				} else {
					mainController.getModel().getBoard().put(square.getCoordinate(), new Pawn(Team.WHITE));
				}
			}
		}
		
		if (event.getButton() == MouseButton.SECONDARY) {
			getMainController().getModel().removeToken(square.getCoordinate());
		}
	}

	@Override
	public void entered(MouseEvent event, HexSquare square) {
		

	}

	@Override
	public void exited(MouseEvent event, HexSquare square) {
		// TODO Auto-generated method stub

	}

}
