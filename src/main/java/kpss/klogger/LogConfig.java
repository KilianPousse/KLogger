package kpss.klogger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * LogConfig is a configuration class for the KLogger library.
 * It allows setting up the logging format, colors, and file output options.
 * 
 * @author POUSSE Kilian
 */
class LogConfig {

    /** Debug mode flag */
    private boolean debugMode = false;

    /** Append mode flag */
    private boolean appendMode = true;

    /** Log file path */
    private String logFilePath = null;

    /** Log file writer */
    private FileWriter logFile = null;

    /** Map of log types to their corresponding colors */
    private Map<String, LogColor> colors = new HashMap<>();

    /** Console log format */
    private String consoleFormat = "[{type}][{context}]: {message}";

    /** File log format */
    private String fileFormat = "{date} [{type}][{context}]: {message}";

    /** Date format for log entries */
    private String dateFormat = "yyyy-MM-dd HH:mm:ss";

    /**
     * Default constructor for LogConfig.
     * Initializes the default colors for log types.
     */
    LogConfig() { 
        colors.put("INFO", new LogColor(0, 170, 255));    // #00AAFF
        colors.put("DEBUG", new LogColor(170, 170, 170)); // #AAAAAA
        colors.put("WARNING", LogColor.YELLOW);                 // #FFFF00
        colors.put("ERROR", new LogColor(255, 33, 33));   // #FF2222
        colors.put("CRITICAL", LogColor.MAGENTA);               // #FF00FF
        colors.put("context", LogColor.GRAY);                   // #AAAAAA
        colors.put("date", new LogColor(0, 170, 0));      // #00AA00
        colors.put("message", LogColor.RESET);                  // #FFFFFF
    }

    /**
     * Sets the log file path.
     * @return the log file path
     */
    public boolean isDebugMode() {
        return debugMode;
    }

    /**
     * Sets the debug mode.
     * @param debugMode true to enable debug mode, false to disable it
     */
    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    /**
     * Sets the append mode for the log file.
     * @return true if append mode is enabled, false otherwise
     */
    public boolean isAppendMode() {
        return appendMode;
    }

    /**
     * Sets the append mode for the log file.
     * @param appendMode true to enable append mode, false to disable it
     */
    public void setAppendMode(boolean appendMode) {
        this.appendMode = appendMode;
        closeLogFile();
        try {
            openLogFile(logFilePath);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the log file path.
     * @param logFilePath the path to the log file
     */
    public void openLogFile(String path) throws IOException {
        logFilePath = path;
        logFile = new FileWriter(logFilePath, appendMode);
    }

    /**
     * Sets the log file path.
     * @return the log file path
     */
    public void closeLogFile() {
        if(logFile != null) {
            try {
                logFile.close();
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Writes a log entry to the log file.
     * @param data the log entry data
     */
    public void writeToFile(Map<String, String> data) {
        if(logFile != null) {
            String text = format(fileFormat, data, false);
            try {
                logFile.write(text + "\n");
                logFile.flush();
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Writes a log entry to the console.
     * @param data the log entry data
     */
    public void writeToConsole(Map<String, String> data) {
        String text = format(consoleFormat, data, true);
        System.out.println(text + LogColor.RESET);
    }

    /**
     * Formats a log entry using the specified template and values.
     * @param template the log entry template
     * @param values the values to replace in the template
     * @param withColor true to enable color formatting, false otherwise
     * @return the formatted log entry
     */
    private String format(String template, Map<String, String> values, boolean withColor) {
        for(Map.Entry<String, String> entry: values.entrySet()) {
            String value = entry.getValue();
            if(withColor) {
                LogColor color = LogColor.RESET;
                if(colors.containsKey(entry.getKey())) {
                    color = colors.get(entry.getKey());
                }
                else if(entry.getKey().equals("type")) {
                    color = colors.getOrDefault(value, LogColor.RESET);
                }
                value = color + value + LogColor.RESET;
            }
            template = template.replace("{" + entry.getKey() + "}", value);
        }
        return template;
    }

    /**
     * Gets Date format.
     * @return the date format
     */
    public String getDateFormat() {
        return dateFormat;
    }

    /**
     * Initializes the logger configuration from an open InputStream (e.g. a JSON file stream).
     * @param jsonStream the InputStream containing the JSON configuration
     * @throws IOException if an error occurs while reading the stream
     */
    public void init(InputStream json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ConfigJson configJson = objectMapper.readValue(json, ConfigJson.class);

        if (configJson.formats != null) {
            this.dateFormat = configJson.formats.getOrDefault("date", dateFormat);
            this.consoleFormat = configJson.formats.getOrDefault("console", consoleFormat);
            this.fileFormat = configJson.formats.getOrDefault("file", fileFormat);
        }

        this.debugMode = configJson.debug;
        this.appendMode = configJson.append;
        configJson.getColors(this.colors);

        if(configJson.file != null) openLogFile(configJson.file);

        KLog.log("Logger configuration loaded from JSON file: " + logFilePath);
        if(debugMode) {
            KLog.debug("Debug mode: " + debugMode);
            KLog.debug("Append mode: " + appendMode);
            KLog.debug("Console format: \"" + consoleFormat + "\"");
            KLog.debug("File format: \"" + fileFormat + "\"");
            KLog.debug("Date format: \"" + dateFormat + "\"");
            KLog.debug("Log file path: \"" + logFilePath + "\"");
            String colorsString = "";
            for(Map.Entry<String, LogColor> entry: colors.entrySet()) {
                colorsString += entry.getKey() + "='" + entry.getValue().getHexa() + "', ";
            }
            KLog.debug("Colors: {" + colorsString.substring(0, colorsString.length() - 2) + "}");
        }
    }



    /**
     * ConfigJson is a nested class representing the JSON configuration structure.
     * It contains fields for colors, formats, debug mode, and append mode.
     * The colors and formats are stored as Maps, while debug and append modes are booleans.
     */
    private static class ConfigJson {

        /** Map of colors for log types */
        public Map<String, String> colors;

        /** Map of formats for log types */
        public Map<String, String> formats;

        /** Debug mode flag */
        public boolean debug;

        /** Append mode flag */
        public boolean append;

        public String file;

        /**
         * Gets the colors from the JSON configuration.
         * @return a HashMap of colors for log types
         */
        public void getColors(Map<String, LogColor> defaultColors) {
            if(colors != null) {
                for(Map.Entry<String, String> entry: colors.entrySet()) {
                    defaultColors.put(entry.getKey(), new LogColor(entry.getValue()));
                }
            }
        }
    }
}