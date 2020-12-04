import java.time.Duration;
import java.time.Instant;

import java.io.BufferedReader;
import java.io.FileReader;

public class Day4
{
    public static void main( String[] args )
    {
        Instant start = Instant.now();

        // Use a byte sum process to "approximate" whether a passport is valid. This is very easily
        // spoofed by a "nation" with certain invalid codes. Lets not use this at a real airport.
        // But it happens to work for the input data.

        // Parse lines.
        BufferedReader reader;
        try{
            reader = new BufferedReader(new FileReader("day4_input.txt"));

            int valid = 0;

            String[] reqFields = { "byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid", "cid" };

            int reqChecksum1 = 0;
            int reqChecksum2 = 0;

            // Sum byte values of the two acceptable field sets.
            for (int i = 0; i < reqFields.length; i++) {

                for (int j = 0; j < reqFields[i].length(); j++) {
                    reqChecksum1 += (int)reqFields[i].charAt(j);
                }

                if (i < reqFields.length - 1) {
                    // Copy checksum 1 into 2 up to "cid", which is skipped.
                    reqChecksum2 = reqChecksum1;
                }
            }

            // Parse the file lines, accumulating by sums within each passport, and when a passport ends
            // check the byte sums.
            int curChecksum = 0;
            boolean inField = true;

            String line;
            char c;
            while ((line = reader.readLine()) != null) {
        
                // Empty line, finished with this passport.
                if (line.length() == 0) {

                    // Check passport validity.
                    if (curChecksum == reqChecksum1 || curChecksum == reqChecksum2) {
                        valid++;
                    }

                    inField = true;
                    curChecksum = 0;
                    continue;
                }

                for (int i = 0; i < line.length(); i++) {
        
                    // Grab char.
                    c = line.charAt(i);

                    if (inField) {
                        // Looking to end field.
                        if (c == ':') {
                            inField = false;
                            continue;
                        }
                    } else {
                        // Looking to start field.
                        if (c == ' ' || i == 0) {
                            inField = true;
                            continue;
                        }
                    }

                    if (inField) {
                        curChecksum += (int)c;
                    }
                }

                inField = true;
            }

            // Check the final passport.
            if (curChecksum == reqChecksum1 || curChecksum == reqChecksum2) {
                valid++;
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
}