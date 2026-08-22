package but.info.sae2_12.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import but.info.sae2_12.model.Model;
import but.info.sae2_12.model.Team;
import but.info.sae2_12.model.factory.FactoryCube;
import but.info.sae2_12.model.factory.FactoryDoubled;
import but.info.sae2_12.model.factory.IFactory;
import but.info.sae2_12.model.state.State;
import but.info.sae2_12.model.tokens.Pawn;
import but.info.sae2_12.model.tokens.Ring;
import but.info.sae2_12.model.tokens.Token;
import coordinates.Coordinate;
import coordinates.CoordinateCube;
import coordinates.CoordinateDoubled;
import coordinates.Point;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Window;

public class MenuController {
	
	private MainController mainController;
	
	@FXML
	private MenuItem itemAPropos;
	@FXML
	private MenuItem itemCharger;
	@FXML
	private MenuItem itemSauvegarder;
	
	@FXML
	public void onAPropos(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("A Propos");
		alert.setContentText("Groupe 6 composé de :\nCHUQUET Anael\nGAUMONT Gabriel\nCLEMENT Simon\nDE MAZZI Leandro");
		alert.show();
	}
	
	@FXML
	public void onCharger(ActionEvent event) {
		Window window = itemCharger.getParentPopup().getOwnerWindow();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Charger une partie");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier Yinsh (*.yns)", "*.yns"));
		
		File file = fileChooser.showOpenDialog(window);

		if (file != null) {
			try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
				
				// Sécurité : Vérification du Magic Number
				String magicNumber = reader.readLine();
				if (magicNumber == null || !magicNumber.equals("SAE212")) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Erreur de chargement");
					alert.setHeaderText("Format de fichier invalide");
					alert.setContentText("Ce fichier n'est pas une sauvegarde Yinsh valide ou est corrompu.");
					alert.showAndWait();
					return;
				}

				//Lecture de l'état global
				String etatGlobal = reader.readLine();
				String[] etatInfos = etatGlobal.split(",");
				Team tour = etatInfos[0].equals("N") ? Team.BLACK : Team.WHITE;
				
				//Lecture du terrain
				Map<Coordinate, Token> board = null;
				String ligne;
				
				while ((ligne = reader.readLine()) != null) {
					String[] data = ligne.split(",");
					if(data.length < 5) continue;
					
					String type = data[0];
					Team equipe = data[1].equals("N") ? Team.BLACK : Team.WHITE;
					String coordType = data[2];
					
					//utilisation de la factori pour générer le plateau vide 
					// avec toute les case (même les case nulle) au premier passage
					if (board == null) {
						IFactory factory = coordType.equals("CUBE") ? new FactoryCube() : new FactoryDoubled();
						board = new HashMap<>(factory.emptyState().board());
					}
					
					Coordinate coord = null;
					if (coordType.equals("CUBE")) {
						coord = new CoordinateCube(Integer.parseInt(data[3]), Integer.parseInt(data[4]), Integer.parseInt(data[5]));
					} else if (coordType.equals("DOUBLED")) {
						coord = new CoordinateDoubled(Integer.parseInt(data[3]), Integer.parseInt(data[4]));
					}
					
					if (coord != null) {
						Token token = type.equals("R") ? new Ring(equipe) : new Pawn(equipe);
						board.put(coord, token); // Remplace les case vide par la pièce chargée
					}
				}
				
				// Mise à jour
				if (board != null) {
					State nouvelEtat = new State(board, tour, List.of());
										
					System.out.println("Partie chargée avec succès !");
				}

			} catch (Exception e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText("Erreur lors de la lecture : " + e.getMessage());
				alert.show();
			}
		}
	}
	
	@FXML
	public void onSauvegarder(ActionEvent event) {
		// Vérification qu'une partie est bien en cours avant de la sauvegardé
		if (mainController.getModel() == null || mainController.getModel().getCurrentState() == null) {
			Alert alert = new Alert(AlertType.WARNING, "Aucune partie en cours à sauvegarder !");
			alert.show();
			return;
		}

		Window window = itemSauvegarder.getParentPopup().getOwnerWindow();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Sauvegarder la partie");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier Yinsh (*.yns)", "*.yns"));
		
		File file = fileChooser.showSaveDialog(window);

		if (file != null) {
			try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
				
				writer.println("SAE212");

				// L'état global (Exemple: N pour Noir, B pour Blanc)
				Team tour = mainController.getModel().getCurrentState().turn(); 
				String charTour = (tour == Team.BLACK) ? "N" : "B";
				writer.println(charTour + ",P"); // P pour identifier la Phase

				//Les données du terrain
				Map<Coordinate, Token> board = mainController.getModel().getCurrentState().board();
				for (Map.Entry<Coordinate, Token> entry : board.entrySet()) {
					Token token = entry.getValue();
					Coordinate coord = entry.getKey();
					
					// On ne sauvegarde que la où il y a une pièce
					if (token != null) {
						String type = (token instanceof Ring) ? "R" : "P";
						String equipe = (token.getTeam() == Team.BLACK) ? "N" : "B";
						
						if (coord instanceof CoordinateCube) {
							CoordinateCube cc = (CoordinateCube) coord;
							writer.println(type + "," + equipe + ",CUBE," + cc.getQ() + "," + cc.getR() + "," + cc.getS());
						} else if (coord instanceof CoordinateDoubled) {
							CoordinateDoubled cd = (CoordinateDoubled) coord;
							Point pt = cd.to2DCoordinate();
							writer.println(type + "," + equipe + ",DOUBLED," + pt.y() + "," + pt.x());
						}
					}
				}
				System.out.println("Partie sauvegardée avec succès !");

			} catch (Exception e) {
				Alert alert = new Alert(AlertType.ERROR, "Erreur lors de la sauvegarde : " + e.getMessage());
				alert.show();
			}
		}
	}
	
	public void setMainController(MainController mainController) {
		this.mainController = mainController;
	}
	
	@FXML
	private void initialize() {
		itemAPropos.setOnAction(e -> onAPropos(e));
		itemCharger.setOnAction(e -> onCharger(e));
		itemSauvegarder.setOnAction(e -> onSauvegarder(e));
	}
}