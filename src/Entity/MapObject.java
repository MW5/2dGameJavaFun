package Entity;

import Main.GamePanel;
import TileMap.Tile;
import TileMap.TileMap;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public abstract class MapObject {
    
    //tile stuff
    protected TileMap tileMap;
    protected int tileSize;
    protected double xMap;
    protected double yMap;
    
    //position and vector
    protected double x;
    protected double y;
    protected double dx;
    protected double dy;
    
    //dimensions 
    protected int width;
    protected int height;
    
    //collision box
    protected int cWidth;
    protected int cHeight;
    
    //collision 
    protected int currRow;
    protected int currCol;
    protected double xDest;
    protected double yDest;
    protected double xTemp;
    protected double yTemp;
    protected boolean topLeft;
    protected boolean topRight;
    protected boolean bottomLeft;
    protected boolean bottomRight;
    
    //animation
    protected Animation animation;
    protected int currentAction;
    protected int previousAction;
    protected boolean facingRight;
    
    //movement
    protected boolean left;
    protected boolean right;
    protected boolean up;
    protected boolean down;
    protected boolean jumping;
    protected boolean falling;
    
    //movement attributes
    protected double moveSpeed;
    protected double maxSpeed;
        //deacceleration speed
    protected double stopSpeed;
    protected double fallSpeed;
    protected double maxFallSpeed;
    protected double jumpStart;
    protected double stopJumpSpeed;
    
    //constructor
    public MapObject(TileMap tm) {
        this.tileMap = tm;
        tileSize = tileMap.getTileSize();
    }
    
    //checks for collision between maps
    public boolean intersects (MapObject o) {
        Rectangle r1 = this.getRectangle();
        Rectangle r2 = o.getRectangle();
        return r1.intersects(r2);
    }
    
    public Rectangle getRectangle() {
        return new Rectangle(
            (int)x - cWidth,
            (int)y - cHeight,
            cWidth,
            cHeight);
    }
    
    public void calculateCorners(double x, double y) {
        int leftTile = (int)(x - cWidth / 2) / tileSize;
        int rightTile = (int)(x + cWidth / 2 - 1) / tileSize;
        int topTile = (int)(y - cHeight / 2) / tileSize;
        int bottomTile = (int)(y + cHeight / 2 - 1) / tileSize;
        if(topTile < 0 || bottomTile >= tileMap.getNumRows() ||
                leftTile < 0 || rightTile >= tileMap.getNumCols()) {
                topLeft = topRight = bottomLeft = bottomRight = false;
                return;
        }
        int tl = tileMap.getTileType(topTile, leftTile);
        int tr = tileMap.getTileType(topTile, rightTile);
        int bl = tileMap.getTileType(bottomTile, leftTile);
        int br = tileMap.getTileType(bottomTile, rightTile);
        topLeft = tl == Tile.BLOCKED;
        topRight = tr == Tile.BLOCKED;
        bottomLeft = bl == Tile.BLOCKED;
        bottomRight = br == Tile.BLOCKED;
    }
    
    public void checkTileMapCollision() {
        currCol = (int)x / tileSize;
        currRow = (int)y / tileSize;
        
        xDest = x + dx;
        yDest = y + dy;
        
        xTemp = x;
        yTemp = y;
        
        //y axis movement
        calculateCorners(x, yDest);
        //going up
        if (dy < 0) {
            if (topLeft || topRight) {
                dy = 0;
                yTemp = currRow * tileSize + cHeight / 2;
            } else {
                yTemp += dy;
            }
        }
        //going down
        if (dy > 0) {
            if(bottomLeft || bottomRight) {
                dy = 0;
                falling = false;
                yTemp = (currRow + 1) * tileSize - cHeight / 2;
            } else {
                yTemp += dy;
            }
        }
        //x axis movement
        calculateCorners(xDest, y);
        if (dx < 0) {
            if (topLeft || bottomLeft) {
                dx = 0;
                xTemp = currCol * tileSize + cWidth / 2;
            } else {
                xTemp += dx;
            }
        }
        if (dx > 0) {
            if (topRight || bottomRight) {
                dx = 0;
                xTemp = (currCol + 1) * tileSize - cWidth / 2;
            } else {
                xTemp += dx;
            }
        }
        //to see if we are falling
        if (!falling) {
            calculateCorners(x, yDest + 1);
            if (!bottomLeft && !bottomRight) {
                falling = true;
            }
        }
    }
    public int getX() {
        return (int)x;
    }
    public int getY() {
        return (int)y;
    }
    public int getWidth() {
        return (int) width;
    }
    public int getHeight() {
        return (int) height;
    }
    public int getCWidth() {
        return (int) cWidth;
    }
    public int getCHeight() {
        return (int) cHeight;
    }
    
    //regular position
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }
    //movement vector
    public void setVector(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }
    //map position
    public void setMapPosition() {
        xMap = tileMap.getX();
        yMap = tileMap.getY();
    }
    
    public void setLeft(boolean b) {
        left = b;
    }
    public void setRight(boolean b) {
        right = b;
    }
    public void setUp(boolean b) {
        up = b;
    }
    public void setDown(boolean b) {
        down = b;
    }
    public void setJumping(boolean b) {
        jumping = b;
    }
    
    public void draw(Graphics2D g) {
        if (facingRight) {
            g.drawImage(animation.getImage(),
                    (int)(x+xMap-width/2),
                    (int)(y+yMap-height/2),
                    null);
        } else {
            g.drawImage(animation.getImage(),
                    (int)(x+xMap-width/2+width),
                    (int)(y+yMap-height/2),
                    -width,
                    height,
                    null);
        }
    }

}
