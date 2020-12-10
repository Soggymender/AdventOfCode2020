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

    int lineCount = 0;

    public static void main( String[] args )
    {
        Day9 day9 = new Day9();

        Instant start = Instant.now();

        long result = day9.decypher(25);
        day9.findAddends(result);

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end); 

        System.out.println((timeElapsed.toMillis()));
    }

    long decypher(int preambleLength) {
    
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

                lineCount++;

                val = Integer.parseInt(line);
 
                // Validate.
                if (numUsed >= preambleLength) {

                    if (!validate(val)) {
                        reader.close();
                        System.out.println(val);
                        return val;
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

        return lineCount;
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

    /*
    Run through the list again. Maintain a set of consequtive numbers between indices lo and hi
    that have a running total below or equal to the target value. If it ever goes above, increase lo
    until in threshold again. If it ever goes below, add more values from the input and increment hi.

    Finding the min and max within the solution subset is a separate loop because I think it's faster than
    keeping those updated on the fly. Dropping the lowest or highest off the end means iterating on average
    half of the subset again - potentially every time lo or hi moves.
    */  
    void findAddends(long total) {
    
        // Parse lines.
        BufferedReader reader;
        try{
            reader = new BufferedReader(new FileReader("day9_input.txt"));

            set = new long[lineCount];
            numUsed = 0;
            int lo = 0;
            int hi = 0;

            long runningTotal = 0;
            long min = total;
            long max = 0; 

            String line;

            long val;

            line = reader.readLine();
            while (line != null) {

                val = Integer.parseInt(line);
 
                // Remove from the tail.
                while (runningTotal > total) {
                    runningTotal -= set[lo];
                    set[lo] = 0; // Just clearing it for debugging.
                    lo++;
                }

                // Check the result.
                if (runningTotal == total) {

                    // Spin through lo to hi and find min and max.
                    // Keeping track of this on the fly wastes a lot of time because if you drop
                    // the previous low off the tail you have to iterate the list to find its replacement.
                    for (int i = lo; i < hi; i++) {
                        if (set[i] < min) {
                            min = set[i];
                        } else if (set[i] > max) {
                            max = set[i];
                        }
                    }
                    System.out.println(min + " + " + max + " = " + (min + max));
                    return;
                }

                // Add to the head.
                set[hi] = val;
                hi++;

                runningTotal += val;

                line = reader.readLine();
            }
           
            reader.close();

        } catch (Exception e) {
            System.out.println(e);
        }
   }    
}