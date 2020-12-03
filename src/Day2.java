import java.time.Duration;
import java.time.Instant;

import java.io.BufferedReader;
import java.io.FileReader;

public class Day2
{
    public static void main( String[] args )
    {
        Instant start = Instant.now();

        // Parse lines.
        BufferedReader reader;
        try{
            reader = new BufferedReader(new FileReader("day2_input.txt"));

            int valid = 0;

            String line;
            while ((line = reader.readLine()) != null) {

                valid += verify(line);
            }

            reader.close();

            System.out.println(valid);

        } catch (Exception e) {
            System.out.println(e);
        }

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end); 

        System.out.println((timeElapsed.toMillis()));
    }

    private static int verify(String line) {

        final int zeroVal = (int)'0';

        char key = '!'; // Java doesn't trust me.
        int  keyCount = 0;

        char c;

        int lo = 0;
        int hi = 0;

        boolean parsedLo = false; // I could cheat this and store state in keyCount but I think readability wins.
        boolean parsedHi = false;

        // Splitting this into two loops to reduce conditionals in password parser.
        int i = 0;
        for (; i < line.length(); i++) {

            // Grab char.
            c = line.charAt(i);

            // State change checks.
            if (!parsedHi) {
                if (c == '-') {
                    // Dash marks end of low.
                    parsedLo = true;
                    continue;
                } else if (c == ' ') {
                    // First space marks end of high.
                    // Key and password positions are known.
                    parsedHi = true;
                    key = line.charAt(i + 1);
                    
                    // Jump to password start.
                    i += 4;
                    break; 
                }
            }

            // Accumulator.
            if (!parsedLo) {

                lo = lo * 10 + (int)(c - zeroVal); // bitshift by 1 then 3 but oh yeah, this is Java.
            } else {
                hi = hi * 10 + (int)(c - zeroVal); // "
            }
        }

        // 2nd loop for password parse. No conditions needed other than loop condition.
        
        char loChar = line.charAt(i + lo - 1);
        char hiChar = line.charAt(i + hi - 1);

        boolean loValid = loChar == key;
        boolean hiValid = hiChar == key;

        if ((loValid || hiValid) && !(loValid && hiValid)) {
            return 1;
        }

        /*
        for (; i < line.length(); i++) {

            if (line.charAt(i) == key) {
                keyCount++;
            }
        }

        if (keyCount >= lo && keyCount <= hi) {
            return 1;
        }
        */

        return 0;
    }
}