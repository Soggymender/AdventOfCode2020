import java.time.Duration;
import java.time.Instant;

import java.io.BufferedReader;
import java.io.FileReader;

import java.util.*;



enum RuleType {
    MINMAX,
    MINMAX_SUFFIX,


}


class Rule {




}


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

            Hashtable<String, Integer> reqFieldsHash = new Hashtable<String, Integer>();

            String[] reqFields = { "byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid" };

            int reqFieldIdxSum = 0;
            for (int i = 0; i < reqFields.length; i++) {
                reqFieldsHash.put(reqFields[i], i + 1);
                reqFieldIdxSum += i + 1;
            }

            // Parse the file lines, accumulating by sums within each passport, and when a passport ends
            // check the byte sums.
            int curFieldIdxSum = 0;
            int curFieldIdx = 0;
            String curField = new String();
            String curValue = new String();

            String[] allValues = new String[reqFields.length];

            boolean inField = true;

            String line;
            char c;
            while ((line = reader.readLine()) != null) {
        
                // Empty line, finished with this passport.
                if (line.length() == 0) {

                    // Check passport validity.
                    if (curFieldIdxSum == reqFieldIdxSum) {
                        valid++;

                        // Now its valid to burn the time checking the field values against the rules.
                    }

                    inField = true;
                    curFieldIdxSum = 0;
                    continue;
                }

                for (int i = 0; i < line.length(); i++) {
        
                    // Grab char.
                    c = line.charAt(i);

                    if (inField) {
                        // Looking to end field.
                        if (c == ':') {
                            inField = false;

                            // If the field is valid, sum it's index.
                            Integer idx = reqFieldsHash.get(curField);
                            if (idx != null) {
                                curFieldIdx = (int)idx;
                                curFieldIdxSum += curFieldIdx;
                            }

                            curField = "";
                            curValue = "";
                            continue;
                        }
                    } else {
                        // Looking to start field.
                        if (c == ' ' || i == 0) {

                            if (c == ' ') {
                                // Store the value for additional parsing and rules check once the passport
                                // fields are all validated.
                                allValues[curFieldIdx - 1] = curValue;
                            }

                            inField = true;
                            continue;
                        }
                    }

                    if (inField) {
                        curField += c;
                    } else {
                        curValue += c;
                    }
                }

                // End of line. Store the current value.
                allValues[curFieldIdx - 1] = curValue;

                inField = true;
            }

            // Check the final passport.
            // Check passport validity.
            if (curFieldIdxSum == reqFieldIdxSum) {
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