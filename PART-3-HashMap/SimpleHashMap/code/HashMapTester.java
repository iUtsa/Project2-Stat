import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Tester class for ArnabSimpleHashMap
 * Runs various experiments and generates performance metrics
 */
public class HashMapTester {
    
    private static final int[] DATA_SIZES = {1000, 5000, 10000, 50000, 100000};
    private static final int LOOKUP_OPERATIONS = 10000;
    private static final int RANDOM_STRING_LENGTH = 10;
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random RANDOM = new Random();
    
    public static void main(String[] args) {
        System.out.println("Starting HashMap Performance Tests...");
        
        // Run experiments
        testInsertion();
        testLookup();
        testBucketDistribution();
        testResizing();
        
        System.out.println("Tests completed. Check the CSV files for results.");
    }
    
    /**
     * Test insertion performance
     */
    private static void testInsertion() {
        System.out.println("\nTesting Insertion Performance...");
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("insertion_results.csv"))) {
            // Write header
            writer.write("DataSize,CustomHashMapTime,JavaHashMapTime\n");
            
            for (int dataSize : DATA_SIZES) {
                System.out.println("Testing with data size: " + dataSize);
                
                // Generate random strings
                List<String> randomStrings = generateRandomStrings(dataSize);
                
                // Test custom HashMap
                ArnabSimpleHashMap<String, Integer> customMap = new ArnabSimpleHashMap<>();
                long startTime = System.nanoTime();
                
                for (int i = 0; i < randomStrings.size(); i++) {
                    customMap.put(randomStrings.get(i), i);
                }
                
                long customTime = System.nanoTime() - startTime;
                
                // Test Java's HashMap
                HashMap<String, Integer> javaMap = new HashMap<>();
                startTime = System.nanoTime();
                
                for (int i = 0; i < randomStrings.size(); i++) {
                    javaMap.put(randomStrings.get(i), i);
                }
                
                long javaTime = System.nanoTime() - startTime;
                
                // Write results
                writer.write(dataSize + "," + customTime + "," + javaTime + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
    
    /**
     * Test lookup performance
     */
    private static void testLookup() {
        System.out.println("\nTesting Lookup Performance...");
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("lookup_results.csv"))) {
            // Write header
            writer.write("DataSize,CustomHashMapTime,JavaHashMapTime\n");
            
            for (int dataSize : DATA_SIZES) {
                System.out.println("Testing with data size: " + dataSize);
                
                // Generate random strings
                List<String> randomStrings = generateRandomStrings(dataSize);
                
                // Prepare maps
                ArnabSimpleHashMap<String, Integer> customMap = new ArnabSimpleHashMap<>();
                HashMap<String, Integer> javaMap = new HashMap<>();
                
                for (int i = 0; i < randomStrings.size(); i++) {
                    customMap.put(randomStrings.get(i), i);
                    javaMap.put(randomStrings.get(i), i);
                }
                
                // Select a subset of strings for lookup
                List<String> lookupStrings = selectRandomSubset(randomStrings, 
                    Math.min(LOOKUP_OPERATIONS, randomStrings.size()));
                
                // Test custom HashMap
                long startTime = System.nanoTime();
                
                for (String s : lookupStrings) {
                    customMap.get(s);
                }
                
                long customTime = System.nanoTime() - startTime;
                
                // Test Java's HashMap
                startTime = System.nanoTime();
                
                for (String s : lookupStrings) {
                    javaMap.get(s);
                }
                
                long javaTime = System.nanoTime() - startTime;
                
                // Write results
                writer.write(dataSize + "," + customTime + "," + javaTime + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
    
    /**
     * Test bucket distribution
     */
    private static void testBucketDistribution() {
        System.out.println("\nTesting Bucket Distribution...");
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("bucket_distribution.csv"))) {
            // Use the largest data size
            int dataSize = DATA_SIZES[DATA_SIZES.length - 1];
            List<String> randomStrings = generateRandomStrings(dataSize);
            
            ArnabSimpleHashMap<String, Integer> customMap = new ArnabSimpleHashMap<>();
            
            for (int i = 0; i < randomStrings.size(); i++) {
                customMap.put(randomStrings.get(i), i);
            }
            
            // Get bucket sizes
            List<Integer> bucketSizes = customMap.getBucketSizes();
            
            // Write header
            writer.write("BucketIndex,ItemCount\n");
            
            // Write bucket distribution
            for (int i = 0; i < bucketSizes.size(); i++) {
                writer.write(i + "," + bucketSizes.get(i) + "\n");
            }
            
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
    
    /**
     * Test resizing behavior
     */
    private static void testResizing() {
        System.out.println("\nTesting Resizing Behavior...");
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("resizing_behavior.csv"))) {
            // Write header
            writer.write("Operations,Size,Capacity,LoadFactor\n");
            
            // Use the largest data size
            int dataSize = DATA_SIZES[DATA_SIZES.length - 1];
            List<String> randomStrings = generateRandomStrings(dataSize);
            
            ArnabSimpleHashMap<String, Integer> customMap = new ArnabSimpleHashMap<>();
            
            // Track metrics at different points
            for (int i = 0; i < randomStrings.size(); i++) {
                customMap.put(randomStrings.get(i), i);
                
                // Record data at regular intervals or after each resize
                if (i % 1000 == 0 || customMap.capacity() != dataSize) {
                    writer.write(i + "," + customMap.size() + "," + 
                                customMap.capacity() + "," + 
                                String.format("%.4f", customMap.loadFactor()) + "\n");
                }
            }
            
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
    
    /**
     * Generate random strings
     */
    private static List<String> generateRandomStrings(int count) {
        List<String> result = new ArrayList<>(count);
        
        for (int i = 0; i < count; i++) {
            // Generate random length between 5 and RANDOM_STRING_LENGTH
            int length = 5 + RANDOM.nextInt(RANDOM_STRING_LENGTH - 4);
            StringBuilder sb = new StringBuilder(length);
            
            for (int j = 0; j < length; j++) {
                sb.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
            }
            
            result.add(sb.toString());
        }
        
        return result;
    }
    
    /**
     * Select random subset from list
     */
    private static <T> List<T> selectRandomSubset(List<T> list, int count) {
        List<T> shuffled = new ArrayList<>(list);
        Collections.shuffle(shuffled);
        return shuffled.subList(0, count);
    }
}