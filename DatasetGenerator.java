import java.util.*;     // For file handling and random number generation
import java.io.*;       // For buffered writing to files
import java.nio.file.*; // For file path handling

public class DatasetGenerator {
    private static final int MIN_INTEGER = 1000000000;  // 1 billion
    private static final int MAX_INTEGER = 2000000000;  // 2 billion
    private static final int MIN_STRING_LENGTH = 4;                         // String lengths between 4
    private static final int MAX_STRING_LENGTH = 6;                         // and 6 characters            
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz";  // Characters to use in random strings
    private static final String CSV_HEADER = "integer,string\n";            // CSV header for the dataset
    
    public static void main(String[] args) {
        int datasetSize = determineDatasetSize();                                       // Determine dataset size based on the problem statement
        System.out.println("Generating dataset with " + datasetSize + " entries...");   // Displaying the dataset size
        
        long startTime = System.currentTimeMillis();                                    // Start time for performance measurement
        String filename = "random_dataset_" + datasetSize + ".csv";                     // Output filename based on dataset size
        
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
    
    private static void generateCSV(String filename, int datasetSize) throws IOException {
        Set<Integer> usedIntegers = new HashSet<>();
        Random random = new Random();
        
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filename))) {
            // Write CSV header
            writer.write(CSV_HEADER);
            
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
                writer.write(randomInt + "," + randomString + "\n");
                
                // Progress reporting
                if (i > 0 && i % 1_000_000 == 0) {
                    System.out.println("Generated " + String.format("%,d", i) + " entries...");
                }
            }
        }
    }
    
    // Method to generate a random string of length between MIN_STRING_LENGTH and MAX_STRING_LENGTH.
    private static String generateRandomString(Random random) {
        int stringLength = MIN_STRING_LENGTH + random.nextInt(MAX_STRING_LENGTH - MIN_STRING_LENGTH + 1);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < stringLength; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
    
    // Method to adjust of dataset size.
    private static int determineDatasetSize() {
        return 10000;   // 10 thousand entries should create significant sorting time differences
    }
}