package antipong;

import antipong.Game.Move;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class. Render our game field, instantiate a
 * new game instance, and kick off the game timer so that the user
 * can actually play. Also handle up and down key strokes for user
 * to control the rectangle
 *
 * @author duncan morrissey
 */
public class GameplayController implements Initializable {
    
    private String difficulty;
    private Game game;
    
    // control timer passed between animation cycles
    private Long lastNanoTime = new Long( System.nanoTime() ); // used for game timer
    private AnimationTimer timer;
    
    //handle overlap of key strokes
    private boolean down = false;
    private boolean up = false;
    
    @FXML
    Label scoreLabel; 
    
    @FXML
    Canvas gameArena;
    
    /**
     * Initializes the controller class. Create a new game by instantiating
     * it with our graphics context, then run the game, starting the animation
     * timer.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        GraphicsContext gc = gameArena.getGraphicsContext2D();
        game = new Game(gc);
        runGame();
    }    
    
    // pass difficulty from initial screen to our gameplay
    public void setDifficulty(String newDifficulty){
        this.difficulty = newDifficulty;
        game.setDifficulty(difficulty);
    }
    
    @FXML
    public void setUserMove(KeyEvent ke){
        if (ke.getCode().equals(KeyCode.DOWN)){
            game.setUserMove(Move.DOWN);
        }
        if (ke.getCode().equals(KeyCode.UP)){
            game.setUserMove(Move.UP);
        }
    }
    
    @FXML
    public void clearUserMove(KeyEvent ke){
        game.setUserMove(Move.NONE);
    }
        
    public void runGame(){
        timer = new AnimationTimer(){
            public void handle(long currentNanoTime){
                double elapsedTime = (currentNanoTime - lastNanoTime.longValue()) / 1000000000.0;
                lastNanoTime = currentNanoTime;
                
                game.update(elapsedTime);
                scoreLabel.setText(game.getScore().toString());
                if (game.getStatus() == 1){
                    timer.stop();
                    try{
                        endGame();
                    }catch(IOException e){
                        System.out.println(e.getMessage());
                    }
                }
            }
        };
        
        timer.start();
    }
    
    /**
     * Transfer to game end screen and display score.
     */
    @FXML
    public void endGame() throws IOException {
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("EndGame.fxml")
        );
        
        Parent endGameParent = loader.load();
        Scene endGameScene = new Scene(endGameParent);
        
        Stage stage = (Stage)gameArena.getParent().getScene().getWindow();
        stage.setScene(endGameScene);
        
        EndGameController controller = loader.<EndGameController>getController();
        controller.setScore(game.getScore());
        
        stage.show();
    }
}
