import java.time.Duration;
import java.time.Instant;

import java.io.BufferedReader;
import java.io.FileReader;

public class Day12
{
    public enum Heading {

        N,
        E,
        S,
        W,
        NUM_HEADINGS
    }

    int xDist = 0;
    int yDist = 0;

    String line;

    public static void main( String[] args )
    {
        Day12 day12 = new Day12();

        Instant start = Instant.now();

        day12.route();

        System.out.println(day12.xDist + ", " + day12.yDist);

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end); 

        System.out.println((timeElapsed.toMillis()));
    }

    public void route() {
    
        // Parse lines.
        BufferedReader reader;
        try{
            reader = new BufferedReader(new FileReader("day12_input.txt"));

            Heading dir = Heading.E;
            int val = 0;

            String dirString;
            char c;

            line = reader.readLine();
            while (line != null) {
        
                dirString = line.substring(0, 1);

                val = Integer.parseInt(line.substring(1));

                switch (dirString) {
                    
                    case "N":
                        move(Heading.N, val);                
                        break;

                    case "E":
                        move(Heading.E, val);
                        break;

                    case "S":
                        move(Heading.S, val);
                        break;

                    case "W":
                        move(Heading.W, val);
                        break;

                    case "L": {
                        int angle = Heading.NUM_HEADINGS.ordinal() - val / 90;//Heading.NUM_HEADINGS.ordinal();
                        dir = Heading.values()[(dir.ordinal() + angle) % Heading.NUM_HEADINGS.ordinal()];
                        break;
                    }

                    case "R": {
                        int angle = val / 90;//Heading.NUM_HEADINGS.ordinal();
                        dir = Heading.values()[(dir.ordinal() + angle) % Heading.NUM_HEADINGS.ordinal() ];
                        break;
                    }

                    case "F":
                        move(dir, val);
                        break;

                    default:
                        break;

                }

                line = reader.readLine();
            }
           
            reader.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void move(Heading heading, int dist) {
            
        switch (heading) {
                        
            case N:
                yDist += dist;                        
                break;

            case E:
                xDist += dist;
                break;

            case S:
                yDist -= dist;
                break;

            case W:
                xDist -= dist;
                break;
        }
    }
}