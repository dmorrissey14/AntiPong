package antipong;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *
 * User rectangle is the user controlled block used to try and avoid enemies.
 * User movement and rendering are handled.
 * 
 * @author duncan morrissey
 */
public class UserRectangle extends Sprite {
    
    public UserRectangle(double x, double y, double width, double height){
        super(x, y, width, height);
    }
    
    /**
     * Rendering a user rectangle draws a rectangle onto the
     * provided graphics context.
     * @param gc 
     */
    @Override
    public void render(GraphicsContext gc){
        gc.setFill(Color.RED);
        gc.fillRect(x, y, width, height);
    }
    
    /**
     * User rectangle only moves along the y-axis, as the user
     * tries to dodge incoming circles by moving up and down. Use 
     * velocity and time to adjust how far they should move.
     * @param time 
     */
    @Override
    public void update(double time){
        y += velocity * time;
    }
    
    /**
     * Checking intersection allows us to know if the game should be over.
     * 
     * We can really just check if the rectangle that defines a 
     * circle to be drawn in javafx overlaps with our user rectangle
     * @param c
     * @return 
     */
    public boolean intersects(EnemyCircle c){
        //circle boundaries
        double circleLeftX = c.getX();
        double circleRightX = c.getX() + c.getWidth();
        double circleUpperY = c.getY();
        double circleLowerY = c.getY() + c.getHeight();
        
        if (circleRightX < x){
            return false;
        }
        if (circleLeftX > (x + width)){
            return false;
        }
        if (circleUpperY > (y + height)){
            return false;
        }
        if (circleLowerY < y){
            return false;
        }
        
        return true;
    }
    
    /**
     * User Rectangles have their velocity constantly changed based
     * upon user input. 
     * @param velocity 
     */
    public void setVelocity(double velocity){
        this.velocity = velocity;
    }
    
    public double getLowerBoundY(){
        return y + height;
    }
}
