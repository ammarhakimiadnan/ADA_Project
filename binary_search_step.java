import java.io.*;
import java.util.*;

class Row {
    int number;
    String text;
    
    public Row(int number, String text) {
        this.number = number;
        this.text = text;
    }
    
    @Override
    public String toString() {
        return number + "/" + text;
    }
}

public class binary_search_step {
    public static List<Row> readDataset(String filename) throws IOException {
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
                        System.err.println("Warning: Skipped invalid row: " + line);
                    }
                }
            }
        }
        return rows;
    }

    public static boolean isDatasetSorted(List<Row> data) {
        for (int i = 1; i < data.size(); i++) {
            if (data.get(i).number < data.get(i-1).number) {
                return false;
            }
        }
        return true;
    }

    public static void binarySearchStep(List<Row> data, int target, PrintWriter writer) {
        int left = 0;
        int right = data.size() - 1;
        boolean found = false;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            writer.print(mid + ": " + data.get(mid).toString());

            if (data.get(mid).number == target) {
                found = true;
                break;
            } else if (data.get(mid).number < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
            writer.println(); // Add newline after each step except the last
        }

        if (!found) {
            writer.print(-1); // No newline after -1
        }
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter dataset filename (e.g., merge_sort_10000.csv): ");
        String filename = scanner.nextLine();

        List<Row> rows;
        try {
            rows = readDataset(filename);
            if (rows.isEmpty()) {
                System.err.println("Error: No valid data loaded.");
                return;
            }

            if (!isDatasetSorted(rows)) {
                System.err.println("Error: Dataset is not sorted.");
                return;
            }

            System.out.print("Enter target integer: ");
            int target = scanner.nextInt();

            String outputFile = "binary_search_step_" + target + ".txt";
            try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
                binarySearchStep(rows, target, writer);
                System.out.println("Results saved to " + outputFile);
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}