public class SimpleHashMapTester {
    
    public static void main(String[] args) {
        // Create a new hash map
        SimpleHashMap<String, String> map = new SimpleHashMap<>();
        
        // Add a bunch of strings
        map.put("apple", "fruit");
        map.put("carrot", "vegetable");
        map.put("orange", "fruit");
        map.put("banana", "fruit");
        map.put("broccoli", "vegetable");
        
        // Test get method
        System.out.println("Value for 'apple': " + map.get("apple"));
        System.out.println("Value for 'carrot': " + map.get("carrot"));
        
        // Test if map contains certain values
        System.out.println("Contains 'fruit': " + map.containsValue("fruit"));
        System.out.println("Contains 'meat': " + map.containsValue("meat"));
        
        // Test resize method
        System.out.println("\nResizing the map...");
        map.resize();
        
        // Verify values are still accessible after resize
        System.out.println("After resize - Value for 'apple': " + map.get("apple"));
        System.out.println("After resize - Value for 'carrot': " + map.get("carrot"));
        System.out.println("After resize - Contains 'fruit': " + map.containsValue("fruit"));
    }
}