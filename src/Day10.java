import java.time.Duration;
import java.time.Instant;

import java.io.BufferedReader;
import java.io.FileReader;

import java.util.*;

public class Day10
{
    int[] elements = null;
    int end = 0;

    long result = 0;

    public static void main( String[] args )
    {
        Day10 day10 = new Day10();

        Instant start = Instant.now();

        day10.chainAdapters();

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end); 

        System.out.println((timeElapsed.toMillis()));
    }

    public void chainAdapters() {
    
        // Parse lines.
        BufferedReader reader;

        int lineCount = 0;

        try{
            {
                reader = new BufferedReader(new FileReader("day10_input.txt"));

                // Count entries.
                String line = reader.readLine();
                while (line != null) {
                
                    line = reader.readLine();
                    lineCount++;
                }

                reader.close();
            }

            {
                reader = new BufferedReader(new FileReader("day10_input.txt"));

                elements = new int[lineCount];
                int val;

                String line;
        
                // Count entries.
                line = reader.readLine();
                while (line != null) {
            
                    val = Integer.parseInt(line);
                    orderedInsert(val);

                    line = reader.readLine();
                }
            
                reader.close();
            }

        } catch (Exception e) {
            System.out.println(e);
        }

        spaceHeater();
        System.out.println(result);
    }

    /*
    I realized this could have been a bit cleaner if I'd just put the "port" and "device" on the sorted list
    as bookends, then I could remove the various checks for 0 and end - 1.
    
    Iterate the list once. Find sections of contiguous "adapters" that can be removed individually.
    After exiting a section mark it with a lo and hi index, and send it to a branch permutation checker
    that runs all combinations from lo to hi. That also branches to find all combinations.

    Multiply the section counts together.

    This happens to run very quickly, but clearly if a section was extremely large it would be very slow.
    But each section that can be counted in isolation significantly reduces the number of permutations
    that need to be manually checked.
    */
    void spaceHeater() {

      
        int delta;
        int temp;

        int lo = -1;
        int hi = 0;

        int sectionLength = 0;
        int sections = 1;

        result = 1; // Add the unmodified permutation.

        for (int i = 0; i < end; i++) {
            
            if (i == 0)
                delta = elements[i + 1]; // Power port.
            else if (i == end - 1)
                delta = 99; // Last element before device. Have to keep this one
            else
                delta = elements[i + 1] - elements[i - 1];
            
            // Can this value be removed?
            if (delta <= 3) {

                sectionLength++;
            } else {

                // Found one that can't be removed.
                // If there were previous ones that could be removed, they form a section.

                if (sectionLength == 0) {
                    lo = i;
                } else {
                    hi = i;

                    long sectionCount = countSectionPermutations(lo, hi);
                    result *= sectionCount;

                    sectionLength = 0;
                    lo = hi;
                }
            }
        }
    }

    long countSectionPermutations(int lo, int hi) {

        long sectionCount = 1;

        int delta;
        int temp;

        for (int i = lo + 1; i < hi; i++) {
            
            if (i == 0)
                // -1 is 0 (the outlet), 0 is being removed, the difference between element 1 and -1 is the value of element 1.
                delta = elements[i + 1];
            else
                delta = elements[i + 1] - elements[i - 1];
            
            // Can this value be removed?
            if (delta <= 3) {

                // i can be removed.
                temp = elements[i];

                // branch
                if (i == 0)
                    elements[i] = 0;
                else
                    elements[i] = elements[i - 1];

                sectionCount += countSectionPermutations(i, hi);

                elements[i] = temp;
            }
        }

        return sectionCount;
    }

    void orderedInsert(int value) {

        for (int i = end; i > 0; i--) {

            if (value < elements[i - 1]) {
                elements[i] = elements[i - 1];
            } else {
                elements[i] = value;
                end++;
                return;
            }
        }

        // If we get this far, value goes at 0.
        elements[0] = value;
        end++;
    }
}