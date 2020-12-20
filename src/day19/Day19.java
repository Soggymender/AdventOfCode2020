package day19;

import java.time.Duration;
import java.time.Instant;

import java.io.BufferedReader;
import java.io.FileReader;

import java.util.*;

import java.util.regex.*;

class Rule
{
    int [] listA = null;
    int [] listB = null;

    char letter;
}

public class Day19
{
    Rule[] rules = new Rule[256];

    List<String> strings = new ArrayList<String>();

    public static void main( String[] args )
    {
        Day19 dayN = new Day19();

        Instant start = Instant.now();

        dayN.decypher();

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end); 

        System.out.println((timeElapsed.toMillis()));
    }

    public void decypher() {

        /*
            Load the rules into a rule array.
            Expand rule 0.
        */

        // Parse lines.
        BufferedReader reader;
        try{
            reader = new BufferedReader(new FileReader("day19_input.txt"));

            boolean inRules = true;

            Pattern ruleIdxPattern = Pattern.compile(":");
            Pattern ruleListPattern = Pattern.compile("\\|");
            Pattern subRulePattern = Pattern.compile(" ");

            String valString;

            int ruleIdx;

            int total = 0;

            String line;
            char c;

            line = reader.readLine();
            while (line != null) {
 
                if (inRules) {

                    if (line.length() == 0) {
                        inRules = false;
                        line = reader.readLine();
                        continue;
                    }

                    String[] ruleIdxString = ruleIdxPattern.split(line);

                    ruleIdx = Integer.parseInt(ruleIdxString[0]);
                    
                    rules[ruleIdx] = new Rule();
                    Rule rule = rules[ruleIdx];
                    
                    String[] ruleListString = ruleListPattern.split(ruleIdxString[1]);
                    ruleListString[0] = ruleListString[0].trim();
                   
                    // Letter rule.
                    if (ruleListString[0].startsWith("\"")) {
                        rule.letter = ruleListString[0].charAt(1);

                    // Sub rule.
                    } else {
                    
                        String[] subRulesString = subRulePattern.split(ruleListString[0]);
                
                        // Parse the rule list.
                        rule.listA = new int[subRulesString.length];
                        for (int i = 0; i < subRulesString.length; i++) {
                            rule.listA[i] = Integer.parseInt(subRulesString[i]);
                        }

                        // If there is a pipe and second rule list, parse it too.
                        if (ruleListString.length == 2) {
                            ruleListString[1] = ruleListString[1].trim();
                            subRulesString = subRulePattern.split(ruleListString[1]);

                            rule.listB = new int[subRulesString.length];
                            for (int i = 0; i < subRulesString.length; i++) {
                                rule.listB[i] = Integer.parseInt(subRulesString[i]);
                            }
                        }
                    }
                } else {

                    total += checkMessage(line);
                }


                line = reader.readLine();
           }
           
            reader.close();

           System.out.println(total);

        } catch (Exception e) {
            System.out.println(e);
        }
    }
// 55, 11
    int checkMessage(String message)
    {
        int pos = checkRuleRecursive(0, message, 0);
        if (pos == message.length())
            return 1;

        return 0;
    }

    int checkRuleRecursive(int idx, String message, int pos) {

        // If this rule is only a letter
        if (rules[idx].listA == null) {
            if (message.charAt(pos) == rules[idx].letter)
                pos++;
            
            return pos;
        }

        int preListPos = pos;

        if (rules[idx].listA == null)
            System.out.println("oops");

        for (int i = 0; i < rules[idx].listA.length; i++) {

            if (pos == message.length()) {
                // Stuck in a loop and ran out of characters.
                pos = preListPos;
                return pos;
            }

            int preRulePos = pos;

            pos = checkRuleRecursive(rules[idx].listA[i], message, pos);
            if (pos == preRulePos) {
                // No match.
                pos = preListPos;
                break;
            }
        }

        if (pos > preListPos) {
            // Match. Don't run the 2nd list.
            return pos;
        }

   
        if (rules[idx].listB != null) {

            for (int i = 0; i < rules[idx].listB.length; i++) {

                if (pos == message.length()) {
                    // Stuck in a loop and ran out of characters.
                    ;//pos = preListPos;
                    return pos;
                }
    
                int preRulePos = pos;

                pos = checkRuleRecursive(rules[idx].listB[i], message, pos);
                if (pos == preRulePos) {
                    // No match.
                    pos = preListPos;
                    break;
                }
            }
        }

        return pos;
    }
}