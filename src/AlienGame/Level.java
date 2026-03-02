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
        }
        //handleCollisions
    }

    public void draw(Graphics2D g, int xOffset, int yOffset){
        tmap.draw(g, xOffset, yOffset);

        for (GameObject obj : gameObjects) {
            obj.draw(g, xOffset, yOffset);
        }
    }

    //handleCollisions

    public TileMap getMap(){
        return tmap;
    }
}