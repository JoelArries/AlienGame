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
    
    public void moveRight(){
        sprite.setVelocityX(walkSpeed);
        movingRight = true;
        sprite.setScale(scaleX, scaleY);
        
    }

    public void moveLeft(){
        sprite.setVelocityX(-walkSpeed);
        movingLeft = true;
        sprite.setScale(-scaleX, scaleY);
    }

    public void jump(){
        if(onGround){sprite.setVelocityY(-0.1f); onGround = false;}
    }

    public void stopMoving(){
        sprite.setVelocityX(0);
        movingLeft = false; movingRight = false;
    }

    public void handleMovement(){
        if(movingRight){moveRight();}
        if(movingLeft){moveLeft();}
        if(!movingLeft && !movingRight){stopMoving();}
    }

    public void applyGravity(long timeElapsed){
        sprite.setVelocityY(sprite.getVelocityY() + (gravity * timeElapsed));
    }

    public void setOnGround(boolean onGround){
        this.onGround = onGround;
    }

    public boolean getOnGround(){
        return onGround;
    }

    public void setPosition(int xpos, int ypos){
        sprite.setPosition(xpos, ypos);
    }

    public void loseLife(){
        lives--;
    }

    public int getLives(){
        return lives;
    }
}