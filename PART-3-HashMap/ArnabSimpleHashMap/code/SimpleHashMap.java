import java.util.LinkedList;

/**
 * A simple hash map implementation.
 */
public class SimpleHashMap<K, V> {
    
    // Global variable - array of linked lists
    LinkedList<Entry<K, V>>[] data;
    
    // Entry class for the linked list
    private class Entry<K, V> {
        K key;
        V value;
        
        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
    
    /**
     * Constructor to initialize the hash map.
     */
    @SuppressWarnings("unchecked")
    public SimpleHashMap() {
        // Initialize with size 10
        data = (LinkedList<Entry<K, V>>[]) new LinkedList[10];
        
        // Initialize each linked list
        for (int i = 0; i < data.length; i++) {
            data[i] = new LinkedList<>();
        }
    }
    
    /**
     * A simple hash function that counts the number of characters in a string.
     * @param key The string to hash
     * @return The hash value
     */
    public int dumbHash(String key) {
        return key.length() % data.length;
    }
    
    /**
     * Puts a key-value pair in the map.
     * @param key The key
     * @param value The value
     */
    public void put(K key, V value) {
        int index = 0;
        if (key instanceof String) {
            index = dumbHash((String) key);
        } else {
            index = Math.abs(key.hashCode() % data.length);
        }
        
        LinkedList<Entry<K, V>> bucket = data[index];
        
        // Check if key already exists
        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                entry.value = value;
                return;
            }
        }
        
        // Add new entry
        bucket.add(new Entry<>(key, value));
    }
    
    /**
     * Gets a value by key.
     * @param key The key
     * @return The value
     */
    public V get(K key) {
        int index = 0;
        if (key instanceof String) {
            index = dumbHash((String) key);
        } else {
            index = Math.abs(key.hashCode() % data.length);
        }
        
        LinkedList<Entry<K, V>> bucket = data[index];
        
        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }
        
        return null;
    }
    
    /**
     * Checks if the map contains a value.
     * @param value The value to check
     * @return True if the value exists, false otherwise
     */
    public boolean containsValue(V value) {
        for (LinkedList<Entry<K, V>> bucket : data) {
            for (Entry<K, V> entry : bucket) {
                if ((value == null && entry.value == null) || 
                    (value != null && value.equals(entry.value))) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Resizes the map to twice its current capacity.
     */
    @SuppressWarnings("unchecked")
    public void resize() {
        // Save old data
        LinkedList<Entry<K, V>>[] oldData = data;
        
        // Create new array with double size
        data = (LinkedList<Entry<K, V>>[]) new LinkedList[oldData.length * 2];
        
        // Initialize new linked lists
        for (int i = 0; i < data.length; i++) {
            data[i] = new LinkedList<>();
        }
        
        // Rehash all entries
        for (LinkedList<Entry<K, V>> bucket : oldData) {
            for (Entry<K, V> entry : bucket) {
                put(entry.key, entry.value);
            }
        }
    }
}