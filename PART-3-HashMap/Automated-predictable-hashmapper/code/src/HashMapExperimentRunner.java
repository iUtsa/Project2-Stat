/**
 * Main class to run HashMap experiments and visualization
 */
public class HashMapExperimentRunner {
    public static void main(String[] args) {
        System.out.println("Starting HashMap Experiment...");

        try {
            // Run all experiments
            System.out.println("Running experiments...");
            HashMapExperiment.main(args);

            // Run visualizations
            System.out.println("Generating visualizations...");
            HashMapVisualizer.main(args);

            System.out.println("Experiment completed successfully!");
        } catch (Exception e) {
            System.err.println("Error during experiment: " + e.getMessage());
            e.printStackTrace();
        }
    }
}