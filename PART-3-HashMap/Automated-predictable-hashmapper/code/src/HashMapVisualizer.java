import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Class to visualize the results of our HashMap experiments
 */
public class HashMapVisualizer {

    /**
     * Create a visualization of collision data
     */
    public static JFrame visualizeCollisions(String csvFile, String title) {
        // Read data from CSV file
        Map<Integer, Map<Integer, Integer>> collisionData = new HashMap<>();
        List<Integer> mapSizes = new ArrayList<>();
        List<Integer> dataSizes = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            // Skip header
            String line = br.readLine();

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int dataSize = Integer.parseInt(values[0]);
                int mapSize = Integer.parseInt(values[1]);
                int collisions = Integer.parseInt(values[2]);

                if (!dataSizes.contains(dataSize)) {
                    dataSizes.add(dataSize);
                }

                if (!mapSizes.contains(mapSize)) {
                    mapSizes.add(mapSize);
                }

                // Store data in nested map
                if (!collisionData.containsKey(dataSize)) {
                    collisionData.put(dataSize, new HashMap<>());
                }
                collisionData.get(dataSize).put(mapSize, collisions);
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
            return null;
        }

        // Create chart
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = getWidth();
                int height = getHeight();
                int padding = 50;

                // Draw axes
                g2d.drawLine(padding, height - padding, width - padding, height - padding); // x-axis
                g2d.drawLine(padding, height - padding, padding, padding); // y-axis

                // Draw x-axis labels
                FontMetrics fm = g2d.getFontMetrics();
                int labelHeight = fm.getHeight();

                for (int i = 0; i < mapSizes.size(); i++) {
                    int mapSize = mapSizes.get(i);
                    String label = String.valueOf(mapSize);
                    int labelWidth = fm.stringWidth(label);

                    float x = padding + i * (width - 2 * padding) / (mapSizes.size() - 1);
                    g2d.drawString(label, x - labelWidth / 2, height - padding / 2);
                }

                // Find max collision value for scaling
                int maxCollisions = 0;
                for (Map<Integer, Integer> dataMap : collisionData.values()) {
                    for (int collisions : dataMap.values()) {
                        maxCollisions = Math.max(maxCollisions, collisions);
                    }
                }

                // Draw lines for each data size
                Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.MAGENTA};

                for (int i = 0; i < dataSizes.size(); i++) {
                    int dataSize = dataSizes.get(i);
                    g2d.setColor(colors[i % colors.length]);

                    int prevX = 0;
                    int prevY = 0;
                    boolean first = true;

                    for (int j = 0; j < mapSizes.size(); j++) {
                        int mapSize = mapSizes.get(j);
                        int collisions = collisionData.get(dataSize).get(mapSize);

                        float x = padding + j * (width - 2 * padding) / (mapSizes.size() - 1);
                        float y = height - padding - (collisions * (height - 2 * padding) / maxCollisions);

                        // Draw circle for data point
                        g2d.fillOval((int) x - 3, (int) y - 3, 6, 6);

                        // Draw line from previous point
                        if (!first) {
                            g2d.drawLine(prevX, prevY, (int) x, (int) y);
                        }

                        prevX = (int) x;
                        prevY = (int) y;
                        first = false;
                    }
                }

                // Draw legend
                int legendX = width - 200;
                int legendY = 50;

                for (int i = 0; i < dataSizes.size(); i++) {
                    g2d.setColor(colors[i % colors.length]);
                    g2d.fillRect(legendX, legendY + i * 20, 10, 10);
                    g2d.setColor(Color.BLACK);
                    g2d.drawString("Data Size: " + dataSizes.get(i), legendX + 20, legendY + i * 20 + 10);
                }

                // Draw titles
                g2d.setFont(new Font("Arial", Font.BOLD, 16));
                String xAxisTitle = "HashMap Size";
                String yAxisTitle = "Number of Collisions";

                // X axis title
                g2d.drawString(xAxisTitle, width / 2 - g2d.getFontMetrics().stringWidth(xAxisTitle) / 2, height - 10);

                // Y axis title - rotated
                g2d.translate(15, height / 2 + g2d.getFontMetrics().stringWidth(yAxisTitle) / 2);
                g2d.rotate(-Math.PI / 2);
                g2d.drawString(yAxisTitle, 0, 0);
                g2d.rotate(Math.PI / 2);
                g2d.translate(-15, -(height / 2 + g2d.getFontMetrics().stringWidth(yAxisTitle) / 2));
            }
        };

        frame.add(chartPanel);
        frame.setVisible(true);
        return frame;
    }

    /**
     * Create a visualization of lookup performance data
     */
    public static JFrame visualizeLookupPerformance(String csvFile) {
        // Read data from CSV file
        Map<Integer, Map<Integer, Double>> lookupData = new HashMap<>();
        List<Integer> mapSizes = new ArrayList<>();
        List<Integer> dataSizes = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            // Skip header
            String line = br.readLine();

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int dataSize = Integer.parseInt(values[0]);
                int mapSize = Integer.parseInt(values[1]);
                double loadFactor = Double.parseDouble(values[2]);
                double lookupTime = Double.parseDouble(values[3]);

                if (!dataSizes.contains(dataSize)) {
                    dataSizes.add(dataSize);
                }

                if (!mapSizes.contains(mapSize)) {
                    mapSizes.add(mapSize);
                }

                // Store data in nested map
                if (!lookupData.containsKey(dataSize)) {
                    lookupData.put(dataSize, new HashMap<>());
                }
                lookupData.get(dataSize).put(mapSize, lookupTime);
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
            return null;
        }

        // Create chart
        JFrame frame = new JFrame("HashMap Lookup Performance");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = getWidth();
                int height = getHeight();
                int padding = 50;

                // Draw axes
                g2d.drawLine(padding, height - padding, width - padding, height - padding); // x-axis
                g2d.drawLine(padding, height - padding, padding, padding); // y-axis

                // Draw x-axis labels
                FontMetrics fm = g2d.getFontMetrics();
                int labelHeight = fm.getHeight();

                for (int i = 0; i < mapSizes.size(); i++) {
                    int mapSize = mapSizes.get(i);
                    String label = String.valueOf(mapSize);
                    int labelWidth = fm.stringWidth(label);

                    float x = padding + i * (width - 2 * padding) / (mapSizes.size() - 1);
                    g2d.drawString(label, x - labelWidth / 2, height - padding / 2);
                }

                // Find max lookup time value for scaling
                double maxLookupTime = 0;
                for (Map<Integer, Double> dataMap : lookupData.values()) {
                    for (double lookupTime : dataMap.values()) {
                        maxLookupTime = Math.max(maxLookupTime, lookupTime);
                    }
                }

                // Draw lines for each data size
                Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.MAGENTA};

                for (int i = 0; i < dataSizes.size(); i++) {
                    int dataSize = dataSizes.get(i);
                    g2d.setColor(colors[i % colors.length]);

                    int prevX = 0;
                    int prevY = 0;
                    boolean first = true;

                    for (int j = 0; j < mapSizes.size(); j++) {
                        int mapSize = mapSizes.get(j);
                        double lookupTime = lookupData.get(dataSize).get(mapSize);

                        float x = padding + j * (width - 2 * padding) / (mapSizes.size() - 1);
                        float y = height - padding - (float) (lookupTime * (height - 2 * padding) / maxLookupTime);

                        // Draw circle for data point
                        g2d.fillOval((int) x - 3, (int) y - 3, 6, 6);

                        // Draw line from previous point
                        if (!first) {
                            g2d.drawLine(prevX, prevY, (int) x, (int) y);
                        }

                        prevX = (int) x;
                        prevY = (int) y;
                        first = false;
                    }
                }

                // Draw legend
                int legendX = width - 200;
                int legendY = 50;

                for (int i = 0; i < dataSizes.size(); i++) {
                    g2d.setColor(colors[i % colors.length]);
                    g2d.fillRect(legendX, legendY + i * 20, 10, 10);
                    g2d.setColor(Color.BLACK);
                    g2d.drawString("Data Size: " + dataSizes.get(i), legendX + 20, legendY + i * 20 + 10);
                }

                // Draw titles
                g2d.setFont(new Font("Arial", Font.BOLD, 16));
                String xAxisTitle = "HashMap Size";
                String yAxisTitle = "Lookup Time (ms)";

                // X axis title
                g2d.drawString(xAxisTitle, width / 2 - g2d.getFontMetrics().stringWidth(xAxisTitle) / 2, height - 10);

                // Y axis title - rotated
                g2d.translate(15, height / 2 + g2d.getFontMetrics().stringWidth(yAxisTitle) / 2);
                g2d.rotate(-Math.PI / 2);
                g2d.drawString(yAxisTitle, 0, 0);
                g2d.rotate(Math.PI / 2);
                g2d.translate(-15, -(height / 2 + g2d.getFontMetrics().stringWidth(yAxisTitle) / 2));
            }
        };

        frame.add(chartPanel);
        frame.setVisible(true);
        return frame;
    }

    /**
     * Create a visualization of bucket distribution
     */
    public static JFrame visualizeBucketDistribution(String csvFile) {
        // Read data from CSV file
        List<Integer> bucketCounts = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            // Skip header
            String line = br.readLine();

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int count = Integer.parseInt(values[1]);
                bucketCounts.add(count);
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
            return null;
        }

        // Create chart
        JFrame frame = new JFrame("HashMap Bucket Distribution");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = getWidth();
                int height = getHeight();
                int padding = 50;

                // Draw axes
                g2d.drawLine(padding, height - padding, width - padding, height - padding); // x-axis
                g2d.drawLine(padding, height - padding, padding, padding); // y-axis

                // Find max bucket count for scaling
                int maxCount = 0;
                for (int count : bucketCounts) {
                    maxCount = Math.max(maxCount, count);
                }

                // Calculate bar width
                int barWidth = (width - 2 * padding) / bucketCounts.size();

                // Draw bars
                g2d.setColor(Color.BLUE);

                for (int i = 0; i < bucketCounts.size(); i++) {
                    int count = bucketCounts.get(i);

                    int x = padding + i * barWidth;
                    int barHeight = (int) ((double) count / maxCount * (height - 2 * padding));

                    g2d.fillRect(x, height - padding - barHeight, barWidth - 2, barHeight);
                }

                // Draw axes labels
                for (int i = 0; i < bucketCounts.size(); i += bucketCounts.size() / 10) {
                    int x = padding + i * barWidth;
                    g2d.drawString(String.valueOf(i), x, height - padding + 15);
                }

                // Y-axis scale
                for (int i = 0; i <= 10; i++) {
                    int y = height - padding - i * (height - 2 * padding) / 10;
                    int value = i * maxCount / 10;
                    g2d.drawString(String.valueOf(value), padding - 30, y + 5);
                    g2d.drawLine(padding - 5, y, padding, y);
                }

                // Draw titles
                g2d.setFont(new Font("Arial", Font.BOLD, 16));
                String xAxisTitle = "Bucket Index";
                String yAxisTitle = "Number of Items";

                // X axis title
                g2d.drawString(xAxisTitle, width / 2 - g2d.getFontMetrics().stringWidth(xAxisTitle) / 2, height - 10);

                // Y axis title - rotated
                g2d.translate(15, height / 2 + g2d.getFontMetrics().stringWidth(yAxisTitle) / 2);
                g2d.rotate(-Math.PI / 2);
                g2d.drawString(yAxisTitle, 0, 0);
                g2d.rotate(Math.PI / 2);
                g2d.translate(-15, -(height / 2 + g2d.getFontMetrics().stringWidth(yAxisTitle) / 2));
            }
        };

        frame.add(chartPanel);
        frame.setVisible(true);
        return frame;
    }

    /**
     * Create a visualization comparing our HashMap with Java's HashMap
     */
    public static JFrame visualizeHashMapComparison(String csvFile) {
        // Read data from CSV file
        List<Integer> dataSizes = new ArrayList<>();
        List<Double> simpleHashMapTimes = new ArrayList<>();
        List<Double> javaHashMapTimes = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            // Skip header
            String line = br.readLine();

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int dataSize = Integer.parseInt(values[0]);
                double simpleTime = Double.parseDouble(values[1]);
                double javaTime = Double.parseDouble(values[2]);

                dataSizes.add(dataSize);
                simpleHashMapTimes.add(simpleTime);
                javaHashMapTimes.add(javaTime);
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
            return null;
        }

        // Create chart
        JFrame frame = new JFrame("HashMap Performance Comparison");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = getWidth();
                int height = getHeight();
                int padding = 50;

                // Draw axes
                g2d.drawLine(padding, height - padding, width - padding, height - padding); // x-axis
                g2d.drawLine(padding, height - padding, padding, padding); // y-axis

                // Find max lookup time value for scaling
                double maxTime = 0;
                for (double time : simpleHashMapTimes) {
                    maxTime = Math.max(maxTime, time);
                }
                for (double time : javaHashMapTimes) {
                    maxTime = Math.max(maxTime, time);
                }

                // Draw data points and lines
                g2d.setColor(Color.RED);
                drawLine(g2d, dataSizes, simpleHashMapTimes, maxTime, width, height, padding);

                g2d.setColor(Color.BLUE);
                drawLine(g2d, dataSizes, javaHashMapTimes, maxTime, width, height, padding);

                // Draw x-axis labels
                for (int i = 0; i < dataSizes.size(); i++) {
                    int dataSize = dataSizes.get(i);
                    String label = String.valueOf(dataSize);
                    int labelWidth = g2d.getFontMetrics().stringWidth(label);

                    float x = padding + i * (width - 2 * padding) / (dataSizes.size() - 1);
                    g2d.setColor(Color.BLACK);
                    g2d.drawString(label, x - labelWidth / 2, height - padding + 15);
                }

                // Draw legend
                g2d.setColor(Color.RED);
                g2d.fillRect(width - 200, 50, 10, 10);
                g2d.setColor(Color.BLACK);
                g2d.drawString("SimpleHashMap", width - 180, 60);

                g2d.setColor(Color.BLUE);
                g2d.fillRect(width - 200, 70, 10, 10);
                g2d.setColor(Color.BLACK);
                g2d.drawString("Java HashMap", width - 180, 80);

                // Draw titles
                g2d.setFont(new Font("Arial", Font.BOLD, 16));
                String xAxisTitle = "Data Size";
                String yAxisTitle = "Lookup Time (ms)";

                // X axis title
                g2d.drawString(xAxisTitle, width / 2 - g2d.getFontMetrics().stringWidth(xAxisTitle) / 2, height - 10);

                // Y axis title - rotated
                g2d.translate(15, height / 2 + g2d.getFontMetrics().stringWidth(yAxisTitle) / 2);
                g2d.rotate(-Math.PI / 2);
                g2d.drawString(yAxisTitle, 0, 0);
                g2d.rotate(Math.PI / 2);
                g2d.translate(-15, -(height / 2 + g2d.getFontMetrics().stringWidth(yAxisTitle) / 2));
            }

            private void drawLine(Graphics2D g2d, List<Integer> xValues, List<Double> yValues,
                                  double maxY, int width, int height, int padding) {
                int prevX = 0;
                int prevY = 0;
                boolean first = true;

                for (int i = 0; i < xValues.size(); i++) {
                    float x = padding + i * (width - 2 * padding) / (xValues.size() - 1);
                    float y = height - padding - (float) (yValues.get(i) * (height - 2 * padding) / maxY);

                    // Draw circle for data point
                    g2d.fillOval((int) x - 3, (int) y - 3, 6, 6);

                    // Draw line from previous point
                    if (!first) {
                        g2d.drawLine(prevX, prevY, (int) x, (int) y);
                    }

                    prevX = (int) x;
                    prevY = (int) y;
                    first = false;
                }
            }
        };

        frame.add(chartPanel);
        frame.setVisible(true);
        return frame;
    }

    /**
     * Visualize hash function comparison data
     */
    public static JFrame visualizeHashFunctionComparison(String csvFile) {
        // Read data from CSV file
        List<String> hashFunctions = new ArrayList<>();
        List<Integer> collisions = new ArrayList<>();
        List<Integer> maxBucketSizes = new ArrayList<>();
        List<Integer> emptyBuckets = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            // Skip header
            String line = br.readLine();

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String hashFunction = values[0];
                int collision = Integer.parseInt(values[1]);
                int maxBucketSize = Integer.parseInt(values[2]);
                int emptyBucket = Integer.parseInt(values[3]);

                hashFunctions.add(hashFunction);
                collisions.add(collision);
                maxBucketSizes.add(maxBucketSize);
                emptyBuckets.add(emptyBucket);
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
            return null;
        }

        // Create chart
        JFrame frame = new JFrame("Hash Function Comparison");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = getWidth();
                int height = getHeight();
                int padding = 70;

                // Draw axes
                g2d.drawLine(padding, height - padding, width - padding, height - padding); // x-axis
                g2d.drawLine(padding, height - padding, padding, padding); // y-axis

                // Find max values for scaling
                int maxCollisions = 0;
                int maxBucketSize = 0;
                int maxEmptyBuckets = 0;

                for (int collision : collisions) {
                    maxCollisions = Math.max(maxCollisions, collision);
                }
                for (int size : maxBucketSizes) {
                    maxBucketSize = Math.max(maxBucketSize, size);
                }
                for (int empty : emptyBuckets) {
                    maxEmptyBuckets = Math.max(maxEmptyBuckets, empty);
                }

                // Calculate bar width and spacing
                int totalFunctions = hashFunctions.size();
                int groupWidth = (width - 2 * padding) / totalFunctions;
                int barWidth = groupWidth / 3 - 4;

                // Draw bars for each hash function
                for (int i = 0; i < totalFunctions; i++) {
                    int x = padding + i * groupWidth;

                    // Draw collision bar (red)
                    int collisionHeight = (int) ((double) collisions.get(i) / maxCollisions * (height - 2 * padding));
                    g2d.setColor(Color.RED);
                    g2d.fillRect(x, height - padding - collisionHeight, barWidth, collisionHeight);

                    // Draw max bucket size bar (blue)
                    int bucketSizeHeight = (int) ((double) maxBucketSizes.get(i) / maxBucketSize * (height - 2 * padding));
                    g2d.setColor(Color.BLUE);
                    g2d.fillRect(x + barWidth + 2, height - padding - bucketSizeHeight, barWidth, bucketSizeHeight);

                    // Draw empty buckets bar (green)
                    int emptyBucketHeight = (int) ((double) emptyBuckets.get(i) / maxEmptyBuckets * (height - 2 * padding));
                    g2d.setColor(Color.GREEN);
                    g2d.fillRect(x + 2 * barWidth + 4, height - padding - emptyBucketHeight, barWidth, emptyBucketHeight);

                    // Draw hash function label
                    g2d.setColor(Color.BLACK);
                    String label = hashFunctions.get(i);
                    int labelWidth = g2d.getFontMetrics().stringWidth(label);

                    // Rotate label for better readability if needed
                    g2d.translate(x + groupWidth / 2, height - padding + 15);
                    g2d.rotate(Math.PI / 4);
                    g2d.drawString(label, -labelWidth / 2, 0);
                    g2d.rotate(-Math.PI / 4);
                    g2d.translate(-(x + groupWidth / 2), -(height - padding + 15));
                }

                // Draw legend
                g2d.setColor(Color.RED);
                g2d.fillRect(width - 180, 40, 10, 10);
                g2d.setColor(Color.BLACK);
                g2d.drawString("Collisions", width - 160, 50);

                g2d.setColor(Color.BLUE);
                g2d.fillRect(width - 180, 60, 10, 10);
                g2d.setColor(Color.BLACK);
                g2d.drawString("Max Bucket Size", width - 160, 70);

                g2d.setColor(Color.GREEN);
                g2d.fillRect(width - 180, 80, 10, 10);
                g2d.setColor(Color.BLACK);
                g2d.drawString("Empty Buckets", width - 160, 90);

                // Draw titles
                g2d.setFont(new Font("Arial", Font.BOLD, 16));
                String xAxisTitle = "Hash Functions";
                String yAxisTitle = "Values (normalized)";

                // X axis title
                g2d.drawString(xAxisTitle, width / 2 - g2d.getFontMetrics().stringWidth(xAxisTitle) / 2, height - 10);

                // Y axis title - rotated
                g2d.translate(15, height / 2 + g2d.getFontMetrics().stringWidth(yAxisTitle) / 2);
                g2d.rotate(-Math.PI / 2);
                g2d.drawString(yAxisTitle, 0, 0);
                g2d.rotate(Math.PI / 2);
                g2d.translate(-15, -(height / 2 + g2d.getFontMetrics().stringWidth(yAxisTitle) / 2));
            }
        };

        frame.add(chartPanel);
        frame.setVisible(true);
        return frame;
    }

    /**
     * Visualize text fingerprint analysis
     */
    public static JFrame visualizeTextFingerprintAnalysis(String csvFile) {
        // Read data from CSV file
        List<String> textTypes = new ArrayList<>();
        List<Integer> collisions = new ArrayList<>();
        List<Integer> maxLevels = new ArrayList<>();
        List<Integer> uniqueWords = new ArrayList<>();
        List<Integer> totalWords = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            // Skip header
            String line = br.readLine();

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String textType = values[0];
                int collision = Integer.parseInt(values[1]);
                int maxLevel = Integer.parseInt(values[2]);
                int uniqueWord = Integer.parseInt(values[3]);
                int totalWord = Integer.parseInt(values[4]);

                textTypes.add(textType);
                collisions.add(collision);
                maxLevels.add(maxLevel);
                uniqueWords.add(uniqueWord);
                totalWords.add(totalWord);
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
            return null;
        }

        // Create chart
        JFrame frame = new JFrame("Text Fingerprint Analysis");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = getWidth();
                int height = getHeight();
                int padding = 70;

                // Draw axes
                g2d.drawLine(padding, height - padding, width - padding, height - padding); // x-axis
                g2d.drawLine(padding, height - padding, padding, padding); // y-axis

                // Find max values for scaling
                int maxCollision = 0;
                int maxLevel = 0;
                int maxUniqueWord = 0;

                for (int collision : collisions) {
                    maxCollision = Math.max(maxCollision, collision);
                }
                for (int level : maxLevels) {
                    maxLevel = Math.max(maxLevel, level);
                }
                for (int uniqueWord : uniqueWords) {
                    maxUniqueWord = Math.max(maxUniqueWord, uniqueWord);
                }

                // Calculate bar width and spacing
                int totalTypes = textTypes.size();
                int groupWidth = (width - 2 * padding) / totalTypes;
                int barWidth = groupWidth / 3 - 4;

                // Draw bars for each text type
                for (int i = 0; i < totalTypes; i++) {
                    int x = padding + i * groupWidth;

                    // Draw collision bar (red)
                    int collisionHeight = (int) ((double) collisions.get(i) / maxCollision * (height - 2 * padding));
                    g2d.setColor(Color.RED);
                    g2d.fillRect(x, height - padding - collisionHeight, barWidth, collisionHeight);

                    // Draw max collision level bar (blue)
                    int levelHeight = (int) ((double) maxLevels.get(i) / maxLevel * (height - 2 * padding));
                    g2d.setColor(Color.BLUE);
                    g2d.fillRect(x + barWidth + 2, height - padding - levelHeight, barWidth, levelHeight);

                    // Draw uniqueWords/totalWords ratio bar (green)
                    double ratio = (double) uniqueWords.get(i) / totalWords.get(i);
                    int ratioHeight = (int) (ratio * (height - 2 * padding));
                    g2d.setColor(Color.GREEN);
                    g2d.fillRect(x + 2 * barWidth + 4, height - padding - ratioHeight, barWidth, ratioHeight);

                    // Draw text type label
                    g2d.setColor(Color.BLACK);
                    String label = textTypes.get(i);

                    g2d.drawString(label, x + groupWidth / 2 - g2d.getFontMetrics().stringWidth(label) / 2, height - padding + 15);
                }

                // Draw legend
                g2d.setColor(Color.RED);
                g2d.fillRect(width - 180, 40, 10, 10);
                g2d.setColor(Color.BLACK);
                g2d.drawString("Collisions", width - 160, 50);

                g2d.setColor(Color.BLUE);
                g2d.fillRect(width - 180, 60, 10, 10);
                g2d.setColor(Color.BLACK);
                g2d.drawString("Max Collision Level", width - 160, 70);

                g2d.setColor(Color.GREEN);
                g2d.fillRect(width - 180, 80, 10, 10);
                g2d.setColor(Color.BLACK);
                g2d.drawString("Unique/Total Ratio", width - 160, 90);

                // Draw titles
                g2d.setFont(new Font("Arial", Font.BOLD, 16));
                String xAxisTitle = "Text Types";
                String yAxisTitle = "Values (normalized)";

                // X axis title
                g2d.drawString(xAxisTitle, width / 2 - g2d.getFontMetrics().stringWidth(xAxisTitle) / 2, height - 10);

                // Y axis title - rotated
                g2d.translate(15, height / 2 + g2d.getFontMetrics().stringWidth(yAxisTitle) / 2);
                g2d.rotate(-Math.PI / 2);
                g2d.drawString(yAxisTitle, 0, 0);
                g2d.rotate(Math.PI / 2);
                g2d.translate(-15, -(height / 2 + g2d.getFontMetrics().stringWidth(yAxisTitle) / 2));
            }
        };

        frame.add(chartPanel);
        frame.setVisible(true);
        return frame;
    }

    /**
     * Create a visual fingerprint from text (standalone method)
     */
    public static BufferedImage createTextFingerprint(String text, int size) {
        return HashMapper.TextVisualizer.createVisualFingerprint(text, size);
    }

    /**
     * Enhance a fingerprint with salt and smooth (standalone method)
     */
    public static BufferedImage enhanceFingerprint(BufferedImage original, double saltLevel, int smoothRadius) {
        return HashMapper.TextVisualizer.saltAndSmooth(original, saltLevel, smoothRadius);
    }

    public static void main(String[] args) {
        visualizeCollisions("string_collisions.csv", "String Key Collisions");
        visualizeCollisions("integer_collisions.csv", "Integer Key Collisions");
        visualizeLookupPerformance("lookup_performance.csv");
        visualizeBucketDistribution("bucket_distribution.csv");
        visualizeHashMapComparison("hashmap_comparison.csv");
        visualizeHashFunctionComparison("hash_function_comparison.csv");
        visualizeTextFingerprintAnalysis("text_fingerprint_analysis.csv");
    }
}