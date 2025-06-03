// DatasetGenerator.java
// Input: User-defined size of the dataset
// Output: A CSV file containing unique integers and random strings

import java.util.*;     // For file handling and random number generation
import java.io.*;       // For buffered writing to files
import java.nio.file.*; // For file path handling

public class DatasetGenerator {
    private static final int MIN_INTEGER = 1000000000;
    private static final int MAX_INTEGER = 2000000000;
    private static final int MIN_STRING_LENGTH = 4;                         // String lengths between 4
    private static final int MAX_STRING_LENGTH = 6;                         // and 6 characters            
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz";  // Characters to use in random strings
    
    public static void main(String[] args) {
        int datasetSize = determineDatasetSize();                                       // Determine dataset size based on the problem statement
        long startTime = System.currentTimeMillis();                                    // Start time for performance measurement
        String filename = "dataset_" + datasetSize + ".csv";                            // Output filename based on dataset size
        
        try {
            generateCSV(filename, datasetSize);                                         // Generate the CSV dataset
            long endTime = System.currentTimeMillis();                                  // End time for performance measurement
            
            System.out.println("Dataset successfully written to " + filename);         
            System.out.println("Generation time: " + (endTime - startTime) + " ms");
            System.out.println("File size: " + String.format("%,d", Files.size(Paths.get(filename))) + " bytes");
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    // Method to generate the CSV file with unique integers and random strings
    private static void generateCSV(String filename, int datasetSize) throws IOException {
        Set<Integer> usedIntegers = new HashSet<>();                                    // To ensure unique integers
        Random random = new Random();           
        
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filename))) {
            // Generate and write data
            for (int i = 0; i < datasetSize; i++) {
                // Generate unique integer
                int randomInt;
                do {
                    randomInt = MIN_INTEGER + random.nextInt(MAX_INTEGER - MIN_INTEGER + 1);
                } while (usedIntegers.contains(randomInt));
                usedIntegers.add(randomInt);
                
                // Generate random string
                String randomString = generateRandomString(random);
                
                // Write CSV line
                writer.write(randomInt + "," + randomString);
                if(i < datasetSize - 1) {
                    writer.write("\n");                                                 // Add a new line except for the last entry
                }
            }
        }
    }
    
    // Method to generate a random string of length between the minimum and maximum string lengths
    private static String generateRandomString(Random random) {
        int stringLength = MIN_STRING_LENGTH + random.nextInt(MAX_STRING_LENGTH - MIN_STRING_LENGTH + 1);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < stringLength; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
    
    // Method to adjust of dataset size
    private static int determineDatasetSize() {
        // Use a Scanner to read user input for dataset size
        int size;
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("Enter the number of entries (positive integer): ");
                try {
                    size = scanner.nextInt();
                    if (size > 0) {
                        return size;                                                    // Valid input, return the size
                    } else {
                        System.out.println("Error: Input must be a positive integer. Try again.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Error: Invalid input. Please enter a positive integer.");
                    scanner.next();                                                     // Clear the invalid input from the scanner buffer
                }
            }
        }
    }
}