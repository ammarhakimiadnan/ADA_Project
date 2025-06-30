import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class binary_search {
    
    static class DataEntry {
        final int intVal;
        final String strVal;
        
        DataEntry(int intVal, String strVal) {
            this.intVal = intVal;
            this.strVal = strVal;
        }
    }
    
    public static boolean isDatasetSorted(List<DataEntry> data) {
        for (int i = 1; i < data.size(); i++) {
            if (data.get(i).intVal < data.get(i-1).intVal) {
                return false;
            }
        }
        return true;
    }
    
    public static List<DataEntry> loadDataset(String filename) throws IOException {
        List<DataEntry> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts.length == 2) {
                    try {
                        int intVal = Integer.parseInt(parts[0].trim());
                        data.add(new DataEntry(intVal, parts[1].trim()));
                    } catch (NumberFormatException e) {
                        System.err.println("Warning: Skipped invalid row: " + line);
                    }
                }
            }
        }
        
        if (!isDatasetSorted(data)) {
            System.err.println("Error: Dataset must be pre-sorted");
            return Collections.emptyList();
        }
        
        return data;
    }
    
    public static int binarySearch(List<DataEntry> data, int target) {
        int left = 0;
        int right = data.size() - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int midVal = data.get(mid).intVal;
            
            if (midVal == target) {
                return mid;
            } else if (midVal < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }
    
    public static void runBinarySearchTests(List<DataEntry> data, String filename) throws IOException {
        int n = data.size();
        String outputFilename = "binary_search_" + n + ".txt";
        
        for (int i = 0; i < 1000; i++) {
            binarySearch(data, data.get(0).intVal);
        }

        // Best case (middle element)
        int bestTarget = data.get(n/2).intVal;
        long bestStart = System.nanoTime();
        for (int i = 0; i < n; i++) {
            binarySearch(data, bestTarget);
        }
        double bestTime = (System.nanoTime() - bestStart) / 1_000_000.0 / n;

        // Average case (random elements)
        Random rand = new Random();
        long avgStart = System.nanoTime();
        for (int i = 0; i < n; i++) {
            int randomIndex = rand.nextInt(n);
            binarySearch(data, data.get(randomIndex).intVal);
        }
        double avgTime = (System.nanoTime() - avgStart) / 1_000_000.0 / n;

        // Worst case (non-existent element)
        int worstTarget = data.get(n-1).intVal + 1;
        long worstStart = System.nanoTime();
        for (int i = 0; i < n; i++) {
            binarySearch(data, worstTarget);
        }
        double worstTime = (System.nanoTime() - worstStart) / 1_000_000.0 / n;

        try (PrintWriter writer = new PrintWriter(outputFilename)) {
            writer.printf("Best case time: %.6f ms%n", bestTime);
            writer.printf("Average case time: %.6f ms%n", avgTime);
            writer.printf("Worst case time: %.6f ms%n", worstTime);
        }
    }
    
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java binary_search <dataset_filename>");
            System.exit(1);
        }
        
        try {
            List<DataEntry> data = loadDataset(args[0]);
            if (data.isEmpty()) {
                System.err.println("Error: No valid data loaded or data not sorted");
                System.exit(1);
            }
            
            runBinarySearchTests(data, args[0]);
            System.out.println("Results saved to binary_search_" + data.size() + ".txt");
            
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}