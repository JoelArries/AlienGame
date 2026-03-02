package AlienGame;

import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;

import game2D.*;

public class AlienGame2 extends GameCore{
    private Level level;
    private Astronaut astronaut;
    private Alien alien;
    private Sprite astronautSprite, alienSprite;
    private Animation alienWalking, astronautWalking, astronautIdle;

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

        level.addObject(alien);
        level.addObject(astronaut);

        setSize(level.getMap().getPixelWidth()/4, level.getMap().getPixelHeight());
        setVisible(true);

        loadBackgroundImages();

        //initialiseGame();
        System.out.println(level.getMap());
    }

    public void initialiseGame(){
        timeElapsed = 0;
        astronaut.setPosition(200, 325);
        alien.setPosition(350, 350);
    }

    @Override
    public void draw(Graphics2D g) {
        int xOffset = -(int)astronaut.getSprite().getX() + 200;
        int yOffset = -(int)astronaut.getSprite().getY() + 300;

        g.drawImage(space, 0, 0, null);
        g.drawImage(background, 0, 0, null);

        level.draw(g, xOffset, yOffset);
    }

    public void update(long timeElapsed){
        astronaut.applyGravity(timeElapsed);
        alien.alienWalk(level.getMap());

        level.update(timeElapsed);
    }

    public void loadObjectAnimations(){
        astronautWalking = new Animation();
        astronautWalking.loadAnimationSeries("images/walk.png", 3, 4, 100, 6, 5);

        astronautIdle = new Animation();
        astronautIdle.loadAnimationSeries("images/walk.png", 3, 4, 100, 0, 1);
        
        alienWalking = new Animation();
        alienWalking.loadAnimationFromSheet("images/alienWalkingCropped.png", 6, 1, 100);
        
        astronautSprite = new Sprite(astronautIdle);
        alienSprite = new Sprite(alienWalking);

        astronaut = new Astronaut(astronautSprite);
        alien = new Alien(alienSprite);
    }

    public void loadBackgroundImages(){
        space = new ImageIcon("images/space.png").getImage();
        background = new ImageIcon("images/rockBackground.png").getImage();

        fullHeart = new ImageIcon("images/fullHeart.png").getImage();
        emptyHeart = new ImageIcon("images/emptyHeart.png").getImage();

    }
}