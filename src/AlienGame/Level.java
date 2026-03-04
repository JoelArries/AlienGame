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
        for(GameObject obj1 : gameObjects){
            obj1.update(timeElapsed);
            if(obj1 instanceof Astronaut){
                checkFloorTileCollision((Astronaut)obj1);
                checkHorizontalCollision((Astronaut)obj1);
            }
            for(GameObject obj2 : gameObjects){
                if(obj1 != obj2 && boundingBoxCollision(obj1, obj2)){
                    if(obj1 instanceof Astronaut){obj1.getSprite().setPosition(200, 325); ((Astronaut)obj1).loseLife();}
                    if(obj2 instanceof Astronaut){obj2.getSprite().setPosition(200, 325); ((Astronaut)obj2).loseLife();}
                }
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

    public void checkHorizontalCollision(Astronaut ast){
        Sprite s = ast.getSprite();
        float tileWidth = tmap.getTileWidth();
        float tileHeight = tmap.getTileHeight();

        int topTile = (int)(s.getY()  / tileHeight);
        int bottomTile = (int)((s.getY() + s.getHeight() -1) / tileHeight);

        if(ast.movingRight){

            int rightTile = (int)((s.getX() + s.getWidth()) / tileWidth);

            for(int y = topTile; y<=bottomTile; y++){
                if(tmap.getTileChar(rightTile, y) == 'p'){
                    tmap.setTileChar('.', rightTile, y);
                    partsCollected++;
                }
                else if(tmap.getTileChar(rightTile, y) != '.'){
                    s.setX(rightTile * tileWidth - s.getWidth());
                    s.setVelocityX(0);
                }
                
            }
        }

        if(ast.movingLeft) { 

        int leftTile = (int)(s.getX() / tileWidth);

        for(int y = topTile; y <= bottomTile; y++) {
            if(tmap.getTileChar(leftTile, y) != '.') {
                s.setX((leftTile + 1) * tileWidth);
                s.setVelocityX(0);
            }
        }
        }
    }

    public boolean boundingBoxCollision(GameObject obj1, GameObject obj2){
        Sprite s1 = obj1.getSprite();
        Sprite s2 = obj2.getSprite();

        return ((s1.getX() + s1.getWidth() > s2.getX()) &&
                (s1.getX() < (s2.getX() + s2.getWidth())) &&
                ((s1.getY() + s1.getHeight() > s2.getY()) &&
                        (s1.getY() < s2.getY() + s2.getHeight())));
    }

    public TileMap getMap(){
        return tmap;
    }
}