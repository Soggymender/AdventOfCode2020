import java.time.Duration;
import java.time.Instant;

import java.io.BufferedReader;
import java.io.FileReader;

import java.util.*;

public class Day6
{
    public static void main( String[] args )
    {
        Day6 day6 = new Day6();

        Instant start = Instant.now();

        day6.processDeclarations();

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end); 

        System.out.println((timeElapsed.toMillis()));
    }

    public void processDeclarations() {
    
        // Parse lines.
        BufferedReader reader;
        try{
            reader = new BufferedReader(new FileReader("day6_input.txt"));

            int aVal = 'a';

            int[] groupResponses = new int[26];

            int numInGroup = 0;
            int numGroupResponses = 0;
            int minGroupIdx = 26;
            int maxGroupIdx = 0;

            // TMI for Part 1.
//            int[] allResponses = new int[26];
            int numYes = 0;

            String line;
            char c;
     
            line = reader.readLine();
            while (line != null) {
        
                // This condition can be avoided when it is checked below by decrementing numInGroup to -1.
                // That seemed a little too abstract for my taste.
                if (line.length() > 0) {
                    numInGroup++;
                }

                // Decode row.
                for (int i = 0; i < line.length(); i++) {
    
                    // Grab char.
                    c = line.charAt(i);
                    numGroupResponses++;

                    // Track bounds to reduce response iteration during the collation.
                    int idx = (int)c - aVal;

                    if (idx < minGroupIdx) {
                        minGroupIdx = idx;
                    }

                    if (idx > maxGroupIdx) {
                        maxGroupIdx = idx;
                    }
                    
                    groupResponses[idx]++; // 1 because we don't care about multiple "yes" for the same question (within this group).
                }

                line = reader.readLine();

                // Look ahead. An empty line, or no line indicates the end of a group. Collate. 
                if (line == null || line.length() == 0) {

                    // Collate group responses with flight.
                    for (int i = minGroupIdx; i <= maxGroupIdx; i++) {
                        // Logically this is what we want, but we dont need the extra detail or another iteration to calculate the answer for Part 1.
//                        allResponses[i] += groupResponses[i];

                        if (groupResponses[i] == numInGroup ) {
                            numYes++;
                        }

                        groupResponses[i] = 0;
                    }

                    numInGroup = 0;
                    numGroupResponses = 0;
                    minGroupIdx = 26;
                    maxGroupIdx = 0;
                }
            }
           
            reader.close();

            System.out.println(numYes);

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}