import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import game2D.*;

//Student ID: 3329293

public class AlienGame extends GameCore{

    //Screen Constants
    static int screenWidth = 768;
    static int screenHeight = 576;

    //Game Constants
    float gravity = 0.0001f;
    float walkSpeed = 0.05f;

    //Game Resources
    Animation alienWalking;
    Sprite alien;

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

        alien = new Sprite(alienWalking);

        initialiseGame();
        System.out.println(tmap);
    }

    public void initialiseGame(){
        timeElapsed = 0;
        alien.setScale(0.15f);
        alien.setPosition(200, 200);
        alien.setVelocity(0, 0);
        alien.show();
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

        //show score implemented here if wanted

        //debug mode here when wanted
    }

    //drawCollidedTiles()

    public void update(long timeElapsed){
        alien.setVelocityY(alien.getVelocityY()+(gravity*timeElapsed));
        alien.setAnimationSpeed(1.0f);

        alien.update(timeElapsed);
    }

    public void handleScreenEdge(Sprite s, TileMap tmap, long timeElapsed){
        float difference = s.getY() + s.getHeight() - tmap.getPixelHeight();
        if (difference > 0)
        {
        	// Put the player back on the map according to how far over they were
        	s.setY(tmap.getPixelHeight() - s.getHeight() - (int)(difference)); 
        	
        	// and make them bounce
        	s.setVelocityY(-s.getVelocityY()*0.75f);
        }
    }

    public void keyPressed(KeyEvent e){

    }

    public boolean boundingBoxCollision(Sprite s1, Sprite s2){return false;}

    public void checkTileCollision(Sprite s, TileMap tmap){

    }

    public void keyReleased(KeyEvent e){

    }
}
