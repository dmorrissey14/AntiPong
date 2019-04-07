package antipong;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

/**
 *
 * @author duncan morrissey
 */
public class LandingPageController implements Initializable {
    
    @FXML
    private ChoiceBox difficultyPicker;
    
    @FXML
    private Button startButton;
    
    /**
     * 
     * @param event
     * @throws IOException 
     * 
     * Transition to actual game play from opening landing page.
     */
    @FXML 
    private void handleStartButtonAction(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("Gameplay.fxml")
        );
        
        Parent landingPageParent = loader.load();
        Scene gamePlayScene = new Scene(landingPageParent);
        
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(gamePlayScene);
        
        // pass difficulty to next scene
        GameplayController controller = loader.<GameplayController>getController();
        String test = (String)difficultyPicker.getValue();
        controller.setDifficulty(test);
        
        stage.show();
        gamePlayScene.getRoot().requestFocus(); // so key events are processed
    }

    /**
     * 
     * @param url
     * @param rb 
     * 
     * Initialize our picker to display difficulty settings.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        difficultyPicker.setItems(FXCollections.observableArrayList("Easy", "Medium", "Difficult"));
        difficultyPicker.setValue("Easy");
    }    
    
}
