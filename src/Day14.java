import java.time.Duration;
import java.time.Instant;

import java.io.BufferedReader;
import java.io.FileReader;

import java.util.List;
import java.util.ArrayList;

public class Day14
{
    List<Long> addresses = new ArrayList<Long>();
    List<Long> values = new ArrayList<Long>();

    long orMask = 0;
    long floatMask = 0;

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
     
            int lineCounter = 0;

            line = reader.readLine();
            while (line != null) {
        
                System.out.println(lineCounter++);

                if (line.startsWith("mask")) {

                    startIdx = line.lastIndexOf(' ') + 1;
                    String maskString = line.substring(startIdx);

                    orMask = 0;
                    floatMask = 0;

                    // Each mask is split into an Or and an An mask.
                    for (int i = 0; i < maskString.length(); i++) {

                        c = maskString.charAt(i);

                        if (c == 'X') {
                            orMask = orMask << 1;
                            floatMask = (floatMask << 1) | 1; // Shift on a one.
                        } else if (c == '1') {
                            orMask = (orMask << 1) | 1;
                            floatMask = (floatMask << 1); // Shift on a zero.
                        } else if (c == '0') {
                            orMask = (orMask << 1);
                            floatMask = (floatMask << 1); // Shift on a zero.
                        }
                    }

                } else if (line.startsWith("mem")) {

                    startIdx = line.lastIndexOf('[') + 1;
                    endIdx = line.lastIndexOf(']');

                    // Parse address.
                    String addressString = line.substring(startIdx, endIdx);
                    long address = Long.parseLong(addressString);

                    // Prase value.
                    startIdx = line.lastIndexOf(' ') + 1;
                    String valString = line.substring(startIdx);

                    long val = Long.parseLong(valString);

                    // Mask the value.
                    long maskedAddress = (address | orMask);
                    
                    long floatingAddress = maskedAddress;

                    int bitCount = countBits(floatMask);
                    long max = (long)Math.pow(2.0, (double)bitCount);

                    // Start at value 0 and iterate through max.
                    long counter = 0;
                    long expandedCounter;
                    long oMask;
                    long aMask;

                    int idx;

                    // This needs to be a do while in case one of the masks is emtpy.
                    do {

                        // Expand the current collapsed value to the mask bit orientation.
                        expandedCounter = expand(counter, floatMask);

                        oMask = expandedCounter & floatMask;     // Or mask forces ones where the expanded counter and float mask both have ones.
                        aMask = ~(expandedCounter ^ floatMask);  // And mask forces zeros where the expanded counter has zeros and the float mask has ones.

                        // Apply the mask to the floating address.
                        floatingAddress = (maskedAddress | oMask) & aMask;

                        // If memory already contains a value for this address, get it.
                        idx = addresses.indexOf(floatingAddress);

                        if (idx == -1) {
                            addresses.add(floatingAddress);
                            idx = addresses.indexOf(floatingAddress);
                            values.add(idx, val);
                        } else {
                            values.set(idx, val);
                        }

                        // Increment the collapsed value.
                        counter++;

                    } while (counter <= max);
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

    // Count the number of 1 bits in a mask.
    int countBits(long address) {

        int bitCount = 0;
        while (address > 0) {
            if ((address & (long)1) > 0) {
                bitCount++;
            }

            address >>= 1;
        }

        return bitCount;
    }

    /*
    Expand a collapsed value into an Or and And mask that match the bit pattern of a source mask.
    */
    long expand(long val, long mask) {

        // find 1s in the mask.
        // count the bit location of each 1.
        // when 1 is found, shift the next val bit left by that amount.
        // shift value right to nuke the just-placed bit.

        long expandedValue = 0;

        for (int i = 0; i < 36; i++) {

            // Found a significant bit in the mask. The next significant bit in the value needs to be shifted here.
            if ((mask & (long)1) == 1) {
                
                // If the least significant bit of value is 1, shift it left into place.
                if ((val & (long)1) > 0) {
                    expandedValue |= ((long)1 << i);
                }

                // Shift the least significant bit of value off the right end.
                val >>= 1;
            }

            mask >>= 1;
        }

        return expandedValue;
    }
}