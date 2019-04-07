package antipong;

import javafx.scene.canvas.GraphicsContext;

/**
 * A sprite is one of the enemy or user objects rendered in the game.
 * In our case, these will be the rectangle controlled by the user, and
 * the circles that our user is trying to avoid.
 *
 * @author duncan morrissey
 */
public abstract class Sprite {
    protected double x, y, width, height, velocity;
    
    public Sprite(double x, double y, double width, double height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    /**
     * Draw the implemented sprite on the game canvas
     * @param gc - the graphics context which to draw.
     */
    public abstract void render(GraphicsContext gc);
    
    /** 
     * Sprites have a velocity that is set in each game iteration.
     * Update sets their new location on the game board based on time passed
     * and their velocity.
     * @param time 
     */
    public abstract void update(double time);
    
    /**
     * There isn't a "clearOval" method, so we need to use clearRect to clear
     * circles or rectangles. Therefore, keep it in parent class.
     * @param gc 
     */
    public void clear(GraphicsContext gc){
        gc.clearRect(x, y, width, height);
    }
    
    
    /* GETTERS AND SETTERS */
    public double getY(){
        return y;
    }
    
    public double getX(){
        return x;
    }
    
    public double getWidth(){
        return width;
    }
    
    public double getHeight(){
        return height;
    }
}
