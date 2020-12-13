import java.time.Duration;
import java.time.Instant;

import java.io.BufferedReader;
import java.io.FileReader;

import java.util.*;

public class Day13
{
    int arrivalTime = 0;
    int bestBus = 0;
    int bestTime = Integer.MAX_VALUE;

    public static void main( String[] args )
    {
        Day13 day13 = new Day13();

        Instant start = Instant.now();

        day13.schedule();

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end); 

        System.out.println((timeElapsed.toMillis()));
    }

    public void schedule() {
    
        // Parse lines.
        BufferedReader reader;
        try{
            reader = new BufferedReader(new FileReader("day13_input.txt"));

            int busId;

            String line;
            String word = new String();
            char c;
     
            line = reader.readLine();
            arrivalTime = Integer.parseInt(line);

            line = reader.readLine();

            while (line != null) {
        
                // Decode row.
                for (int i = 0; i < line.length(); i++) {
    
                    // Grab char.
                    c = line.charAt(i);
                    
                    if (c == ',') {
                        if (!word.equals("x")) {
                            busId = Integer.parseInt(word);
                            int firstViableTime = getFirstViableTime(busId);
                            if (firstViableTime < bestTime) {
                                bestTime = firstViableTime;
                                bestBus = busId;
                            }
                        }

                        word = "";

                    } else {
                        word += c;
                    }
                }

                line = reader.readLine();
            }
           
            System.out.println(bestBus + "@ " + bestTime);
            System.out.println(((bestTime - arrivalTime) * bestBus));

            reader.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    int getFirstViableTime(int busId) {

        int firstViableTime = 0;

        while (firstViableTime < arrivalTime) {
            firstViableTime += busId;
        }

        return firstViableTime;
    }
}