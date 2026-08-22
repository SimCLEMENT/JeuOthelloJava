package but.info.sae2_12.graphicElement;


import but.info.sae2_12.model.tokens.Pawn;
import but.info.sae2_12.model.tokens.Ring;
import but.info.sae2_12.model.tokens.Token;
import coordinates.Coordinate;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;

public class HexSquare extends Polygon {
	
	public static final double HAUTEUR = 50;
	public static final double LARGEUR = 50;
	
	private Color couleur; //vient du contrôleur principal
	private Coordinate position; 
	private Shape contenu; //une certaine forme (pawn), une autre (ring) ou circle (case valide pour déplacement), ou rien
	private Label positionGraphique; // la position en texte, ne change pas
	
	public HexSquare(Coordinate p) {
		Double X = (double) p.to2DCoordinate().x() * LARGEUR/2;
		Double Y = (double) p.to2DCoordinate().y() * HAUTEUR *0.75;

		getPoints().addAll(
				X - LARGEUR / 2, Y -HAUTEUR /4,
				X, Y-HAUTEUR /2,
				X + LARGEUR / 2, Y -HAUTEUR /4,
				X + LARGEUR / 2, Y +HAUTEUR /4,
				X, Y+HAUTEUR /2,
				X - LARGEUR / 2, Y +HAUTEUR /4
				);		
		// heureusement que j'ai déjà fait du OpenGL pour comprendre que les coordonnées sont en vrac! (pas 2 par 2)
		
		this.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
			//appeler méthode interactionmode
		});
		
		this.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> {
			//appeler méthode interactionmode
		});
		
		this.addEventFilter(MouseEvent.MOUSE_EXITED, e -> {
			//appeler méthode interactionmode
		});
	}
	
	public void setToken(Token t) {
		int X = this.position.to2DCoordinate().x();
		int Y = position.to2DCoordinate().y();
		if (t instanceof Pawn) {
			contenu = new Circle(X, Y, LARGEUR /8); //il n'y a pas des masses de choix de Shape;
			switch (t.getTeam()) {					//j'ai pris des cercles de proprétés différentes.
			case WHITE:
				contenu.setFill(Color.grayRgb(255));
				break;
			case BLACK:
				contenu.setFill(Color.grayRgb(0));
			}
			contenu.setStrokeType(StrokeType.INSIDE);

		} else if (t instanceof Ring) {
			contenu = new Circle(X, Y, LARGEUR /2);
			switch (t.getTeam()) {
			case WHITE:
				contenu.setStroke(Color.grayRgb(255));
				break;
			case BLACK:
				contenu.setStroke(Color.grayRgb(0));
			}
		
		} else if (t != null) {
			throw new IllegalArgumentException("le token passé n'est ni pawn, ni ring, ni null");
		}
	}
	
	public Coordinate getCoordinate() {
		return position;
	}

	public Color getColor() {
		return couleur;
	}
	
	public void setColor(Color c) {
		this.couleur = c;
		this.setFill(couleur);
	}
	
	public void toggleIcon(boolean yes) {
		if (yes && contenu == null) {
			int x = getCoordinate().to2DCoordinate().x();
			int y = getCoordinate().to2DCoordinate().y();
			
			Circle icon  = new Circle(x, y, HexSquare.LARGEUR *0.9);
			
			icon.setStroke(Color.grayRgb(127));
			icon.setStrokeType(StrokeType.INSIDE);
			
			contenu = icon;
			
		} else {
			contenu = null;
		}
	}
}

