import java.time.Duration;
import java.time.Instant;

public class Day1
{
    public static void main( String[] args )
    {
        Instant start = Instant.now();

        Array array = new Array("day1_input.txt");

        array.findComponentsOf(2020, 5, 0);
     
        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end); 

        System.out.println((timeElapsed.toMillis()));
    }
}