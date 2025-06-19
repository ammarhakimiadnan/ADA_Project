import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class quick_sort_step {
    
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
            return number + "/" + text;
        }
    }

    public static void main(String args[]) {

        List<Row> rows = readDataset("dataset_sample_1000.csv");

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Select start row: ");
            int start = scanner.nextInt();
            System.out.print("Select end row: ");
            int end = scanner.nextInt();

            // Copy the selected range into a new list
            List<Row> selectedRows = new ArrayList<>(rows.subList(start - 1, end));

            // Output file name based on the selected range
            String outputFile = "quick_sort_step_" + start + "_" + end + ".txt";
            try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
                // Print the selected rows before sorting
                printRowList(selectedRows, writer);
                // Start the quick sort algorithm
                quickSort(selectedRows, writer);
            } catch (IOException e) {
                System.out.println("Error writing to file: " + e.getMessage());
            }
            System.out.println("quick sort with Java completed.\nCheck " + outputFile + " for results.");
        }
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

    // Print the list in [number/string, ...] format
    static void printRowList(List<Row> list, PrintWriter writer) {
        writer.print("[");
        for (int i = 0; i < list.size(); i++) {
            writer.print(list.get(i));
            if (i < list.size() - 1)
                writer.print(", ");
        }
        writer.println("]");
    }


    // Entry point for quick sort algorithm
    static void quickSort(List<Row> list, PrintWriter writer) {
        quickSort(list, 0, list.size() - 1, writer);
    }

    // Recursive Quick Sort with Pivot
    static void quickSort(List<Row> list, int low, int high, PrintWriter writer) {
        if (low < high) {
            int pivotIndex = partition(list, low, high, writer);
            quickSort(list, low, pivotIndex - 1, writer);
            quickSort(list, pivotIndex + 1, high, writer);
        }
    }

    // Partition logic with inline swapping
    static int partition(List<Row> list, int low, int high, PrintWriter writer) {
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

        // Print the pivot index on the left, then the list state
        writer.print("pi=" + (i + 1) + " [");
        for (int idx = 0; idx < list.size(); idx++) {
            writer.print(list.get(idx));
            if (idx < list.size() - 1)
                writer.print(", ");
        }
        writer.println("]");

        return i + 1;
    }
}
