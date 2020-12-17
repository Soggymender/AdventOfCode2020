package Day16;

import java.time.Duration;
import java.time.Instant;

import java.io.BufferedReader;
import java.io.FileReader;

import java.util.*;

import java.util.regex.*;



class Range
{
    int min, max;
}

class Rule
{
    String fieldName;
    Range[] ranges = new Range[2]; 
}

public class Day16
{
    List<Rule> rules = new ArrayList<Rule>();

    public static void main( String[] args )
    {
        Day16 dayN = new Day16();

        Instant start = Instant.now();

        dayN.parseRules();

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end); 

        System.out.println((timeElapsed.toMillis()));
    }

    public void parseRules() {
    
        // Parse lines.
        BufferedReader reader;
        try{
            reader = new BufferedReader(new FileReader("day16_input.txt"));

            String reLineSplit = ":| or |-|,";
            
            Pattern pLineSplit = Pattern.compile(reLineSplit);

            String line;

            boolean inRules = true;
            boolean inYourTicket = false;
            boolean inNearbyTickets = false;
     
            int errorVal = 0;

            line = reader.readLine();
            while (line != null) {
        
                if (line.length() == 0) {
                    if (inRules) {
                        inRules = false;
                        inYourTicket = true;
                    } else if (inYourTicket) {
                        inYourTicket = false;
                        inNearbyTickets = true;

                        // Blast over the first line.
                        line = reader.readLine();
                        line = reader.readLine();
                        continue;
                    }
                }

                if (inRules) {
                    Rule newRule = new Rule();
                    rules.add(newRule);

                    String[] lineParts = pLineSplit.split(line);

                    newRule.fieldName = lineParts[0];

                    newRule.ranges[0] = new Range();
                    newRule.ranges[0].min = Integer.parseInt(lineParts[1].trim());
                    newRule.ranges[0].max = Integer.parseInt(lineParts[2].trim());

                    newRule.ranges[1] = new Range();
                    newRule.ranges[1].min = Integer.parseInt(lineParts[3].trim());
                    newRule.ranges[1].max = Integer.parseInt(lineParts[4].trim());
                }
                
                else if (inNearbyTickets) {

                    String[] lineParts = pLineSplit.split(line);

                    for (int i = 0; i < lineParts.length; i++) {

                        int val = Integer.parseInt(lineParts[i]);

                        boolean valid = false;

                        for (int j = 0; j < rules.size(); j++) {

                            Rule rule = rules.get(j);

                            if ((val >= rule.ranges[0].min && val <= rule.ranges[0].max) ||
                                (val >= rule.ranges[1].min && val <= rule.ranges[1].max)) {
                                valid = true;
                                break;
                            }
                        }

                        if (!valid) {
                            errorVal += val;
                        }
                    }
                }

                line = reader.readLine();

            }

            System.out.println(errorVal);

            reader.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}