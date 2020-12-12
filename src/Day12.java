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

    int xWaypoint = 10;
    int yWaypoint = 1;

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

            int val = 0;

            String dirString;
            char c;

            line = reader.readLine();
            while (line != null) {
        
                dirString = line.substring(0, 1);

                val = Integer.parseInt(line.substring(1));

                switch (dirString) {
                    
                    case "N":
                        moveWaypoint(Heading.N, val);                
                        break;

                    case "E":
                        moveWaypoint(Heading.E, val);
                        break;

                    case "S":
                        moveWaypoint(Heading.S, val);
                        break;

                    case "W":
                        moveWaypoint(Heading.W, val);
                        break;

                    case "L": {
                        int angle = -val / 90;
                        turnWaypoint(angle);
                        break;
                    }

                    case "R": {
                        int angle = val / 90;
                        turnWaypoint(angle);
                        break;
                    }

                    case "F":
                        move(val);
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

    void move(int dist) {
            
        xDist += xWaypoint * dist;
        yDist += yWaypoint * dist;
    }

    void moveWaypoint(Heading heading, int dist) {
            
        switch (heading) {
                        
            case N:
                yWaypoint += dist;                        
                break;

            case E:
                xWaypoint += dist;
                break;

            case S:
                yWaypoint -= dist;
                break;

            case W:
                xWaypoint -= dist;
                break;
        }
    }
    
    void turnWaypoint(int turns) {

        int temp;
        while (turns > 0) {
            temp = xWaypoint;
            xWaypoint = yWaypoint;
            yWaypoint = -temp;
            
            turns--;
        }

        while (turns < 0) {
            temp = xWaypoint;
            xWaypoint = -yWaypoint;
            yWaypoint = temp;

            turns++;
        }
    }
}