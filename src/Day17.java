import java.time.Duration;
import java.time.Instant;

import java.io.BufferedReader;
import java.io.FileReader;

import java.util.*;

public class Day17
{
    int xSize = 0;
    int ySize = 0;
    int zSize = 0;

    // Offsets to get the starting slice in the center.
    int xOffset = 0; 
    int yOffset = 0;

    int sliceSize = 0;

    int numCells = 0;
    int numStateChanges = 0;
    int numActiveCells = 0;

    boolean[] map1 = null;
    boolean[] map2 = null;

    boolean[] curMap = null;
    boolean[] newMap = null;

    public static void main( String[] args )
    {
        Day17 day17 = new Day17();

        Instant start = Instant.now();

        day17.parseMap(6);

        // Show the initial state.
        day17.display(0);

        for (int i = 0; i < 6; i++) {
            
            day17.simulate();
            day17.display(i + 1);    
        }

        System.out.println(day17.numActiveCells);        

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end); 

        System.out.println((timeElapsed.toMillis()));
    }

    public void parseMap(int numSteps) {
    
        // Parse lines.
        BufferedReader reader;

        int startingSliceWidth = 0;
        int startingSliceHeight = 0;

        try{
            {
                reader = new BufferedReader(new FileReader("day17_input.txt"));

                // Count entries.
                String line = reader.readLine();
                while (line != null) {
                
                    line = reader.readLine();
                    startingSliceHeight++;

                    if (startingSliceWidth == 0)
                        startingSliceWidth = line.length(); 
                }

                reader.close();
            }

            xSize = startingSliceWidth + (numSteps * 2); // Account for growth + 1 per step in both directions.
            ySize = startingSliceHeight + (numSteps * 2); // "
            zSize = xSize;

            sliceSize = xSize * ySize;

            xOffset = (xSize - startingSliceWidth) / 2;
            yOffset = (ySize - startingSliceHeight) / 2;

            {   
                map1 = new boolean[xSize * ySize * zSize];
                map2 = new boolean[xSize * ySize * zSize];

                curMap = map1;
                newMap = map2;

                int offset = getOffset(xOffset, yOffset, zSize / 2);

                numCells = xSize * ySize * zSize;

                reader = new BufferedReader(new FileReader("day17_input.txt"));

                String line;
                char c;

                int curY = yOffset;
                int curZ = zSize / 2;

                // Count entries.
                line = reader.readLine();
                while (line != null) {
            
                    for (int i = 0; i < line.length(); i++) {
    
                        // Grab char.
                        c = line.charAt(i);

                        // Store a two copies - one in the hi order and one in the lo.
                        if (c == '#') {
                            map1[offset] = true;
                            map2[offset] = true;
                        }
                        
                        offset++;
                    }

                    line = reader.readLine();
                    offset = getOffset(xOffset, ++curY, curZ);
                }
            
                reader.close();
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    int getOffset(int x, int y, int z)
    {
        int offset = (z * sliceSize) + (y * xSize) + x;
        return offset;
    }

    int simulate() {

        // Swap maps.
        if (curMap == map1) {
            curMap = map2;
            newMap = map1;
        } else {
            curMap = map1;
            newMap = map2;
        }

        numActiveCells = 0;

        int offset;

        for (int z = 0; z < zSize; z++) {

            for (int y = 0; y < ySize; y++) {

                for (int x = 0; x < xSize; x++) {

                    offset = getOffset(x, y, z);
                    
                    // Inactive?
                    if (!curMap[offset]) {
                        if (shouldActivate(offset)) {
                            numActiveCells++;
                            newMap[offset] = true;
                        } else {
                            newMap[offset] = false;
                        }

                    } 
                    
                    // Occupied.
                    else {
                        if (shouldDeactivate(offset)) {
                            newMap[offset] = false;
                            continue;
                        } else {
                            newMap[offset] = true;
                        }

                        numActiveCells++;
                    }
                }
            }
        }

        return numActiveCells;
    }

    boolean shouldActivate(int offset) {

        if (countActiveNeighbors(offset) == 3)
            return true;

        return false;
    }

    boolean shouldDeactivate(int offset) {

        int activeNeighbors = countActiveNeighbors(offset);
        if (activeNeighbors != 2 && activeNeighbors != 3)
            return true;

        return false;
    }

    int countActiveNeighbors(int offset) {

        int neighbors = 0;

        int cellZ = offset / sliceSize;
        int cellY = (offset - (cellZ * sliceSize)) / xSize;
        int cellX = (offset - (cellZ *sliceSize) - (cellY * ySize));

        int x = cellX - 1;
        int y = cellY - 1;
        int z = cellZ - 1;

        int curOffset;

        for (z = cellZ - 1; z <= cellZ + 1; z++) {

            for (y = cellY - 1; y <= cellY + 1; y++) {

                for (x = cellX - 1; x <= cellX + 1; x++) {

                    if (x == cellX && y == cellY && z == cellZ)
                        continue;

                    if (x < 0 || x >= xSize ||
                        y < 0 || y >= ySize ||
                        z < 0 || z >= zSize) {
                            continue;
                        }

                    curOffset = getOffset(x, y, z);

                    if (curMap[curOffset])
                        neighbors++;
                }
            }
        }

        return neighbors;
    }

    void display(int simCount) {

        int rowStart;

        System.out.println("\n\n\n SIM " + simCount);
        for (int z = 0; z < zSize; z++) {

            System.out.println("\nz = " + z);

            for (int y = 0; y < ySize; y++) {

                rowStart = (z * sliceSize) + (y * xSize);

                String row = new String();
                
                for (int x = 0; x < xSize; x++) {
    
                    if (newMap[rowStart + x])
                        row += '#';
                    else
                        row += '.';
                }

                System.out.println(row);
            }
        }
    }
}