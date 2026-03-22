package AlienGame;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import game2D.*;

public class Level{
    private TileMap tmap;
    private ArrayList<GameObject> gameObjects;
    private int partsCollected;
    private int topTile = 0;

    public Level(String mapName){
        tmap = new TileMap();
        tmap.loadMap("maps", mapName + ".txt");
        gameObjects = new ArrayList<>();
    }


    /**
     * Add a GameObject to the gameObjects ArrayList. 
     * 
     * @param obj the object to add
     */
    public void addObject(GameObject obj){
        gameObjects.add(obj);
    }

    /**
     * remove the specified GameObject from the gameObjects ArrayList.
     * The removed object will no longer be drawn.
     * 
     * @param obj   the object to remove
     */
    public void removeObject(GameObject obj){
        gameObjects.remove(obj);
    }

    /**
     * Return an ArrayList of GameObjects
     */
    public ArrayList<GameObject> getObjects(){
        return gameObjects;
    }

    /**
     * updates the GameObjects in the gameObjects ArrayList, checks for sprite/sprite collision, 
     * checks for sprite/tilemap collision.
     *  
     * @param timeElapsed
     */
    public void update(long timeElapsed){
        for(GameObject obj: gameObjects){
            obj.update(timeElapsed);
        }
        for(int i = 0; i<gameObjects.size(); i++){
            for(int j=i+1; j<gameObjects.size(); j++){
                GameObject a = gameObjects.get(i);
                GameObject b = gameObjects.get(j);

                if(boundingBoxCollision(a, b)){
                    handleCollision(a, b);
                }
            }
        }
        for(GameObject obj: gameObjects){
            if (obj instanceof Astronaut) {
                Astronaut a = (Astronaut)obj;
                checkFloorTileCollision(a);
                checkHorizontalCollision(a);
                checkTopTileCollision(a);
            }
        }

        
    }

    /**
     * Calls method to handle astronaut alien collision.
     * 
     * @param obj1
     * @param obj2
     */
    public void handleCollision(GameObject obj1, GameObject obj2){
        if(obj1 instanceof Astronaut && obj2 instanceof Alien){

            handleAstronautAlienCollision((Astronaut)obj1, (Alien)obj2);
        }
        if(obj2 instanceof Astronaut && obj1 instanceof Alien){

            handleAstronautAlienCollision((Astronaut)obj2, (Alien)obj1);
        }
    }

    /**
     * Handles the collision between an astronaut and an alien. Checks if the bottom of the astronaut makes
     * contact with the top of the alien, if it does, the alien is killed and the astronaut will bounce- leaving a 
     * pool of slime behind.
     * 
     * @param astronaut
     * @param alien
     */
    public void handleAstronautAlienCollision(Astronaut astronaut, Alien alien){

        
        Sprite astSprite = astronaut.getSprite();
        Sprite alnSprite = alien.getSprite();

        float astronautFoot = astSprite.getY() + astSprite.getHeight();
        float alienHead = alnSprite.getY();

        float collisionMargin = 1f;

        boolean falling = astSprite.getVelocityY()>0;

        boolean stomp = falling && astronautFoot <= alienHead+collisionMargin;

        if(stomp){
            killAlien(astronaut, alien);
        }
        else{
            astronaut.loseLife();
        }
        
    }

    /**
     * Remove the alien from the gameObjects ArrayList and replace the tile it is standing on
     * with a slime puddle tile.
     * Make the astronaut jump
     * 
     * @param astronaut
     * @param alien
     */
    public void killAlien(Astronaut astronaut, Alien alien){
        Sprite alienSprite = alien.getSprite();

        int tileX = (int)((alienSprite.getX() + alienSprite.getWidth()/ 2)/tmap.getTileWidth());
        int tileY = (int)((alienSprite.getY() + alienSprite.getHeight() + alienSprite.getHeight()) / tmap.getTileHeight());

        gameObjects.remove(alien);

        tmap.setTileChar('9', tileX, tileY);

        astronaut.getSprite().setVelocityY(-0.1f);
        astronaut.setOnGround(false);
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

    /**
     * check if the Astronaut's head is colliding with a tile. 
     * 
     * @param ast the Astronaut
     */
    public void checkTopTileCollision(Astronaut ast){
        Sprite s = ast.getSprite();

        float tileWidth = tmap.getTileWidth();
        float tileHeight = tmap.getTileHeight();

        int headTile = (int)(s.getY()/tileHeight);

        int leftTile = (int)((s.getX()+2)/tileWidth);
        int rightTile = (int)((s.getX()+s.getWidth()-2)/tileWidth);

        for(int i = leftTile; i <= rightTile; i++){
            char ch = tmap.getTileChar(i, headTile);

            if (ch != '.') {
                s.setY((topTile+1) * tileHeight);
                s.setVelocityY(0);
                break;
            }
        }
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
        int tileYBottom = (int)((s.getY() + s.getHeight()) / tileHeight); // bottom of sprite
        int tileYTop = (int)(s.getY() /tileHeight); //top of sprite
        topTile = tileYTop;
        
        char chBottom = tmap.getTileChar(tileX, tileYBottom);
        char chTop = tmap.getTileChar(tileX, tileYTop);

        //TODO:
        //check for other spaceship parts. If part is final part && partsCollected == 3 -> draw spaceship at end.
        if (chBottom == 'p' || chBottom == 'q' || chBottom =='s') {
            tmap.setTileChar('.', tileX, tileYBottom);
            ast.setOnGround(false);
            partsCollected++;
        }
        else if (chBottom != '.') {
            s.setY(tileYBottom * tileHeight - s.getHeight());
            s.setVelocityY(0);
            ast.setOnGround(true);
        }
        else{
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
     * Checks if the Astronaut is at the X and Y coordinates of the Spaceship, and if all parts are collected.
     * if these requirements are satisfied, true is returned
     * 
     * @param ast   the Astronaut object.
     * @return
     */
    public boolean atShip(Astronaut ast){
        Sprite astSprite = ast.getSprite();
        if(astSprite.getX() >= 1970.0 && astSprite.getY() == 224 && partsCollected == 3){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * 
     * @return the TileMap
     */
    public TileMap getMap(){
        return tmap;
    }
}