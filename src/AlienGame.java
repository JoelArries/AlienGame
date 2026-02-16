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
    }

    public void init(){
        tmap.loadMap("maps", "mars.txt");

        setSize(tmap.getPixelWidth()/4, tmap.getPixelHeight());
        setVisible(true);

        alienWalking = new Animation();
        alienWalking.loadAnimationFromSheet("images/spritesheet6.png", 6, 1, 100);

        alien = new Sprite(alienWalking);
    }

    public void initialiseGame(){

    }

    public void draw(Graphics2D g){

    }

    public void update(long timeElapsed){

    }

    public void keyPressed(KeyEvent e){

    }

    public boolean boundingBoxCollision(Sprite s1, Sprite s2){return false;}

    public void checkTileCollision(Sprite s, TileMap tmap){

    }

    public void keyReleased(KeyEvent e){

    }
}
