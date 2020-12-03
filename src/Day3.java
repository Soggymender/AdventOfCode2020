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

            int numTrees = 0;

            int mapX = 0;
//          int mapY = 0;

            int effectiveX = 0;

            int slopeX = 3;
//          int slopeY = 1;

            String line = reader.readLine();
            int mapWidth = line.length();

            char c;

            do {

                effectiveX = mapX % mapWidth;

                c = line.charAt(effectiveX);
                if (c == '#') {
                    numTrees++;
                }

                mapX += slopeX;
//              mapY += slopeY;

//              if (slopeY == 1) {
                    line = reader.readLine();
/*              } else {
                    // Futureproof?.
                    for (int i = 0; i < slopeY; i++) {
                        line = reader.readLine();
                        if (line == null) {
                            break;
                        }
                    }
                }
*/
            } while (line != null);

            reader.close();

            System.out.println(numTrees);

        } catch (Exception e) {
            System.out.println(e);
        }

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end); 

        System.out.println((timeElapsed.toMillis()));
    }
}