import java.time.Duration;
import java.time.Instant;

import java.io.BufferedReader;
import java.io.FileReader;

import java.util.*;

public class Day11
{
    int mapWidth = 0;
    int mapHeight = 0;

    int numCells = 0;
    int numStateChanges = 0;
    int numOccupiedSeats = 0;

    byte[] map = null;

    public static void main( String[] args )
    {
        Day11 day11 = new Day11();

        Instant start = Instant.now();

        day11.parseMap();

        // Show the initial state.
        day11.display();

        while (true) {
            if (day11.simulate() == 0) 
                break;

            day11.display();    
        }

        day11.display();

        System.out.println(day11.numOccupiedSeats);        

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end); 

        System.out.println((timeElapsed.toMillis()));
    }

    public void parseMap() {
    
        // Parse lines.
        BufferedReader reader;

        try{
            {
                reader = new BufferedReader(new FileReader("day11_input.txt"));

                // Count entries.
                String line = reader.readLine();
                while (line != null) {
                
                    line = reader.readLine();
                    mapHeight++;

                    if (mapWidth == 0)
                        mapWidth = line.length(); 
                }

                reader.close();
            }

            {   
                map = new byte[mapWidth * mapHeight];
                int offset = 0;

                numCells = mapWidth * mapHeight;

                reader = new BufferedReader(new FileReader("day11_input.txt"));

                String line;
                char c;

                // Count entries.
                line = reader.readLine();
                while (line != null) {
            
                    for (int i = 0; i < line.length(); i++) {
    
                        // Grab char.
                        c = line.charAt(i);

                        map[offset] = (byte)c;
                        offset++;
                    }

                    line = reader.readLine();
                }
            
                reader.close();
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    int simulate() {

        /*
        the basic implementation of this involves keeping two arrays, so that as each seat is determinen
        whether or not to change state, it is not being influenced by the in-progress changes. Then "page flip".
        afterward.

        The approach below stores modified state  as # - 1 for newly vacated, and # + 1 for newly occupied.
        Then in progress state changes can tell from a single data set whether or not the seat is "currently" 
        occupied or vacated. 

        At the start of a new pass the seat state is re-normalized before being modified again. This could possibly
        be avoided by baking a simulation count into the state data so the current pass knows how to interpret the state.
   
        Of course state changes could be added to an array and the iteration time would be less.
        */

        int localStateChanges = 0;
        numOccupiedSeats = 0;

        for (int i = 0; i < numCells; i++) {

            // Floor?
            if (map[i] == '.')
                continue;

            // Unoccupied?
            if (map[i] == 'L') {
                if (shouldOccupy(i)) {

                    map[i] = '#' + 1;
                    localStateChanges++;
                    numOccupiedSeats++;
                    continue;
                }
            } 
            
            // Occupied.
            else {
                if (shouldVacate(i)) {
                    map[i] = '#' - 1;
                    localStateChanges++;
                    continue;
                }

                numOccupiedSeats++;
            }
        }

        numStateChanges = localStateChanges;

        if (localStateChanges > 0) {
            for (int i = 0; i < numCells; i++) {

                // Re-normalize state from the last iteration.
                // This could be avoided by baking a simulation step count of 0 or 1 into the seat state.
                // So the current pass knows if the state should be applied "locally" or not.
                if (map[i] == ('#' - 1))
                    map[i] = 'L';
                else if (map[i] == ('#' + 1))
                    map[i] = '#';
            }
        }

        return numStateChanges;
    }

    boolean shouldOccupy(int seatIdx) {

        if (countVisiblePassengers(seatIdx) == 0)
            return true;

        return false;
    }

    boolean shouldVacate(int seatIdx) {

        if (countVisiblePassengers(seatIdx) >= 5)
            return true;

        return false;
    }

    int countVisiblePassengers(int seatIdx) {

        /*
        Iterate through 8 offset slopes until a SEAT is found, or the edge of the map is found.
        */

        int passengers = 0;

        int[] xSlopes = new int[] { 1, 1, 0, -1, -1, -1,  0,  1 };
        int[] ySlopes = new int[] { 0, 1, 1,  1,  0, -1, -1, -1 };

        int seatX = seatIdx % mapWidth;
        int seatY = seatIdx / mapWidth;

        int x = seatX;
        int y = seatY;

        int offset;

        for (int i = 0; i < 8; i++) {

            while (true) {

                x += xSlopes[i];
                y += ySlopes[i];
                
                if (!(x >= 0 && x < mapWidth && y >= 0 && y < mapHeight))
                    break;

                offset = y * mapWidth + x;

                if (map[offset] == 'L')
                    break;

                if (map[offset] == '#' || map[offset] == ('#' - 1)) {
                    passengers++;
                    if (passengers >= 5)
                        return passengers;

                    break;
                }                

            }

            x = seatX;
            y = seatY;
        }


        /*
        int passengers = 0;

        int seatX = seatIdx % mapWidth;
        int seatY = seatIdx / mapWidth;

        int offset;

        for (int y = seatY - 1; y <= seatY + 1; y++) {

            if (y < 0)
                continue;
            else if (y >= mapHeight)
                break;
            
            for (int x = seatX - 1; x <= seatX + 1; x++) {

                if (x < 0)
                    continue;
                else if (x >= mapWidth)
                    break;
                else if (x == seatX && y == seatY)
                    continue;

                offset = y * mapWidth + x;

                if (map[offset] == '#' || map[offset] == ('#' - 1)) {
                    passengers++;
                    if (passengers >= 4)
                        return passengers;
                }
            }
        }
        */
        
        return passengers;
    }

    void display() {

        int rowStart;
        int rowEnd;

        for (int y = 0; y < mapHeight; y++) {

            rowStart = y * mapWidth;
            rowEnd = rowStart + mapWidth - 1;

            String row = new String(map, rowStart, mapWidth);

            System.out.println(row);
        }
    }
}