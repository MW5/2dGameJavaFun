package TileMap;

import Main.GamePanel;
import java.awt.*;
import java.awt.image.*;

import java.io.*;
import javax.imageio.ImageIO;

public class TileMap {
    //position
    double x;
    double y;
    
    //bounds
    private int xmin;
    private int ymin;
    private int xmax;
    private int ymax;
    
    //tutorial author invention to smoothen scrool
    private double tween;
    
    //map
    private int[][] map;
    private int tileSize;
    private int numRows;
    private int numCols;
    private int width;
    private int height;
    
    //tileset
    private BufferedImage tileSet;
    private int numTilesAcross;
    private Tile[][] tiles;
    
    //drawing
    private int rowOffset;
    private int colOffset;
    private int numRowsToDraw;
    private int numColsToDraw;
    
    public TileMap(int tileSize) {
        this.tileSize = tileSize;
        numRowsToDraw = GamePanel.HEIGHT / tileSize + 2;
        numColsToDraw = GamePanel.WIDTH / tileSize + 2;
        tween = 0.07;
    }
    
    //loads the tiles from image
    public void loadTiles(String s) {
        try {
            //loads whole image
            tileSet = ImageIO.read(getClass().getResourceAsStream(s));
            numTilesAcross = tileSet.getWidth() / tileSize;
            tiles = new Tile[2][numTilesAcross];
            
            BufferedImage subImage;
            //cuts image into subimages and places them into 2d array
            for (int col = 0; col < numTilesAcross; col++) {
                subImage = tileSet.getSubimage(col * tileSize, 0, tileSize, tileSize);
                tiles[0][col] = new Tile (subImage, Tile.NORMAL);
                
                subImage = tileSet.getSubimage(col * tileSize, tileSize, tileSize, tileSize);
                tiles[1][col] = new Tile (subImage, Tile.BLOCKED);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void loadMap(String s) {
        try {
            //prepare file and reader
            InputStream in = getClass().getResourceAsStream(s);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            
            //get size
            numCols = Integer.parseInt(br.readLine());
            numRows = Integer.parseInt(br.readLine());
            
            //create map
            map = new int[numRows][numCols];
            width = numCols * tileSize;
            height = numRows * tileSize;
            
            //delimiter
            String delims = "\\s+";
            //read map file
            for (int row = 0; row < numRows; row++) {
                String line = br.readLine();
                String [] tokens = line.split(delims);
                for (int col = 0; col < numCols; col++) {
                    map[row][col] = Integer.parseInt(tokens[col]);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public int getTileSize () {
        return tileSize;
    }
    public int getX () {
        return (int) x;
    }
    public int getY () {
        return (int) y;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public int getTileType (int row, int col) {
        int rc = map[row][col];
        int r = rc / numTilesAcross;
        int c = rc % numTilesAcross;
        return tiles[r][c].getType();
    }
    public void setPosition (double x, double y) {
        //this.x += (x - this.x) * tween;
        //this.y += (y - this.y) * tween;
        this.x = x;
        this.y = y;
        fixBounds();
        
        colOffset = (int) - this.x / tileSize;
        rowOffset = (int) - this.y / tileSize;
    }
    private void fixBounds() {
        if (x < xmin) {
            x = xmin;
        }
        if (y < ymin) {
            y = ymin;
        }
        if (x > xmax) {
            x = xmax;
        }
        if (y > ymax) {
            y = ymax;
        }
    }
    public void draw(Graphics2D g) {
        for (int row = rowOffset; row < rowOffset + numRowsToDraw; row++) {
            if (row >= numRows) {
                break;
            }
            for (int col = colOffset; col < colOffset + numColsToDraw; col++) {
                if (col >= numCols) {
                    break;
                }
                if (map[row][col] == 0) {
                    continue;
                } 
                int rc = map[row][col];
                int r = rc / numTilesAcross;
                int c = rc % numTilesAcross;
                
                g.drawImage(tiles[r][c].getImage(),
                        (int)x + col * tileSize,
                        (int)y + row * tileSize, null);
            }
        }
    }
    public int getNumRows() {
        return numRows;
    }
    public int getNumCols() {
        return numCols;
    }
}
