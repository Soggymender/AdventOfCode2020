import java.time.Duration;
import java.time.Instant;

import java.io.BufferedReader;
import java.io.FileReader;

import java.util.*;

class TileVariant
{
    int id;
    char[][] cells = new char[10][10]; 

    String[] edges = new String[4];

    TileVariant rotate90Variant()
    {
        TileVariant variant = new TileVariant();

        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                variant.cells[y][x] = cells[x][9-y];
            }
        }

        return variant;
    }

    TileVariant hflipVariant()
    {
        TileVariant variant = new TileVariant();

        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                variant.cells[x][y] = cells[9 - x][y];
            }
        }

        return variant;
    }

    TileVariant vflipVariant()
    {
        TileVariant variant = new TileVariant();

        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                variant.cells[x][y] = cells[x][9 - y];
            }
        }

        return variant;
    }

    void getEdgeStrings()
    {
        if (edges[0] == null) {
            edges[0] = new String();
            edges[1] = new String();
            edges[2] = new String();
            edges[3] = new String();
        }

        for (int i = 0; i < 10; i++) {
         
            // Top edge.
            edges[0] += cells[i][0];
            
            // Right edge.
            edges[1] += cells[9][i];
            
            //Bottom edge.
            edges[2] += cells[i][9];
            
            // Left edge.
            edges[3] += cells[0][i];
        }
    }

    void display() {

        String rowString = new String();

        System.out.println("\n\n");

        for (int y = 0; y < 10; y++) {
           
           
            for (int x = 0; x < 10; x++) {
                
                rowString += cells[x][y] + " ";
            }

            System.out.println(rowString);
            rowString = "";
        }
    }
}

class Tile
{
    TileVariant[] variants = new TileVariant[8];

    void generateVariants() {

        // Variant 0 is default.

        // Default rotation variants.
        variants[1] = variants[0].rotate90Variant();
        variants[2] = variants[1].rotate90Variant();
        variants[3] = variants[2].rotate90Variant();

        // H Flip variant.
        variants[4] = variants[0].hflipVariant();

        // H Flip rotation variants.
        variants[5] = variants[4].rotate90Variant();
        variants[6] = variants[5].rotate90Variant();
        variants[7] = variants[6].rotate90Variant();

        for (int i = 0; i < variants.length; i++) {
            variants[i].getEdgeStrings();
        }
    }
}

class mapCell
{
    int tileId;
    int tileIdx;
    int tileVariant;
}

public class Day20
{
    List<Tile> freeTiles = new ArrayList<Tile>();
    List<Tile> usedTiles = new ArrayList<Tile>();

    int mapWidth = 0;

    mapCell[][] map;
    int mapX = 0, mapY = 0;

    int highestMapX = 0;
    int highestMapY = 0;

    char[][] image = null;
    int imageWidth = 0;

    public static void main( String[] args )
    {
        Day20 dayN = new Day20();

        Instant start = Instant.now();

        dayN.parse();
        dayN.depuzzle();
//        dayN.displayResult();

        dayN.assemble();
        dayN.findSeaMonsters();
        dayN.checkWater();

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end); 

        System.out.println((timeElapsed.toMillis()));
    }

    public void parse() {
    
        // Parse lines.
        BufferedReader reader;
        try{
            reader = new BufferedReader(new FileReader("day20_input.txt"));

            String line;

            boolean inTile = false;
            int tileRow = 0;
            boolean inTileId = true;

            Tile tile = null;

            line = reader.readLine();
            while (line != null) {

                if (line.length() == 0) {
                   inTile = false;
                   inTileId = true;
                } else if (inTileId) {
                    
                    tile = new Tile();
                    tile.variants[0] = new TileVariant();

                    tile.variants[0].id = Integer.parseInt(line.split(" |:")[1]);
                    tileRow = 0;

                    freeTiles.add(tile);
                
                    inTileId = false;
                    inTile = true;

                } else if (inTile) {

                    for (int i = 0; i < line.length(); i++) {
                        tile.variants[0].cells[i][tileRow] = line.charAt(i);
                    }

                    tileRow++;
                }

                line = reader.readLine();
           }
           
            reader.close();

            mapWidth = (int)Math.sqrt(freeTiles.size());

            map = new mapCell[mapWidth][mapWidth];
            for (int y = 0; y < mapWidth; y++) {
                for (int x = 0; x < mapWidth; x++) {
                    map[x][y] = new mapCell();
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }

        // Generate variants.
        for (Tile tile : freeTiles) {
            tile.generateVariants();
        };
    }

    /*
    iterate map slots.

    place a tile variant.
    branch
    
    */

    boolean depuzzle() {

        if (mapY == mapWidth) {
            return true; 
        }

        // Find a tile / variant that fits here.
        // Move on.

        int localX = mapX;
        int localY = mapY;
/*
        boolean show = false;
        if (mapY >= highestMapY) {
            highestMapX = 0;
            highestMapY = mapY;

            show = true;
        }

        if (mapX >= highestMapX) {
            highestMapX = mapX;

            show = true;
        }
*/
        //if (show) {
            //System.out.println(mapX + ", " + mapY);
        //    displayMap();
        //}

        // Set the coordinates of the next tile.
        mapX++;
        if (mapX >= mapWidth) {
            mapX = 0;
            mapY++;
        }

        for (int i = 0; i < freeTiles.size(); i++) {

            Tile tile = freeTiles.get(i);
            int tileId = freeTiles.get(i).variants[0].id;

            // If this tile is on the used list, it can't be re-used.
            if (usedTiles.contains(freeTiles.get(i))) {
                continue;
            }                 

            // Going to use this tile here for now.
            map[localX][localY].tileId = tileId;
            map[localX][localY].tileIdx = i;

            usedTiles.add(freeTiles.get(i));

            for (int j = 0; j < tile.variants.length; j++) {

                // See if this variant actually fits...
                if (localX > 0) {
                    // Check left edge.
                    
                    // Get left neighbor.
                    int leftIdx = map[localX - 1][localY].tileIdx;
                    int leftVariantIdx = map[localX - 1][localY].tileVariant;
                    TileVariant leftVariant = freeTiles.get(leftIdx).variants[leftVariantIdx];
                    
                    if (!leftVariant.edges[1].equals(tile.variants[j].edges[3]))
                        continue;
                }

                if (localY > 0) {
                    // Check top edge.

                    // Get top neighbor.
                    int topIdx = map[localX][localY - 1].tileIdx;
                    int topVariantIdx = map[localX][localY - 1].tileVariant;
                    TileVariant topVariant = freeTiles.get(topIdx).variants[topVariantIdx];
                    
                    if (!topVariant.edges[2].equals(tile.variants[j].edges[0]))
                        continue;
                }

                // If we got here, this tile variant fits, so far.

                map[localX][localY].tileVariant = j; 

                boolean result = depuzzle();
                if (result == true) {
                    // Reset map coordinates.
                    mapX = localX;
                    mapY = localY;
                    return true;
                }
            }

            //map[localX][localY].tileId = 0;
            map[localX][localY].tileIdx = 0;
            map[localX][localY].tileVariant = 0;
            usedTiles.remove(tile);
        }

        mapX = localX;
        mapY = localY;

        return false;
    }

    void assemble()
    {
        image = new char[mapWidth * 8][ mapWidth * 8];
        imageWidth = mapWidth * 8;

        int imageX = 0;
        int imageY = 0;

        for (int y = 0; y < mapWidth; y++) {
              
            for (int x = 0; x < mapWidth; x++) {
           
                int tileIdx = map[x][y].tileIdx;
                int variantId = map[x][y].tileVariant;

                Tile tile = freeTiles.get(tileIdx);
          
                imageX = x * 8;
                imageY = y * 8;

                for (int ty = 1; ty < 9; ty++) {
                    for (int tx = 1; tx < 9; tx++) {
                        image[imageX + tx-1][imageY + ty-1] = tile.variants[variantId].cells[tx][ty];
                    }
                }
            }
        }
    }

    void rotateImage() {
      
        char[][] output = new char[imageWidth][imageWidth];
        
        for (int y = 0; y < imageWidth; y++) {
            for (int x = 0; x < imageWidth; x++) {
                output[y][x] = image[x][imageWidth - 1 - y];

            }
        }

        image = output;
    }

    void hflipImage() {
      
        char[][] output = new char[imageWidth][imageWidth];
        
        for (int y = 0; y < imageWidth; y++) {
            for (int x = 0; x < imageWidth; x++) {
                output[x][y] = image[imageWidth - 1 - x][y];
            }
        }

        image = output;
    }


    void vflipImage() {
      
        char[][] output = new char[imageWidth][imageWidth];
        
        for (int y = 0; y < imageWidth; y++) {
            for (int x = 0; x < imageWidth; x++) {
                output[x][y] = image[x][imageWidth - 1 - y];
            }
        }

        image = output;
    }    

    void findSeaMonsters()
    {

        /*
        scan 3 rows at a time.
        look for the sea monster pattern.
        if it's complete, do those three rows again but mark out the sea monters.
        */

        // hflip && vflip.
        for (int i = 0; i < 2; i++) {

            // all orientations.
            for (int j = 0; j < 4; j++) {

                for (int y = 0; y < imageWidth - 2; y++) {

                    for (int x = 0; x < imageWidth - 19; x++) {

                        if (containsSeaMonster(x, y)) {

                        }
                    }
                }

                rotateImage();
            }

            if (i == 0) {
                rotateImage();
                hflipImage();
            } else {
                rotateImage();
                hflipImage();
                vflipImage();
            }
        }
    }

    boolean containsSeaMonster(int xStart, int yStart)
    {
        String seamonster1 = new String("                  # ");
        String seamonster2 = new String("#    ##    ##    ###");
        String seamonster3 = new String(" #  #  #  #  #  #   ");

        for (int x = xStart; x < xStart + seamonster1.length(); x++) {

            if (seamonster1.charAt(x - xStart) == '#') {
                if (image[x][yStart] != '#')
                    return false;
            }

            if (seamonster2.charAt(x - xStart) == '#') {
                if (image[x][yStart+1] != '#')
                    return false;
            }

            if (seamonster3.charAt(x - xStart) == '#') {
                if (image[x][yStart+2] != '#')
                    return false;
            }
        }

        System.out.println("FOUND ONE!");

        // There's a sea monster. Invalidate it.
        for (int x = xStart; x < xStart + seamonster1.length(); x++) {

            if (seamonster1.charAt(x - xStart) == '#')
                image[x][yStart] = 'o';
                
            if (seamonster2.charAt(x - xStart) == '#')
                image[x][yStart+1] = 'o';
                    
            if (seamonster3.charAt(x - xStart) == '#')
                image[x][yStart+2] = 'o';
        }

        return true;
    }

    void checkWater()
    {
        int numWater = 0;

        for (int y = 0; y < imageWidth; y++) {

            for (int x = 0; x < imageWidth; x++) {

                if (image[x][y] == '#')
                    numWater++;
            }
        }

        displayImage();

        System.out.println("numWater = " + numWater);
    }

    void displayResult() {
        
        long result = (long)map[0][0].tileId * (long)map[mapWidth-1][0].tileId * (long)map[0][mapWidth-1].tileId * (long)map[mapWidth-1][mapWidth-1].tileId;

        System.out.println(result);
    }

    
    void displayMap() {

        String rowString = new String();

        System.out.println("\n\n");

        for (int y = 0; y < mapWidth; y++) {
           
           
            for (int x = 0; x < mapWidth; x++) {
           
                if (map[x][y].tileId == 0) {
                    System.out.println(rowString);
                    return;
                }
                
                rowString += map[x][y].tileIdx + " ";
                
            }

            System.out.println(rowString);
            rowString = "";
        }
    }

    void displayImage() {

        String rowString = new String();

        System.out.println("\n\n");

        for (int y = 0; y < imageWidth; y++) {
           
           
            for (int x = 0; x < imageWidth; x++) {
                
                rowString += image[x][y];
            }

            System.out.println(rowString);
            rowString = "";
        }
    }   
}