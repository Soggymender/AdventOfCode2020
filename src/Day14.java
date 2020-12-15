import java.time.Duration;
import java.time.Instant;

import java.io.BufferedReader;
import java.io.FileReader;

import java.util.List;
import java.util.ArrayList;

public class Day14
{
    List<Integer> addresses = new ArrayList<Integer>();
    List<Long> values = new ArrayList<Long>();

    long orMask = 0;
    long andMask = 0;

    public static void main( String[] args )
    {
        Day14 dayN = new Day14();

        Instant start = Instant.now();

        dayN.execute();

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end); 

        System.out.println((timeElapsed.toMillis()));
    }

    public void execute() {
    
        // Parse lines.
        BufferedReader reader;
        try{
            reader = new BufferedReader(new FileReader("day14_input.txt"));

            int startIdx;
            int endIdx;

            String line;
            char c;
     
            line = reader.readLine();
            while (line != null) {
        
                if (line.startsWith("mask")) {

                    startIdx = line.lastIndexOf(' ') + 1;
                    String maskString = line.substring(startIdx);

                    orMask = 0;
                    andMask = Long.MAX_VALUE;

                    // Each mask is split into an Or and an An mask.
                    for (int i = 0; i < maskString.length(); i++) {

                        c = maskString.charAt(i);

                        if (c == 'X') {
                            orMask = orMask << 1;
                            andMask = (andMask << 1) | 1; // Shift on a one.
                        } else if (c == '1') {
                            orMask = (orMask << 1) | 1;
                            andMask = (andMask << 1) | 1; // Shift on a zero.
                        } else if (c == '0') {
                            orMask = (orMask << 1);
                            andMask = (andMask << 1); // Shift on a zero.
                        }
                    }

                } else if (line.startsWith("mem")) {

                    startIdx = line.lastIndexOf('[') + 1;
                    endIdx = line.lastIndexOf(']');

                    // Parse address.
                    String addressString = line.substring(startIdx, endIdx);
                    int address = Integer.parseInt(addressString);

                    // Prase value.
                    startIdx = line.lastIndexOf(' ') + 1;
                    String valString = line.substring(startIdx);

                    long val = Integer.parseInt(valString);

                    // Mask the value.
                    long maskedVal = (val | orMask);// | (val & andMask);
                    maskedVal &= andMask;

                    // If memory already contains a value for this address, get it.
                    int idx = addresses.indexOf(address);

                    if (idx == -1) {
                        addresses.add(address);
                        idx = addresses.indexOf(address);
                        values.add(idx, maskedVal);
                    } else {
                        values.set(idx, maskedVal);
                    }
                }

                line = reader.readLine();
            }
           
            long total = 0;
            for (int i = 0; i < values.size(); i++) {
                total += values.get(i);
            }

            System.out.println(total);

            reader.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}