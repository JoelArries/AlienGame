package AlienGame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.SpinnerDateModel;

import game2D.*;

public class AlienGame2 extends GameCore{
    private Level level;
    private Astronaut astronaut;
    private Alien alien;
    private Tornado tornado;
    private Sprite astronautSprite, alienSprite, tornadoSprite;
    private Animation alienWalking, astronautWalking, astronautIdle, tornadoAnimation;

    static int screenWidth = 768;
    static int screenHeight = 576;

    long timeElapsed;

    private Image space, background, emptyHeart, fullHeart;

    public static void main(String[] args){
        AlienGame2 ag = new AlienGame2();
        ag.init();
        ag.run(false, screenWidth, screenHeight);
    }

    public void init(){
        level = new Level("mars");

        loadObjectAnimations();

        astronaut.getSprite().setPosition(200, 325);
        alien.getSprite().setPosition(350, 350);
        tornado.getSprite().setPosition(695, 280);

        level.addObject(alien);
        level.addObject(astronaut);
        level.addObject(tornado);

        setSize(level.getMap().getPixelWidth()/4, level.getMap().getPixelHeight());
        setVisible(true);

        loadBackgroundImages();

        initialiseGame();
        System.out.println(level.getMap());
    }

    public void initialiseGame(){
        timeElapsed = 0;
        astronaut.setPosition(200, 325);
        alien.setPosition(350, 325);
    }

    /**
     * The overriden draw method- used to draw the state of the game to the screen.
     * Set offsets, draw background images and hearts, determine which Astronaut animation should be used.
     * 
     * @param g The Graphics2D object to be drawn.
     */
    @Override
    public void draw(Graphics2D g) {
        int xOffset = -(int)astronaut.getSprite().getX() + 200;
        int yOffset = -(int)astronaut.getSprite().getY() + 300;

        g.drawImage(space, 0, 0, null);
        g.drawImage(background, 0, 0, null);
    
        level.draw(g, xOffset, yOffset);

        if(astronautSprite.getVelocityX() != 0 ){
            astronautSprite.setAnimation(astronautWalking);
        }
        else{
            astronautSprite.setAnimation(astronautIdle);
        }
        handleLives(g);

        String msg = "X: " + astronaut.getSprite().getX() + ". Y: " + astronaut.getSprite().getY();
        g.setColor(Color.WHITE);
        g.drawString(msg, 0, 100);
    }

    /**
     * Apply gravity to the Astronaut and handle movement requested by key presses and update the level.
     */
    public void update(long timeElapsed){
        if(tornado.getX()+20 >= astronaut.getSprite().getX() && tornado.getX()-30 <= astronaut.getSprite().getX()){
            float yDiff = tornado.getSprite().getY() - astronaut.getSprite().getY();
            astronaut.applyGravity(timeElapsed, yDiff);
        }
        else{
            astronaut.applyGravity(timeElapsed);
        }
        astronaut.handleMovement();
        alien.alienWalk(level.getMap());

        level.update(timeElapsed);

    }

    /**
     * Load animations for each sprite to be used when needed.
     */
    public void loadObjectAnimations(){
        astronautWalking = new Animation();
        astronautWalking.loadAnimationSeries("images/walk.png", 3, 4, 100, 6, 5);

        astronautIdle = new Animation();
        astronautIdle.loadAnimationSeries("images/walk.png", 3, 4, 100, 0, 1);
        
        alienWalking = new Animation();
        alienWalking.loadAnimationFromSheet("images/alienWalkingCropped.png", 6, 1, 100);
        
        tornadoAnimation = new Animation();
        tornadoAnimation.loadAnimationFromSheet("images/newTornadoCopy.png", 3, 3, 100);

        tornadoSprite = new Sprite(tornadoAnimation);
        astronautSprite = new Sprite(astronautIdle);
        alienSprite = new Sprite(alienWalking);
        tornado = new Tornado(tornadoSprite);
        astronaut = new Astronaut(astronautSprite);
        alien = new Alien(alienSprite);
    }

    /**
     * Load the background images to be drawn.
     */
    public void loadBackgroundImages(){
        space = new ImageIcon("images/space.png").getImage();
        background = new ImageIcon("images/rockBackground.png").getImage();

        fullHeart = new ImageIcon("images/fullHeart.png").getImage();
        emptyHeart = new ImageIcon("images/emptyHeart.png").getImage();

    }

    /*
     * What should happen when a key is pressed?
     */
    public void keyPressed(KeyEvent e){
        int keyPressed = e.getKeyCode();

        switch (keyPressed) {
            case KeyEvent.VK_RIGHT  :   astronaut.movingRight = true; break;
            case KeyEvent.VK_LEFT   :   astronaut.movingLeft = true; break;
            case KeyEvent.VK_UP     :   astronaut.jump(); break;
            case KeyEvent.VK_ESCAPE :   stop(); break;
            default                 :   break;
        }
    }

    /**
     * What should happen when a key is released?
     */
    public void keyReleased(KeyEvent e){
        int keyReleased = e.getKeyCode();

        switch (keyReleased) {
            case KeyEvent.VK_RIGHT  :   astronaut.movingRight = false; break;
            case KeyEvent.VK_LEFT   :   astronaut.movingLeft = false; break;
            default                 :   break;
        }
    }

    /**
     * Draw the number of full and empty hearts to the screen according to how many lives remain.
     * @param g
     */
    public void handleLives(Graphics2D g) {
        int lives = astronaut.getLives();

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