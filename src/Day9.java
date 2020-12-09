import java.time.Duration;
import java.time.Instant;

import java.io.BufferedReader;
import java.io.FileReader;

import java.util.*;

public class Day9
{
    long[] set = null;
    int numUsed = 0;
    int end = 0;

    public static void main( String[] args )
    {
        Day9 day9 = new Day9();

        Instant start = Instant.now();

        day9.decypher(25);

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end); 

        System.out.println((timeElapsed.toMillis()));
    }

    public void decypher(int preambleLength) {
    
        // Parse lines.
        BufferedReader reader;
        try{
            reader = new BufferedReader(new FileReader("day9_input.txt"));

            set = new long[preambleLength];
            numUsed = 0;
            end = 0;

            String line;
     
            long val;

            line = reader.readLine();
            while (line != null) {

                val = Integer.parseInt(line);
 
                // Validate.
                if (numUsed >= preambleLength) {

                    if (!validate(val)) {
                        System.out.println(val);
                        return;
                    }
                } else {
                    numUsed++;
                }

                // Add.
                set[end] = val; 

                // Move end.
                end++;
                if (end >= preambleLength) {
                    end = 0;
                }

                line = reader.readLine();

                // Look ahead. An empty line, or no line indicates the end of a group. Collate. 
                if (line == null || line.length() == 0) {

                }
            }
           
            reader.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // Use two loops to iterate a single array with a rolling end index.
    boolean validate(long val) {

        // c = count.
        // i = index.
        for (int c = 0, i = end; c < numUsed; c++, i++) {

            // Wrap i.
            if (i == numUsed)
                i = 0;
        
            // ic = count.
            // ii = index.
            for (int ic = c, ii = i; ic < numUsed; ic++, ii++) {

                // Wrap ii.
                if (ii == numUsed)
                    ii = 0;

                if (set[i] + set[ii] == val) {
                    return true;
                }
            }
        }

        return false;
    }
}