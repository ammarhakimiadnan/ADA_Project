import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

class Row {
    int number;
    String text;
    public Row(int number, String text) {
        this.number = number;
        this.text = text;
    }
}

public class binary_search {
    public static List<Row> readDataset(String filename) {
        List<Row> rows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts.length == 2) {
                    try {
                        int number = Integer.parseInt(parts[0]);
                        rows.add(new Row(number, parts[1]));
                    } catch (NumberFormatException e) {
                        System.out.println("Skipping invalid row: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + filename);
        }
        return rows;
    }

    public static int binarySearch(List<Row> data, int target) {
        int left = 0;
        int right = data.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (data.get(mid).number == target) {
                return mid;
            } else if (data.get(mid).number < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }

    public static int extractNFromFilename(String filename) {
        int start = filename.indexOf("_sort_");
        int end = filename.indexOf(".csv");
        if (start == -1 || end == -1) {
            return -1;
        }
        String nStr = filename.substring(start + 6, end);
        return Integer.parseInt(nStr);
    }

    public static void runBinarySearchTests(List<Row> data, String outputFilename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFilename))) {
            int n = data.size();
            Random rand = new Random();

            // Best case: middle element (O(1))
            int bestTarget = data.get(n / 2).number;
            long start = System.nanoTime();
            for (int i = 0; i < n; i++) {
                binarySearch(data, bestTarget);
            }
            long durationBest = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
            writer.println("Best case time (ms): " + durationBest);

            // Average case: random elements (O(log n))
            start = System.nanoTime();
            for (int i = 0; i < n; i++) {
                int randomIndex = rand.nextInt(n);
                binarySearch(data, data.get(randomIndex).number);
            }
            long durationAvg = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
            writer.println("Average case time (ms): " + durationAvg);

            // Worst case: non-existent element (O(log n))
            int worstTarget = -1;  // Guaranteed not to exist
            start = System.nanoTime();
            for (int i = 0; i < n; i++) {
                binarySearch(data, worstTarget);
            }
            long durationWorst = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
            writer.println("Worst case time (ms): " + durationWorst);

        } catch (IOException e) {
            System.out.println("Error writing to file: " + outputFilename);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Enter dataset filename (e.g., merge_sort_10000.csv): ");
            String filename = scanner.nextLine();

            List<Row> rows = readDataset(filename);
            if (rows.isEmpty()) {
                System.out.println("No data loaded. Exiting.");
                return;
            }

            int n = extractNFromFilename(filename);
            if (n == -1) {
                System.out.println("Invalid filename format. Exiting.");
                return;
            }

            String outputFile = "binary_search_" + n + ".txt";
            runBinarySearchTests(rows, outputFile);

            System.out.println("Binary search performance test completed.\nResults saved to " + outputFile);
        } finally {
            scanner.close();
        }
    }
}