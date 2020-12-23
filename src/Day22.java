import java.time.Duration;
import java.time.Instant;

import java.io.BufferedReader;
import java.io.FileReader;

import java.util.*;

class Game
{
    List<Integer> deck1 = null;
    List<Integer> deck2 = null;

    List<Integer> deck1RoundTotals = new ArrayList<Integer>();
    List<Integer> deck2RoundTotals = new ArrayList<Integer>();

    int play() {

        // Start off playing a normal game.
        while (deck1.size() > 0 && deck2.size() > 0) {

            // For the per-round decks I generate a string and get the hashCode.
            // I could either do this manually, or i could store the current string and append to it
            // or both. I'll try that later and see if it can be faster.
            // This code is basically first pass. I went straight to the hashCode because
            // I did not want the complexity of dealing with arrays of arrays.

            // Calculate deck totals.
            int deckTotal1 = 0;
            String deck1TotalString = new String();  
            for (Integer card1 : deck1) {
                deck1TotalString += (char)card1.intValue();
            }

            int deckTotal2 = 0;
            String deck2TotalString = new String();
            for (Integer card2 : deck2) {
                deck2TotalString += (char)card2.intValue();
            }

            // Check for player 1 sub-game insta-win.
            for (Integer total1 : deck1RoundTotals) {
                if (deck1TotalString.hashCode() == total1) {
                    return 1;
                }
            }
    
            for (Integer total2 : deck2RoundTotals) {
                if (deck2TotalString.hashCode() == total2) {
                    return 1;
                }
            }

            // Store totals.
            deck1RoundTotals.add(deck1TotalString.hashCode());
            deck2RoundTotals.add(deck2TotalString.hashCode());            

            Integer draw1 = deck1.remove(0);
            Integer draw2 = deck2.remove(0);

            int subGameWinner = 0;

            // Can we start a new sub-game?
            if (draw1 <= deck1.size() && draw2 <= deck2.size()) {    
                        
                // Duplicate the decks.
                Game subGame = new Game();
                subGame.deck1 = new ArrayList<Integer>();
                for (int i = 0; i < draw1; i++) {
                    subGame.deck1.add(deck1.get(i));
                }

                subGame.deck2 = new ArrayList<Integer>();
                for (int i = 0; i < draw2; i++) {
                    subGame.deck2.add(deck2.get(i));
                }

                // Play a sub-game with the duplicate decks.
                subGameWinner = subGame.play();
            }
        
            if (subGameWinner == 1 || (subGameWinner == 0 && draw1 > draw2)) {
                deck1.add(draw1);
                deck1.add(draw2);

            } else {
                deck2.add(draw2);
                deck2.add(draw1);
            }

        }

        // Someone ran out of cards. Who won?
        if (deck1.size() == 0)
            return 2;
        else
            return 1;
    }
}

public class Day22
{
    Game rootGame = new Game();

    public static void main( String[] args )
    {
        Day22 dayN = new Day22();

        Instant start = Instant.now();

        dayN.loadDeck();
        dayN.rootGame.play();
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

            rootGame.deck1 = new ArrayList<Integer>();
            rootGame.deck2 = new ArrayList<Integer>();

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
                    rootGame.deck1.add(val);

                } else if (inPlayer2) {

                    int val = Integer.parseInt(line);
                    rootGame.deck2.add(val);
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

    int calculateScore() {

        int total = 0;
        int scalar = 1;

        while (rootGame.deck1.size() > 0) {
            total += scalar * rootGame.deck1.remove(rootGame.deck1.size() - 1);
            scalar++;
        }

        while (rootGame.deck2.size() > 0) {
            total += scalar * rootGame.deck2.remove(rootGame.deck2.size() - 1);
            scalar++;
        }

        return total;

    }
}