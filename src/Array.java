import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Array {

    public int[] elements = null;
    int length = 0;
    int end = 0;

    Array(String filename) {

        File file = new File(filename);

        // Count lines. Gross.
        {
            BufferedReader reader;
            try{
                reader = new BufferedReader(new FileReader(file));

                int lineCount = 0;

                String line;
                while ((line = reader.readLine()) != null) {
                    lineCount++;
                }        

                elements = new int[lineCount];
                length = lineCount;
        
                reader.close();

            } catch (Exception e) {
                System.out.println(e);
            }
        }

        // Parse lines.
        {
            BufferedReader reader;
            try{
                reader = new BufferedReader(new FileReader(file));

                String line;
                while ((line = reader.readLine()) != null) {
                    int val = Integer.parseInt(line);
                    OrderedInsert(val);
                }

                reader.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    void OrderedInsert(int value) {

        for (int i = end; i > 0; i--) {

            if (value < elements[i - 1]) {
                elements[i] = elements[i - 1];
            } else {
                elements[i] = value;
                end++;
                return;
            }
        }

        // If we get this far, value goes at 0.
        elements[0] = value;
        end++;
    } 

    public int BinarySearch(int value) {

        int low = 0;
        int high = end - 1;
        int mid;

        do {

            mid = low + (high - low) / 2;

            if (elements[mid] == value) {
                return mid;
            }

            if (value > elements[mid]) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }

        } while (low != high);

        return -1;
    }

    public void Display() {

        for (int i = 0; i < end; i++) {
            if (i > 0) {
                System.out.print(", ");
            }

            System.out.print(elements[i]);
        }

        System.out.println();
        System.out.println();
    }    
}