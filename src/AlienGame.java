import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import game2D.*;

//Student ID: 3329293
/*
Tile height: 32. Tile width: 32.
alien height: 552. alien width: 251
 */

public class AlienGame extends GameCore{

    //Screen Constants
    static int screenWidth = 768;
    static int screenHeight = 576;

    //Game Constants
    float gravity = 0.0001f;
    float walkSpeed = 0.05f;
    float alienScaledX = 0.13f;
    float alienScaledY = 0.058f;
    int alienDirection = 1;

    //Game State Flags
    boolean moveRight = false;
    boolean moveLeft = false;
    boolean idle = true;
    boolean onGround = false;
    boolean alienHitWall;

    //Game Resources
    ArrayList<Sprite> sprites = new ArrayList<>();

    int lives = 3;
    int partsCollected = 0;

    Sprite alien, astronaut;

    Animation alienWalking, astronautWalking, astronautIdle;

    TileMap tmap = new TileMap();

    Image space;
    Image background;
    Image tallRock;
    Image fullHeart;
    Image emptyHeart;

    long timeElapsed;
    boolean debug = false;

    @SuppressWarnings("serial")


    public static void main(String[] args){
        AlienGame alienGame = new AlienGame();
        alienGame.init();
        alienGame.run(false, screenWidth, screenHeight);
    }

    /**
     * loads background, animations, and the tilemap.
     * tilemap is printed to the screen to ensure it is correct.
     */
    public void init(){
        space = new ImageIcon("images/space.png").getImage();
        background = new ImageIcon("images/rockBackground.png").getImage();

        fullHeart = new ImageIcon("images/fullHeart.png").getImage();
        emptyHeart = new ImageIcon("images/emptyHeart.png").getImage();

        tmap.loadMap("maps", "mars.txt");

        setSize(tmap.getPixelWidth()/4, tmap.getPixelHeight());
        setVisible(true);

        alienWalking = new Animation();
        alienWalking.loadAnimationFromSheet("images/alienWalkingCropped.png", 6, 1, 100);

        astronautWalking = new Animation();
        astronautWalking.loadAnimationSeries("images/walk.png", 3, 4, 100, 6, 5);

        astronautIdle = new Animation();
        astronautIdle.loadAnimationSeries("images/walk.png", 3, 4, 100, 0, 1);

        alien = new Sprite(alienWalking);

        astronaut = new Sprite(astronautIdle);
        initialiseGame();
        System.out.println(tmap);
    }

    /**
     * starts by scaling and showing both astronaut and alien.
     * adds the sprites to an array list of sprites: sprites.
     */
    public void initialiseGame(){
        timeElapsed = 0;
        //scale the alien to be the width and height of a tile.
        alien.setScale(0.13f,0.058f);
        alien.setPosition(350, 350);
        alien.setVelocity(0, 0);
        alien.show();

        //scale the astronaut too
        astronaut.setScale(0.92f, 0.7f);
        astronaut.setPosition(200, 325);
        astronaut.setVelocity(0, 0);
        astronaut.show();

        sprites.add(alien);
        sprites.add(astronaut);
    }

    /**
     * draws the current state of the game to the screen.
     * debug allows for bounding boxes to be drawn around sprites.
     * drawTransformed must be used since sprites are scaled.
     * 
     * @param g the Graphics2D object to be drawn
     */
    public void draw(Graphics2D g){
        //make sure to draw "back to front": BackG->ForeG->sprite
        int xOffset = -(int)astronaut.getX() + 200;
        int yOffset = -(int)astronaut.getY() + 300;

        g.drawImage(space, 0, 0, null);
        
        g.drawImage(background, 0, 0, null);

        //offsets applied to tilemap and drawn
        tmap.draw(g, xOffset, yOffset);

        alien.setOffsets(xOffset, yOffset);
        alien.drawTransformed(g);
        astronaut.setOffsets(xOffset, yOffset);
        astronaut.drawTransformed(g);

        String msg = "Parts collected: " + partsCollected+"/3";
        g.setColor(Color.WHITE);
        g.drawString(msg, 0, 50);


        handleLives(g);

        //debug mode here when wanted
        if(debug){
            for(Sprite s: sprites){
                s.drawBoundingBox(g);
            }
        }
    }


    /**
     * update sprites, check for collisions.
     * 
     * @param timeElapsed the time elapsed since the game started.
     */
    public void update(long timeElapsed){
        /*
        * TODO: 
        * iterate through sprite arraylist updating velocity
        * and animation speed for each sprite it concerns
         */

        if(moveRight){
            astronaut.setScale(0.92f, 0.7f);
            astronaut.setVelocityX(walkSpeed);
        }
        else if(moveLeft){
            astronaut.setScale(-0.92f, 0.7f);
            astronaut.setVelocityX(-walkSpeed);
        }
        else{
            astronaut.setVelocityX(0);
        }
        alien.setVelocityY(0);
        alien.setAnimationSpeed(1.0f);
        astronaut.setVelocityY(astronaut.getVelocityY() + (gravity*timeElapsed));
        astronaut.setAnimationSpeed(1.0f);

        alienWalk();

        
        for(Sprite s : sprites){
            s.update(timeElapsed);
            checkFloorTileCollision(s, tmap);
        }
        if(boundingBoxCollision(astronaut, alien)){
            System.out.println("Collision");
            lives--;
            astronaut.setPosition(200, 325);
        }

        checkHorizontalCollision(astronaut);
    }

    /**
     * checks if the bounding boxes of two sprites overlap. 
     * if the boxes overlap, a collision has occured between the sprites
     * 
     * @param s1 a sprite in the game
     * @param s2 another sprite in the game
     * @return
     */
    public boolean boundingBoxCollision(Sprite s1, Sprite s2){
        return ((s1.getX() + s1.getWidth() > s2.getX()) &&
                (s1.getX() < (s2.getX() + s2.getWidth())) &&
                ((s1.getY() + s1.getHeight() > s2.getY()) &&
                        (s1.getY() < s2.getY() + s2.getHeight())));
    }

    /**
     * checks for tile collisions on the horizontal plane.
     * sprite- tile map collison
     * 
     * @param s the sprite for which the collison is checked
     */
    public void checkHorizontalCollision(Sprite s){
        float tileWidth = tmap.getTileWidth();
        float tileHeight = tmap.getTileHeight();

        int topTile = (int)(s.getY()  / tileHeight);
        int bottomTile = (int)((s.getY() + s.getHeight() -1) / tileHeight);

        if(moveRight){

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

        if(moveLeft) { 

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
 * checks for tile collision with the top of the sprite
 * 
 * @param s the sprite for which the collision is checked
 */
    public void checkTopSpriteCollision(Sprite s){
        //TODO:
    }

    /**
     * checks for tile collision with the bottom of the player and the tile map
     * 
     * @param s the sprite 
     * @param tmap the tilemap
     */
    public void checkFloorTileCollision(Sprite s, TileMap tmap){
        float tileWidth = tmap.getTileWidth();
        float tileHeight = tmap.getTileHeight();

        int tileX = (int)((s.getX() + s.getWidth()/2) / tileWidth); // middle of sprite
        int tileY = (int)((s.getY() + s.getHeight()) / tileHeight); // bottom of sprite
        
        char ch = tmap.getTileChar(tileX, tileY);
        //TODO:
        //check for other spaceship parts. If part is final part && partsCollected == 3 -> draw spaceship at end.
        if (ch == 'p') {
            tmap.setTileChar('.', tileX, tileY);
            onGround = false;
            partsCollected++;
        }else if (ch == 'x') {
            onGround = false;
        }
        else if (ch != '.') {
            s.setY(tileY * tileHeight - s.getHeight());
            s.setVelocityY(0);
            onGround = true;
        }else{
            onGround = false;
        }
    }

    /**
     * overrides the GameCore keyPressed to check custom keys
     * 
     * @param e the event that has been generated
     */
    public void keyPressed(KeyEvent e){
        int keyPressed = e.getKeyCode();

        switch(keyPressed)
        {
            case KeyEvent.VK_RIGHT  : moveRight = true; astronaut.setAnimation(astronautWalking);break;
            case KeyEvent.VK_LEFT   : moveLeft = true; astronaut.setAnimation(astronautWalking);  break;
            case KeyEvent.VK_UP     : if(onGround){astronaut.setVelocityY(-0.1f); onGround = false;} break;
            case KeyEvent.VK_D      : debug = !debug; break;
            case KeyEvent.VK_L      : lives--;
            case KeyEvent.VK_ESCAPE : stop(); break;
        }
    }
 
    /**
     * once a key has been pressed, what should happen when it is released?
     * 
     * @param e the event that has been generated
     */
    public void keyReleased(KeyEvent e){
        int keyReleased = e.getKeyCode();

        switch (keyReleased) {
            case KeyEvent.VK_RIGHT  : moveRight = false; astronaut.setAnimation(astronautIdle);break;
            case KeyEvent.VK_LEFT   : moveLeft = false; astronaut.setAnimation(astronautIdle); break;
                
        }
    }

    /**
     * sets the alien on its way 
     * checks if the alien has encountered an obstacle and turns them around if so
     */
    public void alienWalk() {
        float tileWidth = tmap.getTileWidth();
        float tileHeight = tmap.getTileHeight();

        alien.setVelocityX(walkSpeed * alienDirection);

        int tileY = (int) ((alien.getY() + alien.getHeight() / 2) / tileHeight);
        int tileX;
        if (alienDirection == 1) {
            tileX = (int) ((alien.getX() + alien.getWidth()) / tileWidth);
        } else {
            tileX = (int) ((alien.getX()) / tileWidth);
        }


        char ch = tmap.getTileChar(tileX, tileY);
        if (ch != '.') { 
            alienDirection *= -1;
            alien.setScale(alienScaledX * alienDirection, alienScaledY);
        }
    }

    /**
     * draws the player's remaining lives
     * 
     * @param g Graphics2D object to be drawn
     */
    public void handleLives(Graphics2D g) {
        switch(lives){
            case 3  :   g.drawImage(fullHeart, getWidth()-150, 50, null);
                        g.drawImage(fullHeart, getWidth()-100, 50, null);
                        g.drawImage(fullHeart, getWidth()-50, 50, null); 
                        break;
           
            case 2  :   g.drawImage(fullHeart, getWidth()-150, 50, null);
                        g.drawImage(fullHeart, getWidth()-100, 50, null);
                        g.drawImage(emptyHeart, getWidth()-50, 50, null); 
                        break;

            case 1  :   g.drawImage(fullHeart, getWidth()-150, 50, null);
                        g.drawImage(emptyHeart, getWidth()-100, 50, null);
                        g.drawImage(emptyHeart, getWidth()-50, 50, null); 
                        break;

            case 0  :   g.drawImage(emptyHeart, getWidth()-150, 50, null);
                        g.drawImage(emptyHeart, getWidth()-100, 50, null);
                        g.drawImage(emptyHeart, getWidth()-50, 50, null); 
                        break;
        }
    }
}
