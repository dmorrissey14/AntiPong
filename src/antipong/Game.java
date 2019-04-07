package antipong;

import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import java.util.Hashtable;
import java.util.Random;

/**
 * The Game creates an instance of the AntiPong game. Upon instantiation, 
 * the game is passed a graphics context on which to render the user and enemies. 
 * Then methods to update the game field and controls elements of the game are exposed to
 * an outer class which can update on a timer.
 * @author duncan morrissey
 */
public class Game {
    // Game variables
    final int   upperY = 0,     // canvas dimesions
                lowerY = 565,
                leftX = 0,
                rightX = 800;
    
    final GraphicsContext gameArena;
    private String difficulty = "Easy";
    private int status = 0; // return 1 at end of game
    private Integer score = 0;
    
    //user variables
    final UserRectangle user;
    private Move userMove = Move.NONE;                          // what move is the user currently performing
    private Hashtable<Move, Double> userSpeed = new Hashtable();// what velocity is associated with that move?
    
    //enemy variables
    private ArrayList<EnemyCircle> enemies = new ArrayList<EnemyCircle>();
    private double enemySpeed = 75;
    private double lastEnemyTime = 0.0;                         // track how long it's been since an enemy was created
    private double timeBetweenEnemies = 1.3;                    // track how long we'd like between enemy generation
    
    public Game(GraphicsContext gc){
        gameArena = gc;
       
        //initialize the user
        user = new UserRectangle(600, 200, 50, 120);
        user.render(gc);
        // set initial speeds
        userSpeed.put(Move.UP, -100.0);
        userSpeed.put(Move.DOWN, 100.0);
        userSpeed.put(Move.NONE, 0.0);
    }
    
    /**
     * The difficulty level selected by a user essentially corresponds to 
     * the speed at which things happen. Higher difficulty lends to a faster
     * moving rectangle, faster moving circles, and less time between circle
     * generation.
     * @param difficulty 
     */
    public void setDifficulty(String difficulty){
        this.difficulty = difficulty;
        
        // make game faster for higher speeds
        if ( difficulty.equals("Medium") ){
            userSpeed.put(Move.UP, -200.0);
            userSpeed.put(Move.DOWN, 200.0);
            enemySpeed = 150;
            timeBetweenEnemies = 1.0;
        }
        if ( difficulty.equals("Difficult")){
            userSpeed.put(Move.UP, -300.0);
            userSpeed.put(Move.DOWN, 300.0);
            enemySpeed = 225;
            timeBetweenEnemies = 0.75;
        }
    }
    
    /**
     * Move user and enemies based on their velocity. 
     * Update score if needed, remove old enemies,
     * generate new enemies
     * @param time 
     */
    public void update(double time){
        moveUser(time);
        moveEnemies(time);
        
        for (EnemyCircle enemy : enemies ){
            // end game if they intersect
            if (user.intersects(enemy)){
                status = 1; //end game
            }
            //credit user score if they avoid the enemy successfully
            if (enemy.getX() + enemy.getWidth() > rightX){
                score += 1;
                enemy.setMissed(true);
                enemy.clear(gameArena);
            }
        }
        
        // Remove old enemies and create new if necessary
        enemies.removeIf(en -> (en.getMissed() == true));
        
        lastEnemyTime += time;
        if (lastEnemyTime > timeBetweenEnemies){
            createNewEnemy();
            lastEnemyTime = 0.0;
        }
    }
    
    
    // User methods
    public void setUserMove(Move m){
        userMove = m;
    }
    
    /** 
     * Clear the old rectangle, update its coordinates, render it again
     * @param time 
     */
    private void moveUser(double time){
        user.clear(gameArena);
        // --> check users coordinates and set to appropriate;
        user.setVelocity(calculateUserRectangleVelocity());
        user.update(time);
        user.render(gameArena);
    }
    
    /**
     * If the user is approaching the edge of the playing field, we want to adjust
     * their speed and prevent them from exiting the playing field. If the values
     * go over the max by 1 or two pixels we don't care as you can't even see the 
     * difference.
     * @return 
     */
    private double calculateUserRectangleVelocity(){
        double velocity = userSpeed.get(userMove);
        double userY = user.getY();
        double userLowerY = user.getLowerBoundY();
        
        // don't exceed top of playing field
        if (userMove.equals(Move.UP)){
            if (userY <= upperY){
                velocity = 0;
            }
        // don't exceed bottom of playing fields
        }else if (userMove.equals(Move.DOWN)){
            if (userLowerY >= lowerY){
                velocity = 0;
            }
        }
        
        return velocity;
    }
    
    // Enemy variables
    
    /**
     * For each enemy circle, clear its old location, update
     * its x-axis coordinate based on time passed, render its new
     * location.
     * @param time 
     */
    private void moveEnemies(double time){
        for( EnemyCircle enemy : enemies ){
            enemy.clear(gameArena);
            enemy.update(time);
            enemy.render(gameArena);
        }
    }
    
    /**
     * Randomly generate an enemy, but ensure that it is an appropriate
     * distance away from the last three enemies created.
     */
    private void createNewEnemy(){
        Random random = new Random();
        double newY;
        while(true){
            newY = random.nextDouble() * (650); //nextDouble is between 0 and 1, get a value between 0 and 650
            if (isValidNewEnemy(newY)){
                break;
            }
        }
        
        enemies.add(new EnemyCircle(10, newY, 50, 50, enemySpeed));
    }
    
    private boolean isValidNewEnemy(double y){
        // ensure new y value is not within 50 px of last 3 enemies
        double  value1, value2, value3;
        if (enemies.size() > 0){
            value1 = enemies.get(enemies.size() - 1).getY();
        }else{
            value1 = 5000;  //arbitrary value that won't be close to our random new value
        }
        if (enemies.size() > 1){
            value2 = enemies.get(enemies.size() - 2).getY();
        }else{
            value2 = 5000;
        }
        if (enemies.size() > 2){
            value3 = enemies.get(enemies.size() - 3).getY();
        }else{
            value3 = 5000;
        }
        
        if (java.lang.Math.abs( y - value1) < 75){ // ensure we are at least 100px away from last value
            return false;
        }
        if (java.lang.Math.abs( y - value2) < 75){
            return false;
        }
        if (java.lang.Math.abs( y - value3) < 75){
            return false;
        }
        
        return true;
    }
    
    // Game methods - expose these to outer timer to control transitions and ending game
    public Integer getStatus(){
        return status;
    }
    
    public Integer getScore(){
        return score;
    }
    
    /** 
     * Set the valid moves available to the user. Based on game 
     */
    public enum Move{
        UP,
        DOWN,
        NONE;
    }
}
