package but.info.sae2_12.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import but.info.sae2_12.graphicElement.HexSquare;
import coordinates.Coordinate;
import coordinates.CoordinateDoubled;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class CentralController implements Initializable {
	
	@FXML
	private Pane panelJeu;
	
	private Color c0;
	private Color c1;
	private Color c2;
	
	private MainController mc;
	
	int grid[][] = { //pratique pour la création des cases
			{4, 6},
			{3, 5, 7},
			{2, 4, 6, 8},
			{1, 3, 5, 7, 9},
			{2, 4, 6, 8},
			{1, 3, 5, 7, 9},
			{0, 2, 4, 6, 8, 10},
			{1, 3, 5, 7, 9},
			{0, 2, 4, 6, 8, 10},
			{1, 3, 5, 7, 9},
			{0, 2, 4, 6, 8, 10},
			{1, 3, 5, 7, 9},
			{0, 2, 4, 6, 8, 10},
			{1, 3, 5, 7, 9},
			{2, 4, 6, 8},
			{1, 3, 5, 7, 9},
			{2, 4, 6, 8},
			{3, 5, 7},
			{4, 6},
			};
	

	public HashMap<Coordinate, HexSquare> map = new HashMap<Coordinate, HexSquare>();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		for (int i = 0; i < 19; i++) {
			for (int y : grid[i]) {
				Coordinate coord = new CoordinateDoubled(y, i);
				HexSquare tile = new HexSquare(coord);
				map.put(coord, tile);
				
				panelJeu.getChildren().add(tile);
				tile.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> mc.getInteractionMode().handleClick(event, tile));
				tile.addEventFilter(MouseEvent.MOUSE_ENTERED, event -> mc.getInteractionMode().entered(event, tile));
				tile.addEventFilter(MouseEvent.MOUSE_EXITED, event -> mc.getInteractionMode().exited(event, tile));
			}
		}
	}
	
	public void setParams(MainController mc, Color c0, Color c1, Color c2) {
		this.mc = mc;
		this.c0 = c0;
		this.c1 = c1;
		this.c2 = c2;
		applyColors();
	}
	
	public void applyColors() {
		for (int i = 0; i < 19; i++) {
			int colorID = i%3;
			for (int y : grid[i]) {
				Coordinate coord = new CoordinateDoubled(y, i);
				switch (colorID) {
				case 0:
					map.get(coord).setColor(c0);
					break;
				case 1:
					map.get(coord).setColor(c1);
					break;
				case 2:
					map.get(coord).setColor(c2);
					break;
				}
			}
		}
	}
	
}
