import java.time.Duration;
import java.time.Instant;

import java.io.BufferedReader;
import java.io.FileReader;

import java.util.*;

public class DayN
{
    public static void main( String[] args )
    {
        DayN dayN = new DayN();

        Instant start = Instant.now();

        dayN.boot();

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end); 

        System.out.println((timeElapsed.toMillis()));
    }

    public void boot() {
    
        // Parse lines.
        BufferedReader reader;
        try{
            reader = new BufferedReader(new FileReader("day6_input.txt"));

            String line;
            char c;
     
            line = reader.readLine();
            while (line != null) {
        
                // Decode row.
                for (int i = 0; i < line.length(); i++) {
    
                    // Grab char.
                    c = line.charAt(i);
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
}