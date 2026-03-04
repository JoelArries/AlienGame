package AlienGame;
import java.awt.Graphics2D;
import java.util.ArrayList;

import game2D.*;

public class Level{
    private TileMap tmap;
    private ArrayList<GameObject> gameObjects;
    private int partsCollected;

    public Level(String mapName){
        tmap = new TileMap();
        tmap.loadMap("maps", mapName + ".txt");
        gameObjects = new ArrayList<>();
    }

    public void addObject(GameObject obj){
        gameObjects.add(obj);
    }

    public void update(long timeElapsed){
        for(GameObject obj : gameObjects){
            obj.update(timeElapsed);
            if(obj instanceof Astronaut){
                checkFloorTileCollision((Astronaut)obj);
            }
        }
    }

    public void draw(Graphics2D g, int xOffset, int yOffset){
        tmap.draw(g, xOffset, yOffset);

        for (GameObject obj : gameObjects) {
            obj.draw(g, xOffset, yOffset);
        }
    }

    private void checkFloorTileCollision(Astronaut ast){
        Sprite s = ast.getSprite();

        float tileWidth = tmap.getTileWidth();
        float tileHeight = tmap.getTileHeight();

        int tileX = (int)((s.getX() + s.getWidth()/2) / tileWidth); // middle of sprite
        int tileY = (int)((s.getY() + s.getHeight()) / tileHeight); // bottom of sprite
        
        char ch = tmap.getTileChar(tileX, tileY);
        //TODO:
        //check for other spaceship parts. If part is final part && partsCollected == 3 -> draw spaceship at end.
        if (ch == 'p') {
            tmap.setTileChar('.', tileX, tileY);
            ast.setOnGround(false);
            partsCollected++;
        }else if (ch == 'x') {
            ast.setOnGround(false);
        }
        else if (ch != '.') {
            s.setY(tileY * tileHeight - s.getHeight());
            s.setVelocityY(0);
            ast.setOnGround(true);
        }else{
            ast.setOnGround(false);
        }
    
    }

    public TileMap getMap(){
        return tmap;
    }
}