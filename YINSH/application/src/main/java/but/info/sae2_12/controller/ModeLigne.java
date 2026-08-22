package but.info.sae2_12.controller;

import java.util.Set;

import but.info.sae2_12.graphicElement.HexSquare;
import but.info.sae2_12.model.Model;
import coordinates.Coordinate;
import javafx.scene.input.MouseEvent;

public class ModeLigne extends InteractionMode {

	private HexSquare selectedLine;
	
	public ModeLigne(MainController mainController) {
		super(mainController);
	}

	@Override
	public void handleClick(MouseEvent event, HexSquare square) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void entered(MouseEvent event, HexSquare square) {
		Model model = mainController.modelProperty().get();
		if (selectedLine == null) { 
			for (Set<Coordinate> line : model.getPawnsLines()) {
				if (line.contains(square.getCoordinate())) {
					for (Coordinate yx : line) {
						mainController.centralController.map.get(yx).toggleIcon(true);
					}
				}
			}
		} else if (selectedLine != null && model.getRings(model.getTurn()).contains(square.getCoordinate())){
			square.toggleIcon(true);
		} else {
			square.setFill(square.getColor().darker());
		}
	}

	@Override
	public void exited(MouseEvent event, HexSquare square) {
		// TODO Auto-generated method stub
		Model model = mainController.modelProperty().get();
		
		for (Set<Coordinate> line : model.getPawnsLines()) {
			for (Coordinate yx : line) {
				mainController.centralController.map.get(yx).toggleIcon(false);
			}
		}
		square.toggleIcon(false);
		square.setFill(square.getColor());
	
	}

}
