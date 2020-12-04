import java.io.File;

import java.time.Duration;
import java.time.Instant;

import java.io.BufferedReader;
import java.io.FileReader;

import java.util.ArrayList;
import java.util.List;

class Map {

    char[][] map = null;
    int width = 0;
    int height = 0;

    public void load(String filename) {

        File file = new File(filename);

        // Calculate width and height.
        {
            BufferedReader reader;
            try{
                reader = new BufferedReader(new FileReader(file));

                String line;
                while ((line = reader.readLine()) != null) {

                    if (line.length() > width) {
                        width = line.length();
                    }

                    height++;

                    // Test bale out at 10.
                    if (height == 10) {
                        break;
                    }
                }        

                reader.close();

            } catch (Exception e) {
                System.out.println(e);
            }
        }

        map = new char[width][height];

        // Parse lines.
        {
            BufferedReader reader;
            try{
                reader = new BufferedReader(new FileReader(file));

                int y = 0;

                String line;
                while ((line = reader.readLine()) != null) {

                    for (int x = 0; x < width; x++) {
                        map[x][y] = line.charAt(x);
                    }

                    y++;

                    // Test bale out at 10.
                    if (y == 10) {
                        break;
                    }
                }

                reader.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public void display() {

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.print(map[x][y]);
            }
            System.out.println();
        }       
    }

    public void path(int sX, int sY, int eX, int eY) {

        List<Node> open = new ArrayList<Node>();
        List<Node> closed = new ArrayList<Node>();


    }
}

class Node {

    Node parent = null;

    float f = 0;
    float g = 0;
    float h = 0;
}

public class DAStar
{
    public static void main( String[] args )
    {
        Instant start = Instant.now();

        Map map = new Map();

        map.load("dastar_input.txt");

        int sX = 0;
        int sY = 0;
        int eX = map.width - 1;
        int eY = map.height - 1;

        map.path(sX, sY, eX, eY);

        map.display();

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end); 

        System.out.println((timeElapsed.toMillis()));
    }
}


