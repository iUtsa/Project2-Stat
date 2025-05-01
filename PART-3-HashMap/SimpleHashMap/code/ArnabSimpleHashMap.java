import java.util.LinkedList;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple HashMap implementation with a naive hashing function.
 * This implementation uses an array of LinkedLists to handle collisions.
 * @param <K> The type of keys in this map
 * @param <V> The type of values in this map
 */
public class ArnabSimpleHashMap<K, V> {
    
    // Data structure: Array of LinkedLists
    private LinkedList<Entry<K, V>>[] data;
    private int size; // Number of key-value pairs in the map
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR_THRESHOLD = 0.75;
    
    /**
     * Internal class to store key-value pairs
     */
    private static class Entry<K, V> {
        K key;
        V value;
        
        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
    
    /**
     * Constructor that initializes with default capacity
     */
    @SuppressWarnings("unchecked")
    public ArnabSimpleHashMap() {
        data = new LinkedList[DEFAULT_CAPACITY];
        size = 0;
        
        // Initialize each bucket with an empty LinkedList
        for (int i = 0; i < data.length; i++) {
            data[i] = new LinkedList<>();
        }
    }
    
    /**
     * Constructor that initializes with specified capacity
     */
    @SuppressWarnings("unchecked")
    public ArnabSimpleHashMap(int capacity) {
        data = new LinkedList[capacity];
        size = 0;
        
        // Initialize each bucket with an empty LinkedList
        for (int i = 0; i < data.length; i++) {
            data[i] = new LinkedList<>();
        }
    }
    
    /**
     * Dumb hash function that counts the number of letters in a string
     * For non-string keys, we use the hashCode() and take absolute value
     */
    private int dumbHash(K key) {
        if (key == null) {
            return 0;
        }
        
        if (key instanceof String) {
            String str = (String) key;
            return str.length() % data.length;
        } else {
            // For non-string keys, use their hashCode
            return Math.abs(key.hashCode() % data.length);
        }
    }
    
    /**
     * Puts a key-value pair into the map
     */
    public void put(K key, V value) {
        // Check if we need to resize
        if ((double) size / data.length >= LOAD_FACTOR_THRESHOLD) {
            resize();
        }
        
        int index = dumbHash(key);
        LinkedList<Entry<K, V>> bucket = data[index];
        
        // Check if key already exists
        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                entry.value = value; // Update the value
                return;
            }
        }
        
        // Key doesn't exist, add a new entry
        bucket.add(new Entry<>(key, value));
        size++;
    }
    
    /**
     * Gets a value by key
     */
    public V get(K key) {
        int index = dumbHash(key);
        LinkedList<Entry<K, V>> bucket = data[index];
        
        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }
        
        return null; // Key not found
    }
    
    /**
     * Checks if the map contains a key
     */
    public boolean containsKey(K key) {
        int index = dumbHash(key);
        LinkedList<Entry<K, V>> bucket = data[index];
        
        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Checks if the map contains a value
     */
    public boolean containsValue(V value) {
        for (LinkedList<Entry<K, V>> bucket : data) {
            for (Entry<K, V> entry : bucket) {
                if (entry.value == null) {
                    if (value == null) {
                        return true;
                    }
                } else if (entry.value.equals(value)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Removes a key-value pair by key
     */
    public V remove(K key) {
        int index = dumbHash(key);
        LinkedList<Entry<K, V>> bucket = data[index];
        
        for (int i = 0; i < bucket.size(); i++) {
            Entry<K, V> entry = bucket.get(i);
            if (entry.key.equals(key)) {
                bucket.remove(i);
                size--;
                return entry.value;
            }
        }
        
        return null; // Key not found
    }
    
    /**
     * Dynamically resizes the array when load factor threshold is exceeded
     */
    @SuppressWarnings("unchecked")
    public void resize() {
        int newCapacity = data.length * 2;
        LinkedList<Entry<K, V>>[] oldData = data;
        
        // Create new array with double capacity
        data = new LinkedList[newCapacity];
        for (int i = 0; i < data.length; i++) {
            data[i] = new LinkedList<>();
        }
        
        size = 0; // Reset size as we'll reinsert all entries
        
        // Reinsert all entries
        for (LinkedList<Entry<K, V>> bucket : oldData) {
            for (Entry<K, V> entry : bucket) {
                put(entry.key, entry.value);
            }
        }
    }
    
    /**
     * Returns the number of key-value pairs in the map
     */
    public int size() {
        return size;
    }
    
    /**
     * Returns the current capacity of the map
     */
    public int capacity() {
        return data.length;
    }
    
    /**
     * Returns the current load factor of the map
     */
    public double loadFactor() {
        return (double) size / data.length;
    }
    
    /**
     * Returns the distribution of entries across buckets
     */
    public List<Integer> getBucketSizes() {
        List<Integer> bucketSizes = new ArrayList<>();
        for (LinkedList<Entry<K, V>> bucket : data) {
            bucketSizes.add(bucket.size());
        }
        return bucketSizes;
    }
}