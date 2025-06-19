import java.util.*;
import java.io.*;

public class quick_sort {
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
        List<Row> rows = readDataset("dataset_10000000.csv");
        int size = rows.size(); // Get the size of the dataset
        String outputFile = "quick_sort_" + size + ".csv"; // Output file name based on the selected range
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
            System.out.println("Quick Sort with Java");
            System.out.println("Dataset Size: " + size);
            // Start time for performance measurement
            long startTime = System.currentTimeMillis();
            // Start the quick sort algorithm
            quickSort(rows, writer);
            // End time for performance measurement
            long endTime = System.currentTimeMillis();
            // Record the time taken for sorting
            System.out.println("Time Taken: " + (endTime - startTime) + " ms");
            printRowList(rows, writer);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
        System.out.println("Quick sort with Java completed.\nCheck " + outputFile + " for results.");
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

    // Quick sort for List<Row>, sorting by number only
    static void quickSort(List<Row> list, PrintWriter writer) {
        quickSort(list, 0, list.size() - 1, writer);
    }

    // Recursive Quick Sort
    static void quickSort(List<Row> list, int low, int high, PrintWriter writer) {
        if (low < high) {
            int pivotIndex = partition(list, low, high);
            quickSort(list, low, pivotIndex - 1, writer);
            quickSort(list, pivotIndex + 1, high, writer);
        }
    }

    // Partition logic for Quick Sort
    static int partition(List<Row> list, int low, int high) {
        Row pivot = list.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (list.get(j).number <= pivot.number) {
                i++;
                // Swap list[i] and list[j]
                Row temp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
            }
        }
        // Swap list[i + 1] and list[high] (or pivot)
        Row temp = list.get(i + 1);
        list.set(i + 1, list.get(high));
        list.set(high, temp);
        return i + 1;
    }
}