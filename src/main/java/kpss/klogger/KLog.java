package kpss.klogger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * KLog is a simple logging utility for Java applications.
 * It provides methods to initialize the logger, set the log file path, and enable or disable debug mode.
 * It also allows logging messages with different severity levels (INFO, DEBUG, WARNING, ERROR, CRITICAL).
 * 
 * @author POUSSE Kilian
 */
public class KLog {
    
    /** The default log file path. */
    private static LogConfig config = new LogConfig();

    /**
     * Sets the debug mode for the logger.
     * @param b true to enable debug mode, false to disable it.
     */
    public static void setDebugMode(boolean b) {
        config.setDebugMode(b);
    }

    /**
     * Checks if the logger is in debug mode.
     * @return true if debug mode is enabled, false otherwise.
     */
    public static boolean isDebugMode() {
        return config.isDebugMode();
    }

    /**
     * Sets the append mode for the logger.
     * @param b true to enable append mode, false to disable it.
     */
    public static void setAppendMode(boolean b) {
        config.setAppendMode(b);
    }

    /**
     * Checks if the logger is in append mode.
     * @return true if append mode is enabled, false otherwise.
     */
    public static boolean isAppendMode() {
        return config.isAppendMode();
    }

    /**
     * Sets the log file path for the logger.
     * @param path the path to the log file.
     */
    public static void setOutPut(String path) {
        try {
            config.openLogFile(path);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the log file path for the logger and enables append mode.
     * @param path the path to the log file.
     * @throws IOException if an I/O error occurs while opening the log file.
     */
    public static void setConfig(String path) throws IOException {
        config.init(new FileInputStream(path));
    }

    /**
     * Sets the log file path for the logger and enables append mode.
     * @param input the InputStream for the configuration file.
     * @throws IOException if an I/O error occurs while opening the log file.
     */
    public static void setConfig(InputStream input) throws IOException {
        config.init(input);
    }

    /**
     * Writes a log message to the console and the log file.
     * @param type the type of log message (INFO, DEBUG, WARNING, ERROR, CRITICAL).
     * @param message the log message.
     */
    private static void write(String type, String message) {
        String context = StackWalker.getInstance()
                .walk(frames -> frames.skip(2).findFirst().get().getClassName());
        context += "." + StackWalker.getInstance()
                .walk(frames -> frames.skip(2).findFirst().get().getMethodName());

        Map<String, String> data = new HashMap<>();
        data.put("type", type);
        data.put("context", context);
        data.put("message", message);
        data.put("date", LocalDateTime.now().format(DateTimeFormatter.ofPattern(config.getDateFormat())));

        config.writeToConsole(data);
        config.writeToFile(data);
    }

    /**
     * Logs an informational message.
     * @param msg the message to log.
     */
    public static void log(String msg) {
        write("INFO", msg);
    }

    /**
     * Logs a debug message if debug mode is enabled.
     * @param msg the message to log.
     */
    public static void debug(String msg) {
        if(isDebugMode()) {
            write("DEBUG", msg);
        }
    }

    /**
     * Logs a warning message.
     * @param msg the message to log.
     */
    public static void warning(String msg) {
        write("WARNING", msg);
    }

    /**
     * Logs an error message.
     * @param msg the message to log.
     */
    public static void error(String msg) {
        write("ERROR", msg);
    }

    /**
     * Logs an error message with an exception.
     * @param msg the message to log.
     * @param e the exception associated with the error.
     */
    public static void error(String msg, Exception e) {
        write("ERROR", msg + exceptionToStr(e));
    }

    /**
     * Logs an error message with an exception.
     * @param e the exception associated with the error.
     */
    public static void error(Exception e) {
        write("ERROR", "An exception was caught" + exceptionToStr(e));
    }

    /**
     * Logs a critical error message and terminates the program.
     * @param msg the message to log.
     */
    public static void critical(String msg) {
        int code = 1;
        write("CRITICAL", msg + criticalCode(code));
        System.exit(code);
    }

    /**
     * Logs a critical error message with an exit code and terminates the program.
     * @param msg the message to log.
     * @param code the exit code for the program termination.
     */
    public static void critical(String msg, int code) {
        write("CRITICAL", msg + criticalCode(code));
        System.exit(code);
    }

    /**
     * Logs a critical error message with an exception and terminates the program.
     * @param msg the message to log.
     * @param e the exception associated with the critical error.
     */
    public static void critical(String msg, Exception e) {
        int code = e.hashCode();
        write("CRITICAL", msg + exceptionToStr(e) + criticalCode(code));
        System.exit(code);
    }

    /**
     * Logs a critical error message with an exception and terminates the program.
     * @param e the exception associated with the critical error.
     */
    public static void critical(Exception e) {
        int code = e.hashCode();
        write("CRITICAL", "An exception was caught" + exceptionToStr(e) + criticalCode(code));
        System.exit(code);
    }

    /**
     * Converts an exception to a string representation.
     * @param e the exception to convert.
     * @return the string representation of the exception.
     */
    private static String exceptionToStr(Exception e) {
        StringBuilder str = new StringBuilder("\n    --> " + e.getClass().getSimpleName() + ": " + e.getMessage());
        int i = e.getStackTrace().length;
        for(StackTraceElement element: e.getStackTrace()) {
            String branch =  i > 1 ? "├──" : "└──";
            str.append("\n        " + branch +" ").append(element.toString());
            i--;
        }
        return str.toString();
    }

    /**
     * Generates a critical error code message.
     * @param code the error code.
     * @return the string representation of the critical error code.
     */
    private static String criticalCode(int code) {
        StringBuilder str = new StringBuilder("\n    --> Critical error code: " + code);
        return str.toString();
    }

    /**
     * Private constructor to prevent instantiation of the KLog class.
     */
    private KLog() { /* ... */ }
}
