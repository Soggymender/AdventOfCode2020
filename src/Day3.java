import java.time.Duration;
import java.time.Instant;

import java.io.BufferedReader;
import java.io.FileReader;

public class Day3
{
    public static void main( String[] args )
    {
        Instant start = Instant.now();

        // Parse lines.
        BufferedReader reader;
        try{
            reader = new BufferedReader(new FileReader("day3_input.txt"));

            int[] slopesX = { 1, 3, 5, 7, 1 };
            int[] slopesY = { 1, 1, 1, 1, 2 };

            int numTrees[] = new int[5];

            int[] mapX = { 0, 0, 0, 0, 0 };
            int mapY = 0;

            int effectiveX = 0;

            String line = reader.readLine();
            int mapWidth = line.length();

            char c;

            do {

                for (int i = 0; i < 5; i++) {

                    // 5th slope only increments X on 0 and every other line.
                    if (i < 4 || mapY == 0 || ((mapY % (slopesY[i])) == 0)) {

                        effectiveX = mapX[i] % mapWidth;

                        c = line.charAt(effectiveX);
                        if (c == '#') {
                            numTrees[i]++;
                        }

                        mapX[i] += slopesX[i];
                    }
                }

                mapY++;

                line = reader.readLine();

            } while (line != null);

            reader.close();

            long result = (long)numTrees[0] * (long)numTrees[1] * (long)numTrees[2] * (long)numTrees[3] * (long)numTrees[4];

            System.out.println(result);

        } catch (Exception e) {
            System.out.println(e);
        }

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end); 

        System.out.println((timeElapsed.toMillis()));
    }
}