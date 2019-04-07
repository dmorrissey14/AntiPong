package antipong;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Set our scene to our initial landing page and launch
 * the application
 * 
 * @author duncan morrissey
 */
public class AntiPong extends Application {
        
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("LandingPage.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setTitle("Anti Pong");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
