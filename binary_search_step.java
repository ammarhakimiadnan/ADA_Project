import java.io.*;
import java.util.*;

class Row {
    int number;
    String text;
    
    public Row(int number, String text) {
        this.number = number;
        this.text = text;
    }
    
    public String toString() {
        return number + "/" + text;
    }
}

public class binary_search_step{
    
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
                        // Skip rows with invalid numbers
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + filename);
        }
        return rows;
    }
    
    public static void binarySearchStep(List<Row> data, int target, PrintWriter writer) {
        int left = 0;
        int right = data.size() - 1;
        boolean found = false;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            writer.println(mid + ": " + data.get(mid).toString());
            
            int midValue = data.get(mid).number;
            if (midValue == target) {
                found = true;
                break;
            } else if (midValue < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        if (!found) {
            writer.println(-1);
        }
    }
    
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter dataset filename (e.g. merge_sort_10000.csv): ");
            String filename = scanner.nextLine();
            
            List<Row> rows = readDataset(filename);
            if (rows.isEmpty()) {
                System.out.println("No data loaded. Exiting.");
                return;
            }
            
            System.out.print("Enter target integer: ");
            int target = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            String outputFile = "binary_search_step_" + target + ".txt";
            try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
                binarySearchStep(rows, target, writer);
                System.out.println("Binary search with Java completed.\nCheck " + outputFile + " for results.");
            } catch (IOException e) {
                System.out.println("Error writing to file: " + outputFile);
            }
        }
    }
}