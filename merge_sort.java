import java.util.*;
import java.io.*;

public class merge_sort {
    // Helper class to store number/string pairs
    static class Row {
        int number;
        String text;

        Row(int number, String text) {
            this.number = number;
            this.text = text;
        }

        @Override
        public String toString() {
            return number + "," + text;
        }
    }

    public static void main(String args[]) {
        // Change the filename below to the generated dataset file
        List<Row> rows = readDataset("dataset_506803465.csv");
        int size = rows.size(); // Get the size of the dataset
        String outputFile = "merge_sort_" + size + ".csv"; // Output file name based on the selected range
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
            System.out.println("Merge Sort with Java");
            System.out.println("Dataset Size: " + size);
            // Start time for performance measurement
            long startTime = System.currentTimeMillis();
            // Start the merge sort algorithm
            mergeSort(rows, writer);
            // End time for performance measurement
            long endTime = System.currentTimeMillis();
            // Record the time taken for sorting
            System.out.println("Time Taken: " + (endTime - startTime) + " ms");
            printRowList(rows, writer);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
        System.out.println("Merge sort with Java completed.\nCheck " + outputFile + " for results.");

    }

    // Read the dataset from a CSV file and return a list of Row objects
    static List<Row> readDataset(String filename) {
        List<Row> rows = new ArrayList<>();
        // Read both number and string from CSV file
        // and store them in a list of Row objects
        // Each Row object contains a number and a string
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    try {
                        int number = Integer.parseInt(parts[0]);
                        String text = parts[1];
                        rows.add(new Row(number, text));
                    } catch (NumberFormatException e) {
                        // Skip header or malformed lines
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return rows;
    }

    // Print the list in number/string format
    static void printRowList(List<Row> list, PrintWriter writer) {
        for (int i = 0; i < list.size(); i++) {
            writer.print(list.get(i));
            if (i < list.size() - 1)
                writer.println();
        }
    }

    // Merge sort for List<Row>, sorting by number only
    // This method is the entry point for the merge sort algorithm
    static void mergeSort(List<Row> list, PrintWriter writer) {
        mergeSort(list, 0, list.size() - 1, writer);
    }

    // Merge sort for List<Row>, sorting by number only
    static void mergeSort(List<Row> list, int left, int right, PrintWriter writer) {
        // Recursion base case: if the selectedRows has one or zero elements, it's
        // already sorted
        if (left < right) {
            // 1. Divide: Find the middle index to split the list into two halves
            int mid = (left + right) / 2;
            // 2. Recursion: Sort the left half
            mergeSort(list, left, mid, writer);
            // 2. Recursion: Sort the right half
            mergeSort(list, mid + 1, right, writer);
            // 3. Conquer: Merge the two sorted halves
            merge(list, left, mid, right);
        }
    }

    // Merge two sorted selectedRowss by number using LinkedList
    static void merge(List<Row> list, int left, int mid, int right) {
        // Create LinkedLists for the left and right selectedRowss
        LinkedList<Row> L = new LinkedList<>(list.subList(left, mid + 1));
        LinkedList<Row> R = new LinkedList<>(list.subList(mid + 1, right + 1));

        int k = left;
        // Merge elements from L and R back into the original list in sorted order
        while (!L.isEmpty() && !R.isEmpty()) {
            if (L.getFirst().number <= R.getFirst().number) {
                list.set(k++, L.removeFirst());
            } else {
                list.set(k++, R.removeFirst());
            }
        }
        // Copy any remaining elements from L (if any)
        while (!L.isEmpty()) {
            list.set(k++, L.removeFirst());
        }
        // Copy any remaining elements from R (if any)
        while (!R.isEmpty()) {
            list.set(k++, R.removeFirst());
        }
    }
}