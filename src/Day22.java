import java.time.Duration;
import java.time.Instant;

import java.io.BufferedReader;
import java.io.FileReader;

import java.util.*;

public class Day22
{
    List<Integer> deck1 = new ArrayList<Integer>();
    List<Integer> deck2 = new ArrayList<Integer>();

    public static void main( String[] args )
    {
        Day22 dayN = new Day22();

        Instant start = Instant.now();

        dayN.loadDeck();
        dayN.play();
        int score = dayN.calculateScore();

        System.out.println(score);

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end); 

        System.out.println((timeElapsed.toMillis()));
    }

    public void loadDeck() {
    
        // Parse lines.
        BufferedReader reader;
        try{
            reader = new BufferedReader(new FileReader("day22_input.txt"));

            boolean inPlayer1 = true;
            boolean inPlayer2 = false;

            String line;
        
            line = reader.readLine();
            line = reader.readLine();

            while (line != null) {
        
                if (inPlayer1) {

                    if (line.length() == 0) {
                        inPlayer1 = false;
                        inPlayer2 = true;

                        line = reader.readLine();
                        line = reader.readLine();
                        continue;
                    }

                    int val = Integer.parseInt(line);
                    deck1.add(val);

                } else if (inPlayer2) {

                    int val = Integer.parseInt(line);
                    deck2.add(val);
                }

                line = reader.readLine();
            }
           
            reader.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /*
        for each round store the starting deck.

        if either player had this deck before player 1 wins game.

        draw

        if either card count < card value
            highest card wins round.


    */


    void play() {

        while (deck1.size() > 0 && deck2.size() > 0) {

            Integer card1 = deck1.remove(0);
            Integer card2 = deck2.remove(0);

            if (card1 > card2) {
                deck1.add(card1);
                deck1.add(card2);
            } else {
                deck2.add(card2);
                deck2.add(card1);
            }
        }
    }

    int calculateScore() {

        int total = 0;
        int scalar = 1;

        while (deck1.size() > 0) {
            total += scalar * deck1.remove(deck1.size() - 1);
            scalar++;
        }

        while (deck2.size() > 0) {
            total += scalar * deck2.remove(deck2.size() - 1);
            scalar++;
        }

        return total;

    }
}