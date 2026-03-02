package AlienGame;
import java.awt.Graphics2D;

import game2D.*;

public abstract class GameObject {
    protected Sprite sprite;

    /**
     * GameObject constructor.
     * @param sprite
     */
    public GameObject(Sprite sprite){
        this.sprite = sprite;
    }

    public void update(long timeElapsed){
        sprite.update(timeElapsed);
    }

    /**
     * Draw the current state of the game to the screen.
     * Using drawTransformed since all sprites in the game are scaled.
     * @param g the Graphics2D object to be drawn
     * @param xOffset x-offset relative to specific on screen position
     * @param yOffset y-offset relative to specific on screen position
     * @param isTransformed has the sprite been scaled?
     */
    public void draw(Graphics2D g, int xOffset, int yOffset){
        sprite.setOffsets(xOffset, yOffset);
        sprite.drawTransformed(g);
        
    }

    /**
     * 
     * @return the sprite
     */
    public Sprite getSprite(){
        return sprite;
    }
}
