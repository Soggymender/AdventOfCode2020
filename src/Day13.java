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

    // 
    int[] departureOffsets = null;

    int[] busIds = null;
    int numDepartures = 0;
    int offset = 0;

    public static void main( String[] args )
    {
        Day13 day13 = new Day13();

        Instant start = Instant.now();

        day13.schedule();
        long result = day13.findOptimalDeparture();

        System.out.println(result);

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

            departureOffsets = new int[250];
            busIds = new int[250];

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
                        if (word.equals("x")) {
                            offset++;
                        } else {
                            busId = Integer.parseInt(word);
                            
                            if (numDepartures == 0) {
                                departureOffsets[0] = busId;
                                busIds[0] = busId;
                                numDepartures = 1;
                            } else {
                                departureOffsets[numDepartures] = offset;
                                busIds[numDepartures] = busId;
                                numDepartures++;
                            }

                            offset++;
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

    /*
    This part 2 jammed me up. I wrote a brute force algorithm but it would have taken ages to come to the right
    value. It was just a manual lowest common multiple search. At that point I was stuck, and couldn't recognize
    any relationship or property of the input that would allow it to work faster.

    A friend walked me through it, and the code was a relatively minor tweak - but one I wouldn't have reconigzed on my own.
    */
    long findOptimalDeparture() {

        long departure = departureOffsets[0];

        int hi = 1;
        long step = departureOffsets[0];

        while (true) {

            departure += step;

            // See if the proposed departure time is a multiple of the element at hi.
            long val = (departure + departureOffsets[hi]) % busIds[hi];
            if (val == 0) {

                // The step can be multiplied by element at hi, then all future proposed departures
                // are guaranteed to satisfy the time requirements for all times below hi + 1 (Thanks Jeff!)
                step *= busIds[hi];
                
                // If this was the last element, this proposed departure time is the lowest viable meeting
                // the requirements for consecutive departures.
                if (hi == numDepartures - 1) {
                    return departure;
                }

                hi++;
            }
        }
    }
}