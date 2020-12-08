import java.time.Duration;
import java.time.Instant;

import java.io.BufferedReader;
import java.io.FileReader;

import java.util.*;

class Rule
{
    String containerName;

    List<Integer> containedCounts = new ArrayList<Integer>();
    List<Rule> contained = new ArrayList<Rule>();

    public Rule(String name) {
        containerName = name;
    }

    public Rule addContained(int count, String name) {
        
        Rule rule = new Rule(name);
        
        containedCounts.add(count);
        contained.add(rule);

        return rule;
    }

    int getIdx(String name) {

        for (int i = 0; i < contained.size(); i++) {
            Rule curRule = contained.get(i);
            if (curRule.containerName.equals(name)) {
                return i;
            }
        }

        return -1;
    }    
}

public class Day7
{
    // This is the top level rule that will contain all root rules.
    Rule rules = new Rule("rules");

    public static void main( String[] args )
    {
        Day7 day7 = new Day7();

        Instant start = Instant.now();

        day7.parseRules();
        int count = day7.countOptions("shiny gold");

        System.out.println(count);

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end); 

        System.out.println((timeElapsed.toMillis()));
    }

    public void parseRules() {
    
        // Parse lines.
        BufferedReader reader;
        try{
            reader = new BufferedReader(new FileReader("day7_input.txt"));

            boolean inContainer = true;
            boolean inCount = false;
            String word = "";
            int count = 0;
            
            String containerName = "";
            String containedName = ""; 

            Rule containerRule = null;

            String line;
            char c;

            boolean eol = false;

            line = reader.readLine();
            while (line != null) {
        
                // Decode row.
                for (int i = 0; i < line.length() && !eol; i++) {
    
                    // Grab char.
                    c = line.charAt(i);

                    // End of word.
                    if (c == ' ' || c == ',' || c == '.') {
                        
                        int wordLength = word.length();

                        boolean bag = false;
                        boolean bags = false;
                        boolean contain = false;

                        if (wordLength == 3 && word.equals("bag")) {
                            bag = true;
                        } else if (wordLength == 4 && word.equals("bags")) {
                            bags = true;
                        } else if (wordLength == 7 && word.equals("contain")) {
                            contain = true;
                        }

                        if (inContainer) {
                            
                            if (bag || bags) {

                                // Create the new container rule.
                                containerRule = rules.addContained(0, containerName);

                                containerName = "";
                                word = "";

                                inContainer = false;
                                continue;
                            }

                            // Add the finished word to the container name.
                            if (containerName.length() == 0) {
                                containerName += word;
                            } else {
                                containerName += " " + word;
                            }

                            word = "";

                            continue;
                        } else if (inCount) { 
                       
                            // End of word and we're in a count.
                                
                            // Trim leading space but end on trailing space.
                            if (wordLength == 0 && c == ' ') {
                                continue;
                            }

                            if (word.equals("no")) {
                                inCount = false;
                                eol = true;
                                word = "";
                                break;
                            }

                            count = Integer.parseInt(word);
                            inCount = false;
                            word = "";
                            continue;

                        } else {

                            // Check ahead for a bag count so every word doesn't have to be int parsed.
                            if (c == ',') {
                                inCount = true;
                            }

                            if (contain) {
                                inCount = true;
                                word = "";
                                continue;
                            }

                            if (bag || bags) {
                                
                                // Create a new rule to the current container.
                                containerRule.addContained(count, containedName);

                                containedName = "";
                                word = "";
                                word = "";
                                continue;
                            }

                            // Add the finished word to the container name.
                            if (containedName.length() == 0) {
                                containedName += word;
                            } else {
                                containedName += " " + word;
                            }

                            word = "";

                            continue;
                        }
                    }

                    word += c;
                }

                inContainer = true;
                eol = false;

                line = reader.readLine();

                // Look ahead. 
                if (line == null || line.length() == 0) {

                }
            }
           
            reader.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    int countOptions(String name) {

        int total = 0;

        // Iterate the top level rules.
        for (int i = 0; i < rules.contained.size(); i++) {
            if (!rules.contained.get(i).containerName.equals(name)) {
                continue;
            }
            total += countOptionsRec(rules, rules.contained.get(i), name);
        } 

        return total;
    }

    int countOptionsRec(Rule rules, Rule rule, String name) {
        
        int total = 0;

        // Don't count the gold bag.
        if (!rule.containerName.equals(name)) {
            total = 1;
        }

        for (int i = 0; i < rule.contained.size(); i++) {

            int rulesIdx = rules.getIdx(rule.contained.get(i).containerName);
            Rule curRule = rules.contained.get(rulesIdx);
            int count = rule.containedCounts.get(i);

            total += count * countOptionsRec(rules, curRule, name);
        }

        return total;
    }
}