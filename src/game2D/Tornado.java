package game2D;

import AlienGame.GameObject;

public class Tornado extends GameObject{

    public Tornado(Sprite sprite) {
        super(sprite);
    }

    public float getX(){
        return sprite.getX();
    }

    public float getY(){
        return sprite.getY();
    }
}
