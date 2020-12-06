import java.time.Duration;
import java.time.Instant;

import java.io.BufferedReader;
import java.io.FileReader;

import java.util.*;

public class Day5
{
    public static void main( String[] args )
    {
        Day5 day5 = new Day5();

        Instant start = Instant.now();

        day5.decodeBoardingPasses();

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end); 

        System.out.println((timeElapsed.toMillis()));
    }

    public void decodeBoardingPasses() {
    
        // Parse lines.
        BufferedReader reader;
        try{
            reader = new BufferedReader(new FileReader("day5_input.txt"));

            int lowestSeatId = 1000;
            int highestSeatId = -1;
            long seatIdSum = 0;

            int lo;
            int hi; 

            int row;
            int col;

            int seatId;

            String line;
            char c;
            while ((line = reader.readLine()) != null) {
        
                lo = 0;
                hi = 127;

                int mid;

                int i;

                // Decode row.
                for (i = 0; i < 7; i++) {
    
                    // Grab char.
                    c = line.charAt(i);
    
                    mid = lo + ((hi - lo + 1) / 2);

                    if (c == 'F')
                        hi = mid - 1;
                    else
                        lo = mid;
                }

                row = lo;

                lo = 0;
                hi = 7;

                // Decode column.
                for (/*i = 8*/; i < 10; i++) {

                    // Grab char.
                    c = line.charAt(i);
    
                    mid = lo + ((hi - lo + 1) / 2);

                    if (c == 'L')
                        hi = mid - 1;
                    else
                        lo = mid;
                }

                col = lo;

                seatId = row * 8 + col;

                seatIdSum += seatId;

                if (seatId > highestSeatId) {
                    highestSeatId = seatId;
                } else if (seatId < lowestSeatId) {
                    lowestSeatId = seatId;
                }
            }
           
            reader.close();

            // The missing seat id is the total of the expected seat ids minus the total of the actual seat ids.

            long expectedSeatIdSum = 0;
            // Initial test before trying harder.
            // for (int i = lowestSeatId; i <= highestSeatId; i++) {
            //    expectedSeatIdSum += i;
            // } 

            // Not going to lie. I had to look this up. This will give the same result as the loop above.
            expectedSeatIdSum = (highestSeatId * (highestSeatId + 1)) / 2;
            expectedSeatIdSum -= ((lowestSeatId - 1) * lowestSeatId) / 2;

            long missingSeatId = expectedSeatIdSum - seatIdSum;

            System.out.println(highestSeatId + ", " + missingSeatId);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

}