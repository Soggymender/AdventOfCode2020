import java.time.Duration;
import java.time.Instant;

public class Day1
{
    public static void main( String[] args )
    {
        Instant start = Instant.now();

        Array array = new Array("day1_input.txt");

        // Work.
        for (int i = 0; i < array.end; i++) {

            int a = array.elements[i];
            int b = 2020 - a;

            int result = array.BinarySearch(b);
            if (result != -1) {
                System.out.println(result + ": " + a + " + " + array.elements[result]);
                break;
            }
        }
     
        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end); 

        System.out.println((timeElapsed.toMillis()));
    }
}