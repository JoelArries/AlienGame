package AlienGame;

import game2D.Sprite;
import game2D.TileMap;

public class Alien extends GameObject{
    private float walkSpeed = 0.05f;
    private int direction = 1;
    private float scaleX = 0.13f;
    private float scaleY = 0.058f;

    public Alien(Sprite sprite) {
        super(sprite);
        sprite.setScale(scaleX, scaleY);
    }

    /**
     * Set the position of the Alien.
     * @param xpos The X-Position of the Alien.
     * @param ypos The Y-Position of the Alien.
     */
    public void setPosition(int xpos, int ypos){
        sprite.setPosition(xpos, ypos);
    }

    /**
     * Set the Alien on it's way. 
     * Handles collisions with the Alien and the Tile Map,
     * when a collision is detected the Alien will turn and walk the other way.
     * @param tmap
     */
    public void alienWalk(TileMap tmap){
        float tileWidth = tmap.getTileWidth();
        float tileHeight = tmap.getTileHeight();

        sprite.setVelocityX(walkSpeed * direction);

        int tileY = (int) ((sprite.getY() + sprite.getHeight() / 2) / tileHeight);
        int tileX;
        if (direction == 1) {
            tileX = (int) ((sprite.getX() + sprite.getWidth()) / tileWidth);
        } else {
            tileX = (int) ((sprite.getX()) / tileWidth);
        }


        char ch = tmap.getTileChar(tileX, tileY);
        if (ch != '.') { 
            direction *= -1;
            sprite.setScale(scaleX * direction, scaleY);
        }
    }
    
}