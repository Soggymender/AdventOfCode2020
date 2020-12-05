import java.time.Duration;
import java.time.Instant;

import java.io.BufferedReader;
import java.io.FileReader;

import java.util.*;

public class Day4
{
    // Hash to index (which doubles as a checksum).
    // Used to determine if a key is a recogized / required one, where to store the value in an array,
    // and to sum the required keys to make sure they are all present.
    Hashtable<String, Integer> reqFieldsHash = new Hashtable<String, Integer>();
    Hashtable<String, Integer> eyeColorsHash = new Hashtable<String, Integer>();

    String[] reqFields = { "byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid"};
    String[] eyeColors = { "amb", "blu", "brn", "gry", "grn", "hzl", "oth" };

    int reqFieldIdxSum = 0;

    public static void main( String[] args )
    {
        Day4 day4 = new Day4();

        Instant start = Instant.now();

        day4.checkPassports();

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end); 

        System.out.println((timeElapsed.toMillis()));
    }

    public void checkPassports() {
    
        // Parse lines.
        BufferedReader reader;
        try{
            reader = new BufferedReader(new FileReader("day4_input.txt"));

            int validFields = 0; // Total passports with valid fields.
            int valid = 0;  // Subset of the above with valid values.

            // Build required fields hash.
            for (int i = 0; i < reqFields.length; i++) {
                reqFieldsHash.put(reqFields[i], i + 1);
                reqFieldIdxSum += i + 1;
            }

            // Build eye color hash.
            for (int i = 0; i < eyeColors.length; i++) {
                eyeColorsHash.put(eyeColors[i], i + 1);
            }

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
                        validFields++;
                        valid += validatePassport(allValues);
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
                            } else {
                                curFieldIdx = -1;
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
                                if (curFieldIdx != -1)
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
                if (curFieldIdx != -1)
                    allValues[curFieldIdx - 1] = curValue;

                inField = true;
            }

            // Check the final passport.
            if (curFieldIdxSum == reqFieldIdxSum) {
                valid += validatePassport(allValues);
            }

            reader.close();

            System.out.println(validFields + " / " + valid);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private int validatePassport(String[] values) {

        for (int i = 0; i < reqFields.length; i++) {
            
            String valString = values[i];

            int val = -1;

            // Just going to parse this for the year formats all in one spot.
            if (i <= 2)  {
                try {
                    val = Integer.parseInt(valString);
                } catch (Exception e) {
                    return 0;
                }
            }

            switch (i) {

                case 0:
                    if (valString.length() != 4)
                        return 0;
                    
                    if (val < 1920 || val > 2002)
                        return 0;

                    break;

                case 1:
                    if (valString.length() != 4)
                        return 0;
                    
                    if (val < 2010 || val > 2020)
                        return 0;

                    break;

                case 2:
                    if (valString.length() != 4)
                        return 0;
                    
                    if (val < 2020 || val > 2030)
                        return 0;

                    break;

                case 3:

                    if (valString.endsWith("cm")) {

                        valString = valString.substring(0, valString.length() - 2);
                        val = Integer.parseInt(valString);
                        if (val < 150 || val > 193)
                            return 0;

                        break;

                    } else if (valString.endsWith("in")) {
                        valString = valString.substring(0, 2);
                        val = Integer.parseInt(valString);
                        if (val < 59 || val > 76)
                            return 0;

                        break;
                    } 

                    return 0;

                case 4:
                    
                    if (valString.length() != 7)
                        return 0;

                    if (valString.charAt(0) != '#')
                        return 0;

                    try {
                        Long.parseLong(valString.substring(1), 16);
                        break;
                    } catch (Exception e) {
                        return 0;
                    }

                case 5:

                    if (!eyeColorsHash.containsKey(valString))
                        return 0;

                    break;

                case 6:

                    if (valString.length() != 9)
                        return 0;

                    try {
                        Long.parseLong(valString.substring(1), 10);
                        break;
                    } catch (Exception e) {
                        return 0;
                    }

                default:
                    break;
            }
        }

        return 1;
    }
}