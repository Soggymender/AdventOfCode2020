import java.time.Duration;
import java.time.Instant;

import java.io.BufferedReader;
import java.io.FileReader;

import java.util.*;

class State 
{
    int pos = 0;
}

public class Day18
{
    public static void main( String[] args )
    {
        Day18 dayN = new Day18();

        Instant start = Instant.now();

        dayN.math();

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end); 

        System.out.println((timeElapsed.toMillis()));
    }

    public void math() {
    
        // Parse lines.
        BufferedReader reader;
        try{
            reader = new BufferedReader(new FileReader("day18_input.txt"));

            String line;
            char c;
     
            long total = 0;
            State state = new State();

            line = reader.readLine();
            while (line != null) {
    
                total += calculate(line, state);
                state.pos = 0;

                line = reader.readLine();
           }
           
           System.out.println(total);

            reader.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    long calculate(String line, State state) {

        long result = 0;

        char c;

        long val = 0;

        boolean add = false;
        boolean mul = false;

        boolean valReady = false;

        // Decode row.
        for (; state.pos < line.length(); state.pos++) {

            valReady = false;

            // Grab char.
            c = line.charAt(state.pos);

            if (c == ' ')
                continue;

            if (c == '(') {
                state.pos++;
                val = calculate(line, state);
                valReady = true;
            }

            if (c == ')') {
                return result;
            }

            if (c == '+') {
                add = true;
                continue;
            }

            if (c == '*') {
                mul = true;
                continue;
            }

            if (!valReady)
                val = c - '0';

            if (add) {
                result += val;
                add = false;
            } else if (mul) {
                result *= val;
                mul = false;
            } else {
                result = val;
            }
        }

        return result;
    }
}