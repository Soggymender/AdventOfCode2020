import java.time.Duration;
import java.time.Instant;

import java.io.BufferedReader;
import java.io.FileReader;

import java.util.*;

class Game
{
    List<Integer> deck1 = null;
    List<Integer> deck2 = null;

    // These are actually hashes of the start-of-round decks.
    // Totals would be non-commutative, and deck matching is card-order-dependent.
    List<Integer> deck1RoundTotals = new ArrayList<Integer>();
    List<Integer> deck2RoundTotals = new ArrayList<Integer>();

    // Hash generator for a deck of cards.
    // Saves storing and iterating a whole deck for each round.
    // Has to be rebuilt per round because the cards change.
    int hash(List<Integer> deck)
    {
        int hash = 0;

        for (Integer card1 : deck1)
            hash = (hash << 5) - hash + card1.intValue();

        return hash;
    }

    int play() {

        // Start off playing a normal game.
        while (deck1.size() > 0 && deck2.size() > 0) {

            // Calculate deck totals.
            int deck1Hash = hash(deck1);
            int deck2Hash = hash(deck2);

            // Check for player 1 sub-game insta-win.
            for (int i = 0; i < deck1RoundTotals.size(); i++) {
                if (deck1Hash == deck1RoundTotals.get(i) ||
                    deck2Hash == deck2RoundTotals.get(i))
                    return 1;
            }

            // Store totals.
            deck1RoundTotals.add(deck1Hash);
            deck2RoundTotals.add(deck2Hash);            

            Integer draw1 = deck1.remove(0);
            Integer draw2 = deck2.remove(0);

            int subGameWinner = 0;

            // Can we start a new sub-game?
            if (draw1 <= deck1.size() && draw2 <= deck2.size()) {    
               
                
                // Duplicate the decks.
                Game subGame = new Game();
                subGame.deck1 = new ArrayList<Integer>();
                subGame.deck2 = new ArrayList<Integer>();

                for (int i = 0; i < draw1; i++) {
                    subGame.deck1.add(deck1.get(i));
                }

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

    int calculateScore() {

        int total = 0;
        int scalar = 1;

        // Only one of these will run.
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