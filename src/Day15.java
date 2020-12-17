import java.time.Duration;
import java.time.Instant;

//import java.io.BufferedReader;
//import java.io.FileReader;

//import java.util.*;

public class Day15
{

    int i;
    int prevVal;

    public static void main( String[] args )
    {
        Day15 dayN = new Day15();

        Instant start = Instant.now();

        dayN.predict();

        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end); 

        System.out.println((timeElapsed.toMillis()));

        System.out.println(dayN.i + " " + dayN.prevVal);

    }

    public void predict() {
    
        int[] startingVals = new int[] { 1, 0, 18, 10, 19, 6 };// { 0, 3, 6 };

        int[] plays = new int[30000000];
        int numPlays = 0;

        for (int i = 0; i < startingVals.length; i++) {
            plays[startingVals[i]] = i + 1;
        }

        numPlays = startingVals.length;
        
        prevVal = startingVals[startingVals.length - 1];
        plays[prevVal] = numPlays; // Previously played value stores 0 meaning it was not previously played.

        int playCount = 0;

        //int plays0 = 1;
        //plays[0] = plays0;
        int plays0 = plays[0];

        for (i = numPlays + 1; i <= plays.length; i++) {

            if (playCount == 0) {
                prevVal = 0;
                playCount = plays0;
                plays0 = i;
            } else {
                prevVal = i - 1 - playCount;
                playCount = plays[prevVal];
                plays[prevVal] = i;
            }
        }

        plays[prevVal] = i - 1;
    }
}