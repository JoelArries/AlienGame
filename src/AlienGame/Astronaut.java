package AlienGame;
import game2D.Sprite;

public class Astronaut extends GameObject{
    private int lives = 3;
    private float gravity = 0.0001f;
    private float walkSpeed = 0.05f;
    private float scaleX = 0.92f;
    private float scaleY = 0.7f;
    private boolean onGround;
    public boolean movingLeft;
    public boolean movingRight;



    public Astronaut(Sprite sprite) {
        super(sprite);
        sprite.setScale(scaleX, scaleY);
    }
    
    /**
     * Set the Astronaut's velocity to walkSpeed in the positive X-Direction.
     */
    public void moveRight(){
        sprite.setVelocityX(walkSpeed);
        movingRight = true;
        sprite.setScale(scaleX, scaleY);
        
    }

    /**
     * Set the Astronaut's velocity to walkSpeed in the negative X-Direction.
     */
    public void moveLeft(){
        sprite.setVelocityX(-walkSpeed);
        movingLeft = true;
        sprite.setScale(-scaleX, scaleY);
    }

    /**
     * Give the Astronaut a negative Y-Velocity.
     */
    public void jump(){
        if(onGround){sprite.setVelocityY(-0.1f); onGround = false;}
    }

    /**
     * Set the Astronaut's X-Velocity to zero.
     */
    public void stopMoving(){
        sprite.setVelocityX(0);
        movingLeft = false; movingRight = false;
    }

    /**
     * Call the methods relating to the direction determined by Key Events.
     */
    public void handleMovement(){
        if(movingRight){moveRight();}
        if(movingLeft){moveLeft();}
        if(!movingLeft && !movingRight){stopMoving();}
    }

    /**
     * Apply our Martian gravity to the Astronaut.
     * 
     * @param timeElapsed
     */
    public void applyGravity(long timeElapsed){
        sprite.setVelocityY(sprite.getVelocityY() + (gravity * timeElapsed));
    }

    /**
     * Set onGround to true.
     * 
     * @param onGround
     */
    public void setOnGround(boolean onGround){
        this.onGround = onGround;
    }

    /**
     * Is the Astronaut on the ground?
     * @return
     */
    public boolean getOnGround(){
        return onGround;
    }

    /**
     * Set the Astronaut's position.
     * 
     * @param xpos The X-Position of the Astronaut 
     * @param ypos The Y-Position of the Astronaut 
     */
    public void setPosition(int xpos, int ypos){
        sprite.setPosition(xpos, ypos);
    }

    /**
     * Remove a life from the Astronaut and set the position back to the start.
     */
    public void loseLife(){
        lives--;
        getSprite().setPosition(200, 325);

    }

    /**
     * 
     * @return The number of Astronaut's lives remaining.
     */
    public int getLives(){
        return lives;
    }
}