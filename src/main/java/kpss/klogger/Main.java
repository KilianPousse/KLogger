package kpss.klogger;

/**
 * Main class for testing the KLogger library.
 */
public class Main {
    
    /**
     * Main method to test the KLogger library.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        KLog.setDebugMode(true);

        KLog.log("Hello, World!");
        KLog.debug("This is a debug message.");
        KLog.warning("This is a warning message.");
        KLog.error("This is an error message.");
        KLog.error("This is an exception message.", new Exception("Test exception"));
        KLog.error(new Exception("Test exception"));

        
        
    }

}
