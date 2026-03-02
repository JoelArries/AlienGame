package AlienGame;
import game2D.Sprite;

public class Astronaut extends GameObject{
    private float gravity = 0.0001f;
    private float walkSpeed = 0.05f;
    private float scaleX = 0.92f;
    private float scaleY = 0.7f;
    private boolean onGround;
    private boolean isMoving;

    public Astronaut(Sprite sprite) {
        super(sprite);
        sprite.setScale(scaleX, scaleY);
    }
    
    public void moveRight(){
        sprite.setVelocityX(walkSpeed);
        isMoving = true;
    }

    public void moveLeft(){
        sprite.setVelocityX(-walkSpeed);
        isMoving = true;
    }

    public void jump(){
        if(onGround){sprite.setVelocityY(-0.1f); onGround = false;}
    }

    public void stop(){
        sprite.stop();
    }

    public void applyGravity(long timeElapsed){
        sprite.setVelocityY(sprite.getVelocityY() + (gravity * timeElapsed));
    }

    public void setOnGround(boolean onGround){
        this.onGround = onGround;
    }

    public boolean isMoving(){
        return isMoving;
    }

    public void setPosition(int xpos, int ypos){
        sprite.setPosition(xpos, ypos);
    }
}