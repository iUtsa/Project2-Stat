import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * Visualizer for HashMap performance metrics
 * Uses JFreeChart to create visualizations
 */
public class HashMapVisualizer {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }
    
    private static void createAndShowGUI() {
        JFrame frame = new JFrame("ArnabSimpleHashMap Performance Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        
        // Create tabs for different charts
        javax.swing.JTabbedPane tabbedPane = new javax.swing.JTabbedPane();
        
        // Add insertion performance chart
        try {
            tabbedPane.addTab("Insertion Performance", createInsertionChart());
            tabbedPane.addTab("Lookup Performance", createLookupChart());
            tabbedPane.addTab("Bucket Distribution", createBucketDistributionChart());
            tabbedPane.addTab("Resizing Behavior", createResizingChart());
        } catch (IOException e) {
            System.err.println("Error creating charts: " + e.getMessage());
        }
        
        frame.getContentPane().add(tabbedPane);
        frame.setVisible(true);
    }
    
    private static ChartPanel createInsertionChart() throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        try (BufferedReader reader = new BufferedReader(new FileReader("insertion_results.csv"))) {
            // Skip header
            reader.readLine();
            
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                int dataSize = Integer.parseInt(values[0]);
                double customTime = Double.parseDouble(values[1]) / 1_000_000.0; // Convert to ms
                double javaTime = Double.parseDouble(values[2]) / 1_000_000.0; // Convert to ms
                
                dataset.addValue(customTime, "ArnabSimpleHashMap", String.valueOf(dataSize));
                dataset.addValue(javaTime, "Java HashMap", String.valueOf(dataSize));
            }
        }
        
        JFreeChart chart = ChartFactory.createBarChart(
            "Insertion Time Comparison",
            "Data Size",
            "Time (ms)",
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
        
        return new ChartPanel(chart);
    }
    
    private static ChartPanel createLookupChart() throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        try (BufferedReader reader = new BufferedReader(new FileReader("lookup_results.csv"))) {
            // Skip header
            reader.readLine();
            
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                int dataSize = Integer.parseInt(values[0]);
                double customTime = Double.parseDouble(values[1]) / 1_000_000.0; // Convert to ms
                double javaTime = Double.parseDouble(values[2]) / 1_000_000.0; // Convert to ms
                
                dataset.addValue(customTime, "ArnabSimpleHashMap", String.valueOf(dataSize));
                dataset.addValue(javaTime, "Java HashMap", String.valueOf(dataSize));
            }
        }
        
        JFreeChart chart = ChartFactory.createBarChart(
            "Lookup Time Comparison",
            "Data Size",
            "Time (ms)",
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
        
        return new ChartPanel(chart);
    }
    
    private static ChartPanel createBucketDistributionChart() throws IOException {
        XYSeries series = new XYSeries("Items per Bucket");
        
        try (BufferedReader reader = new BufferedReader(new FileReader("bucket_distribution.csv"))) {
            // Skip header
            reader.readLine();
            
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                int bucketIndex = Integer.parseInt(values[0]);
                int itemCount = Integer.parseInt(values[1]);
                
                series.add(bucketIndex, itemCount);
            }
        }
        
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        
        JFreeChart chart = ChartFactory.createXYLineChart(
            "Bucket Distribution",
            "Bucket Index",
            "Number of Items",
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
        
        return new ChartPanel(chart);
    }
    
    private static ChartPanel createResizingChart() throws IOException {
        XYSeries capacitySeries = new XYSeries("Capacity");
        XYSeries sizeSeries = new XYSeries("Size");
        XYSeries loadFactorSeries = new XYSeries("Load Factor");
        
        try (BufferedReader reader = new BufferedReader(new FileReader("resizing_behavior.csv"))) {
            // Skip header
            reader.readLine();
            
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                int operations = Integer.parseInt(values[0]);
                int size = Integer.parseInt(values[1]);
                int capacity = Integer.parseInt(values[2]);
                double loadFactor = Double.parseDouble(values[3]);
                
                capacitySeries.add(operations, capacity);
                sizeSeries.add(operations, size);
                loadFactorSeries.add(operations, loadFactor);
            }
        }
        
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(capacitySeries);
        dataset.addSeries(sizeSeries);
        
        JFreeChart chart = ChartFactory.createXYLineChart(
            "Resizing Behavior",
            "Operations",
            "Value",
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
        
        // Create a separate chart for load factor (different scale)
        XYSeriesCollection loadFactorDataset = new XYSeriesCollection();
        loadFactorDataset.addSeries(loadFactorSeries);
        
        JFreeChart loadFactorChart = ChartFactory.createXYLineChart(
            "Load Factor Behavior",
            "Operations",
            "Load Factor",
            loadFactorDataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
        
        // Create a panel with both charts
        javax.swing.JPanel panel = new javax.swing.JPanel(new java.awt.GridLayout(2, 1));
        panel.add(new ChartPanel(chart));
        panel.add(new ChartPanel(loadFactorChart));
        
        return new ChartPanel(chart);
    }
}