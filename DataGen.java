import java.util.*;
import java.io.*;
import java.nio.file.*;

public class DataGen {
    private static final int MIN_INTEGER = 1000000000;
    private static final int MAX_INTEGER = 2000000000;
    private static final int MIN_STRING_LENGTH = 4;
    private static final int MAX_STRING_LENGTH = 6;
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String CSV_HEADER = "integer,string\n";
    
    public static void main(String[] args) {
        int datasetSize = determineDatasetSize();
        System.out.println("Generating dataset with " + datasetSize + " entries...");
        
        long startTime = System.currentTimeMillis();
        String filename = "Dataset_" + datasetSize + ".csv";
        
        try {
            generateCSV(filename, datasetSize);
            long endTime = System.currentTimeMillis();
            
            System.out.println("Dataset successfully written to " + filename);
            System.out.println("Generation time: " + (endTime - startTime) + " ms");
            System.out.println("File size: " + 
                String.format("%,d", Files.size(Paths.get(filename))) + " bytes");
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    // Determine dataset size based on the problem statement
    private static void generateCSV(String filename, int datasetSize) throws IOException {
        // Use a simple array to track used numbers (memory intensive but comply to the problem constraints)
        boolean[] usedNumbers = new boolean[MAX_INTEGER + 1];       // Boolean array to make sure the unique integers are generated
        Random random = new Random();
        int[] generatedNumbers = new int[datasetSize];
        
        // Generate unique numbers first
        for (int i = 0; i < datasetSize; i++) {
            int randomInt;
            do {
                randomInt = MIN_INTEGER + random.nextInt(MAX_INTEGER - MIN_INTEGER + 1);    // Generate a random integer in the specified range
            } while (usedNumbers[randomInt]);
            usedNumbers[randomInt] = true;
            generatedNumbers[i] = randomInt;
            
            if (i > 0 && i % 1_000_000 == 0) {
                System.out.println("Generated " + String.format("%,d", i) + " numbers...");
            }
        }
        
        // Shuffle the array manually
        for (int i = generatedNumbers.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = generatedNumbers[i];
            generatedNumbers[i] = generatedNumbers[j];
            generatedNumbers[j] = temp;
        }
        
        // Write to CSV with random strings
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filename))) {
            writer.write(CSV_HEADER);
            
            for (int i = 0; i < datasetSize; i++) {
                writer.write(generatedNumbers[i] + "," + generateRandomString(random) + "\n");
                
                if (i > 0 && i % 1_000_000 == 0) {
                    System.out.println("Written " + String.format("%,d", i) + " entries...");
                }
            }
        }
    }
    
    // Method to generate a random string of length between MIN_STRING_LENGTH and MAX_STRING_LENGTH.
    private static String generateRandomString(Random random) {
        int length = MIN_STRING_LENGTH + random.nextInt(MAX_STRING_LENGTH - MIN_STRING_LENGTH + 1);
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
    
    private static int determineDatasetSize() {
        return 1000;        // Change this value to adjust the dataset size as needed
    }
}