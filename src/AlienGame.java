import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
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

    //Game State Flags
    boolean moveRight = false;
    boolean moveLeft = false;
    boolean idle = true;
    //Not sure about this one 
    boolean turning = false;

    //Game Resources
    ArrayList<Sprite> sprites = new ArrayList<>();

    Sprite alien;
    Sprite astronaut;

    Animation alienWalking, astronautWalking;

    TileMap tmap = new TileMap();

    long timeElapsed;

    @SuppressWarnings("serial")


    public static void main(String[] args){
        AlienGame alienGame = new AlienGame();
        alienGame.init();
        alienGame.run(false, screenWidth, screenHeight);
    }

    public void init(){
        tmap.loadMap("maps", "mars.txt");

        setSize(tmap.getPixelWidth()/4, tmap.getPixelHeight());
        setVisible(true);

        alienWalking = new Animation();
        alienWalking.loadAnimationFromSheet("images/spritesheet6.png", 6, 1, 100);
        astronautWalking = new Animation();
        astronautWalking.loadAnimationSeries("images/walk.png", 3, 4, 100, 6, 5);

        alien = new Sprite(alienWalking);
        astronaut = new Sprite(astronautWalking);

        initialiseGame();
        System.out.println(tmap);
    }

    public void initialiseGame(){
        timeElapsed = 0;
        //scale the alien to be the width and height of a tile.
        alien.setScale(0.13f,0.058f);
        alien.setPosition(200, 200);
        alien.setVelocity(0, 0);
        alien.show();

        //scale the astronaut too
        astronaut.setScale(0.92f, 0.7f);
        astronaut.setPosition(100, 100);
        astronaut.setVelocity(0, 0);
        astronaut.show();
        sprites.add(alien);
        sprites.add(astronaut);
    }

    public void draw(Graphics2D g){
        //make sure to draw "back to front": BackG->ForeG->sprite
        int xOffset = -(int)alien.getX() + 200;
        int yOffset = -(int)alien.getY() + 200;

        g.setBackground(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());

        //offsets are applied to clouds here and then drawn

        //offsets applied to tilemap and drawn
        tmap.draw(g, xOffset, yOffset);

        alien.setOffsets(xOffset, yOffset);
        alien.drawTransformed(g);
        astronaut.setOffsets(xOffset, yOffset);
        astronaut.drawTransformed(g);


        //show score implemented here if wanted

        //debug mode here when wanted
    }


    public void update(long timeElapsed){
        /*
        * TODO: 
        * iterate through sprite arraylist updating velocity
        * and animation speed for each sprite it concerns
         */

        alien.setVelocityY(alien.getVelocityY()+(gravity*timeElapsed));
        alien.setAnimationSpeed(1.0f);
        astronaut.setVelocityY(astronaut.getVelocityY() + (gravity*timeElapsed));
        astronaut.setAnimationSpeed(1.0f);

        if(moveRight){
            astronaut.setVelocityX(walkSpeed);
        }
        else{
            astronaut.setVelocityX(0);
        }
        
        for(Sprite s : sprites){
            checkFloorTileCollision(s, tmap);
            s.update(timeElapsed);
            //handleScreenEdge(s, tmap, timeElapsed);
        }
        
    }

    public void handleScreenEdge(Sprite s, TileMap tmap, long timeElapsed){
        /*
        * TODO:
        * implement this with tile collision instead after initial rough tests
        */

        float difference = s.getY() + s.getHeight() - tmap.getPixelHeight();
        if (difference > 0)
        {
        	// Put the player back on the map according to how far over they were
        	s.setY(tmap.getPixelHeight() - s.getHeight() - (int)(difference)); 
        	
        	// and make them bounce
        	s.setVelocityY(-s.getVelocityY()*0.5f);
        }
    }

    public void keyPressed(KeyEvent e){
        int keyPressed = e.getKeyCode();

        switch(keyPressed)
        {
            case KeyEvent.VK_RIGHT  : moveRight = true; break;
        }
    }

    public boolean boundingBoxCollision(Sprite s1, Sprite s2){return false;}


    public void checkFloorTileCollision(Sprite s, TileMap tmap){
        float tileWidth = tmap.getTileWidth();
        float tileHeight = tmap.getTileHeight();

        int tileX = (int)((s.getX() + s.getWidth()/2) / tileWidth);
        int tileY = (int)((s.getY() + s.getHeight()) / tileHeight); // bottom of sprite
        char ch = tmap.getTileChar(tileX, tileY);
        if (ch != '.') {
            s.stop();
        }
    }

    public void keyReleased(KeyEvent e){
        int keyReleased = e.getKeyCode();

        switch (keyReleased) {
            case KeyEvent.VK_RIGHT  : moveRight = false; break;
                
        }
    }
}
