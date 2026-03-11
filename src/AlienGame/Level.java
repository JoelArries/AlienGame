package AlienGame;
import java.awt.Color;
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
    //TODO:
    //add any more gameObjects to the JavaDoc if they are implemented.

    /**
     * Add a GameObject to the gameObjects ArrayList. 
     * GameObjects can be Aliens, Astronauts, 
     * 
     * @param obj the object to add
     */
    public void addObject(GameObject obj){
        gameObjects.add(obj);
    }

    public ArrayList<GameObject> getObjects(){
        return gameObjects;
    }

    /**
     * Update game objects, check for tile and sprite-sprite collision
     * 
     * @param timeElapsed
     */
    public void update(long timeElapsed){
        for(GameObject obj1 : gameObjects){
            obj1.update(timeElapsed);
            if(obj1 instanceof Astronaut){
                checkFloorTileCollision((Astronaut)obj1);
                checkHorizontalCollision((Astronaut)obj1);
            }
            for(GameObject obj2 : gameObjects){
                if(obj1 != obj2 && boundingBoxCollision(obj1, obj2)){
                    if(obj1 instanceof Astronaut){((Astronaut)obj1).loseLife();}
                    if(obj2 instanceof Astronaut){((Astronaut)obj2).loseLife();}
                }
            }
        }
    }

    /**
     * Draw the Tile Map, GameObjects, and a message informing the player of
     * how many parts have been collected.
     * 
     * @param g the Graphics2D object to draw
     * @param xOffset The offset applied to the Tile Map in the X-direction
     * @param yOffset The offset applied to the Tile Map in the Y-direction
     */
    public void draw(Graphics2D g, int xOffset, int yOffset){
        tmap.draw(g, xOffset, yOffset);

        String Msg = "Parts Collected: ";
        g.setColor(Color.WHITE);
        if(partsCollected < 3){
            Msg = Msg + partsCollected+"/3";
        }
        else{
            Msg = "All parts collected! Board the spaceship.";
        }

        g.drawString(Msg, 0, 50);

        for (GameObject obj : gameObjects) {
            obj.draw(g, xOffset, yOffset);
        }
    }

    public void checkTopTileCollision(Astronaut ast){
        Sprite s = ast.getSprite();

        float tileWidth = tmap.getTileWidth();
        float tileHeight = tmap.getTileHeight();

    }

    /**
     * Check for a collision between the foot of Astronaut and the Tile Map.
     * Updates the value of partsCollected when the astronaut collides with a ship part.
     * 
     * @param ast the Astronaut object for which the collisions are handled.
     */
    public void checkFloorTileCollision(Astronaut ast){
        Sprite s = ast.getSprite();

        float tileWidth = tmap.getTileWidth();
        float tileHeight = tmap.getTileHeight();

        int tileX = (int)((s.getX() + s.getWidth()/2) / tileWidth); // middle of sprite
        int tileY = (int)((s.getY() + s.getHeight()) / tileHeight); // bottom of sprite
        
        char ch = tmap.getTileChar(tileX, tileY);
        //TODO:
        //check for other spaceship parts. If part is final part && partsCollected == 3 -> draw spaceship at end.
        if (ch == 'p' || ch == 'q' || ch=='s') {
            tmap.setTileChar('.', tileX, tileY);
            ast.setOnGround(false);
            partsCollected++;
        }
        /*else if (ch == 'x') {
            ast.setOnGround(false);
            ast.loseLife();
        }   Shouldnt be needed since the tornado is there, cba rewriting it if not though */
        else if (ch != '.') {
            s.setY(tileY * tileHeight - s.getHeight());
            s.setVelocityY(0);
            ast.setOnGround(true);
        }else{
            ast.setOnGround(false);
        }
    
    }

    /**
     * Check for horizontal collisions between the Astronaut and the Tile Map.
     * Updates the value of partsCollected when the Astronaut collides with a ship part.
     * 
     * @param ast The Astronaut object for which the collisions are handled.
     */
    public void checkHorizontalCollision(Astronaut ast){
        Sprite s = ast.getSprite();
        float tileWidth = tmap.getTileWidth();
        float tileHeight = tmap.getTileHeight();

        int topTile = (int)(s.getY()  / tileHeight);
        int bottomTile = (int)((s.getY() + s.getHeight() -1) / tileHeight);

        if(ast.movingRight){

            int rightTile = (int)((s.getX() + s.getWidth()) / tileWidth);

            for(int y = topTile; y<=bottomTile; y++){
                if(tmap.getTileChar(rightTile, y) == 'p' || tmap.getTileChar(rightTile, y) == 'q' || tmap.getTileChar(rightTile, y) == 's'){
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

    /**
     * Detect overlap of two GameObjects' bounding boxes to determine a collision.
     * 
     * @param obj1 GameObject 1
     * @param obj2 GameObject 2
     * @return true if collision, false if not
     */
    public boolean boundingBoxCollision(GameObject obj1, GameObject obj2){
        Sprite s1 = obj1.getSprite();
        Sprite s2 = obj2.getSprite();

        return ((s1.getX() + s1.getWidth() > s2.getX()) &&
                (s1.getX() < (s2.getX() + s2.getWidth())) &&
                ((s1.getY() + s1.getHeight() > s2.getY()) &&
                        (s1.getY() < s2.getY() + s2.getHeight())));
    }

    /**
     * 
     * @return the TileMap
     */
    public TileMap getMap(){
        return tmap;
    }
}