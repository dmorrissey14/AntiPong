package antipong;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Enemy circles are generated at set intervals based upon difficulty, then
 * progress across the screen from left to right. 
 * 
 * @author duncan morrissey
 */
public class EnemyCircle extends Sprite{
    /*  x =         x coordinate of upper left bound of oval
        y =         y coordinate of upper left bound of oval
        width =     width at center of oval
        height =    height at center of oval */
    
    /*
    Game checks missed to verify what circles have been successfully
    dodged by the user and should be removed from the game.
    */
    private boolean missed = false;
    
    
    /** Enemy circles have their velocity set once for the duration of their
     * lifespan, whereas user rectangle has its velocity change based on user
     * actions.
     */
    public EnemyCircle(double x, double y, double width, double height, double velocity){
        super(x, y, width, height);
        this.velocity = velocity;
    }
    
    /**
     * Enemy circles are, of course, drawing a circle on the canvas.
     * @param gc 
     */
    public void render(GraphicsContext gc){
        gc.setFill(Color.BLUE);
        gc.fillOval(x, y, width, height);
    }
    
    /**
     * Circles move horizontally across the game arena. At each time iteration,
     * move them based upon the velocity and time passed.
     * @param time 
     */
    public void update(double time){
        x += velocity * time;
    }
    
    public boolean getMissed(){
        return missed;
    }
    
    public void setMissed(boolean b){
        missed = b;
    }
}
