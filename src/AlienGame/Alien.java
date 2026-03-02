package AlienGame;

import game2D.Sprite;
import game2D.TileMap;

public class Alien extends GameObject{
    private float walkSpeed = 0.01f;
    private int direction = 1;
    private float scaleX = 0.13f;
    private float scaleY = 0.058f;

    public Alien(Sprite sprite) {
        super(sprite);
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        sprite.setScale(scaleX, scaleY);
    }

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