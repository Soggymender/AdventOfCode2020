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
                    orderedInsert(val);
                }

                reader.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    void orderedInsert(int value) {

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

    public int binarySearch(int value) {

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

        } while (low <= high);

        return -1;
    }

    public int findComponentsOf(int total, int n, int low) {

        // At depth. Last shot.
        if (n == 1) {
            int idx = binarySearch(total);
            if (idx != -1) {
                System.out.println(n + ": " + elements[idx]);
            }

            return idx;
        }

        // Work.
        for (int i = low; i < end; i++) {

            int a = elements[i];
            int b = total - a;

            int idx = findComponentsOf(b, n - 1, i + 1);
            if (idx != -1) {
                System.out.println(n + ": " + elements[i]);
                return idx;
            }
        }
        
        return -1;
    }

    public void display() {

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