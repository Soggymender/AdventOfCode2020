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
    List<Rule> unidentifiedRules = new ArrayList<Rule>();
    List<Rule> identifiedRules = new ArrayList<Rule>(); 

    ArrayList<ArrayList<Integer>> tickets = new ArrayList<ArrayList<Integer>>(); 

    public static void main( String[] args )
    {
        Day16 dayN = new Day16();

        Instant start = Instant.now();

        dayN.parseRules();
        dayN.locateFields();
        long result = dayN.checksumTicket();

        System.out.println(result);

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

            ArrayList curTicket = null;

            line = reader.readLine();
            while (line != null) {
        
                if (line.length() == 0) {
                    if (inRules) {
                        inRules = false;
                        inYourTicket = true;

                        line = reader.readLine();
                        line = reader.readLine();

                        continue;
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
                    unidentifiedRules.add(newRule);

                    String[] lineParts = pLineSplit.split(line);

                    newRule.fieldName = lineParts[0];

                    newRule.ranges[0] = new Range();
                    newRule.ranges[0].min = Integer.parseInt(lineParts[1].trim());
                    newRule.ranges[0].max = Integer.parseInt(lineParts[2].trim());

                    newRule.ranges[1] = new Range();
                    newRule.ranges[1].min = Integer.parseInt(lineParts[3].trim());
                    newRule.ranges[1].max = Integer.parseInt(lineParts[4].trim());
                }
                
                else {

                    String[] lineParts = pLineSplit.split(line);

                    // If the previous ticket was invalid, re-use it.
                    // Otherwise create a new one.
                    if (curTicket == null) {
                        curTicket = new ArrayList<Integer>(lineParts.length);
                    } else {
                        curTicket.clear();
                    }

                    boolean ticketValid = true;
                    for (int i = 0; i < lineParts.length; i++) {

                        int val = Integer.parseInt(lineParts[i]);

                        boolean valid = false;

                        for (int j = 0; j < unidentifiedRules.size(); j++) {

                            Rule rule = unidentifiedRules.get(j);

                            if ((val >= rule.ranges[0].min && val <= rule.ranges[0].max) ||
                                (val >= rule.ranges[1].min && val <= rule.ranges[1].max)) {
                                valid = true;
                                break;
                            }
                        }

                        if (!valid) {
                            errorVal += val;
                            ticketValid = false;
                        } else if (ticketValid) {
                            // This entry was valid, and the ticket is valid so far.
                            // Add this value to the ticket.
                            curTicket.add(i, val);
                        }
                    }

                    if (ticketValid) {
                        // Add the ticket to the tickets array.
                        // Don't re-use the ticket.
                        tickets.add(curTicket);
                        curTicket = null;
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

    void locateFields() {

        int numColumns = unidentifiedRules.size();

        Rule rule = null;

        // Add the rules to the identified array just to set up the indices.
        // But they will be swapped around until they are all in position.
        // unidentifiedRules will be iterated and reduced for simplicity.
        for (int i = 0; i < unidentifiedRules.size(); i++) {
            identifiedRules.add(null);//unidentifiedRules.get(i));
        }

        // While there are rules in the unidentified rules.
        while (unidentifiedRules.size() > 0) {

            System.out.println("NEW PASS/n");

            // Find a rule with only one solution then remove it.
            // Multiple rules qualify for multiple fields, but one will match just one. Find it. Remove it. Repeat.
            // A great optimization would be to only re-iterate the columns I already found to be matching, starting with 2 matches
            // working up to the last. It would make for more code though. An initial pass to find candidates, a sort, and additional passes
            // on the valid columns.
            for (int k = 0; k < unidentifiedRules.size(); k++) {

                rule = unidentifiedRules.get(k);

                int lastQualifiedColumn = -1;
                int numQualifedColumns = 0;

                // Iterate each column in valid tickets.
                for (int i = 0; i < numColumns; i++) {

                    // This column has already been identified.
                    if (identifiedRules.get(i) != null) {
                        continue;
                    }

                    // If all are valid
                    boolean valid = true;

                    for (int j = 1; j < tickets.size(); j++) {

                        int val = tickets.get(j).get(i);

                        if ((val < rule.ranges[0].min || val > rule.ranges[0].max) &&
                            (val < rule.ranges[1].min || val > rule.ranges[1].max)) {
                            valid = false;
                            break;
                        }
                    }

                    // All values of all tickets of this column observe this rules ranges.
                    if (valid) {
                        numQualifedColumns++;
                        lastQualifiedColumn = i;
                    }
                }
          
                if (numQualifedColumns == 1) {

                    identifiedRules.set(lastQualifiedColumn, rule);

                    unidentifiedRules.remove(rule);
 
                    System.out.println(rule.fieldName + " matches 1 column " + lastQualifiedColumn); 
                    break;

                } else {
                    System.out.println(rule.fieldName + " matches " + numQualifedColumns + " columns");
                }
            }

        }
    }

    long checksumTicket() {

        long checksum = 1;

        ArrayList<Integer> myTicket = tickets.get(0);

        for (int i = 0; i < identifiedRules.size(); i++) {

            if (identifiedRules.get(i).fieldName.startsWith("departure")) {
                checksum *= (long)myTicket.get(i);
            }
        }

        return checksum;
    }
}