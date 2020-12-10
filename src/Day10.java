import java.time.Duration;
import java.time.Instant;

import java.io.BufferedReader;
import java.io.FileReader;

import java.util.*;

public class Day10
{
    int[] elements = null;
    int end = 0;

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

        int loDeltaCount = 0;
        int hiDeltaCount = 0;

        // Chain adapters.
        int delta;
        for (int i = 0; i < elements.length; i++) {

            if (i == 0)
                delta = elements[i];
            else
                delta = elements[i] - elements[i - 1];
            
            if (delta == 1)
                loDeltaCount++;
            else if (delta == 3)
                hiDeltaCount++;
        }

        // Account for the device's joltage difference which is always 3.
        hiDeltaCount += 1;

        System.out.println(loDeltaCount + ", " + hiDeltaCount);
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