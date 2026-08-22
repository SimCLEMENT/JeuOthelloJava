package but.info.sae2_12.controller;

import but.info.sae2_12.AI.MiniMax;
import but.info.sae2_12.AI.MinimaxAI;
import but.info.sae2_12.model.Model;
import but.info.sae2_12.model.Team;
import but.info.sae2_12.model.actions.Action;
import but.info.sae2_12.model.state.IState;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

public class AIController {
	
    @FXML
    private TextField victoireField;
    @FXML
    private TextField anneauxField;
    @FXML
    private TextField pionsField;
    @FXML
    private TextField ligneField;
    @FXML
    private TextField mobiliteField;
    @FXML
    private ProgressBar evaluationProgressBar;
	
    private MainController mainController;
    private MinimaxAI ai = new MinimaxAI(Team.BLACK, 3);
	
    public void setMainController(MainController controller) {
        this.mainController = controller;
        
        this.mainController.modelProperty().addListener((observable, ancienModele, nouveauModele) -> {
            if (nouveauModele != null) {
                updateEvaluationBar(nouveauModele.getCurrentState());
                nouveauModele.currentStateProperty().addListener((obs, ancienState, nouveauState) -> {
                    if (nouveauState != null) {
                        updateEvaluationBar(nouveauState);
                    }
                });
            }
        });
    }
	
    @FXML
    public boolean validerLesScores() {
        try {
            MiniMax.setWinScore(Double.parseDouble(victoireField.getText()));
            MiniMax.setRingRemovedWeight(Double.parseDouble(anneauxField.getText()));
            MiniMax.setPawnWeight(Double.parseDouble(pionsField.getText()));
            MiniMax.setNearLineWeight(Double.parseDouble(ligneField.getText()));
            MiniMax.setMobilityWeight(Double.parseDouble(mobiliteField.getText()));
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Mise a jour");
            alert.setContentText("Mise a jour des poids réussie.");
            
            alert.showAndWait();
            
            return true;
            
        } catch (NumberFormatException e) {
        	Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Erreur");
            alert.setHeaderText("Un problème est survenue lors du changement des poids.");
            alert.setContentText("Verifiez que ce sont bien des nombres saisient dans les différents champs.");
            
            alert.showAndWait();
            
            return false;
        }
    }

    public void updateEvaluationBar(IState currentState) {
        MiniMax minimax = new MiniMax();
	    
        double score = minimax.evaluate(currentState, Team.BLACK); 

        double pourcentage = (score + 100000.0) / 200000.0;

        pourcentage = Math.max(0.0, Math.min(1.0, pourcentage));

        evaluationProgressBar.setProgress(pourcentage);
    }
	
    @FXML
    public void afficherCoupIA() {
        if (mainController == null) {
            return; 
        }
        
        Model model = mainController.getModel();
        
        if (model == null) {
        	Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Erreur");
            alert.setHeaderText("Impossible de récupérer le model");
            alert.setContentText("Recherche du meilleur coup à échoué.");
            return;
        }
        
        IState currentState = model.getCurrentState();
        
        if(!this.validerLesScores()) {
            return;
        }
		
        Action meilleurCoup = ai.chooseMove(currentState);
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Meilleur coup");
        
        if (meilleurCoup != null) {
            alert.setHeaderText("Le meilleur coup est :");
            alert.setContentText(meilleurCoup.toString());
        } else {
            alert.setHeaderText("Aucun coup n'est possible.");
            alert.setContentText("Aucun coup n'est possible.");
        }
		
        alert.showAndWait();
    }
}