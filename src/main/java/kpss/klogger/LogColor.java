package kpss.klogger;

import java.awt.Color;

/**
 * LogColor is a utility class for creating ANSI escape codes for colored console output.
 * It provides predefined colors and allows creating custom colors using RGB or hexadecimal values.
 * 
 * @author POUSSE Kilian
 */
public class LogColor {
    
    /** ANSI escape code for resetting color */
    public static final LogColor RESET = ansi("\u001B[0m");

    /** ANSI escape code for black text */
    public static final LogColor BLACK = new LogColor(0, 0, 0);

    /** ANSI escape code for red text */
    public static final LogColor RED = new LogColor(255, 0, 0);

    /** ANSI escape code for green text */
    public static final LogColor GREEN = new LogColor(0, 255, 0);

    /** ANSI escape code for blue text */
    public static final LogColor BLUE = new LogColor(0, 0, 255);

    /** ANSI escape code for yellow text */
    public static final LogColor YELLOW = new LogColor(255, 255, 0);

    /** ANSI escape code for magenta text */
    public static final LogColor MAGENTA = new LogColor(255, 0, 255);

    /** ANSI escape code for cyan text */
    public static final LogColor CYAN = new LogColor(0, 255, 255);

    /** ANSI escape code for white text */
    public static final LogColor WHITE = new LogColor(255, 255, 255);

    /** ANSI escape code for gray text */
    public static final LogColor GRAY = new LogColor(128, 128, 128);

    /** ANSI escape code for bold text */
    public static final LogColor BOLD = ansi("\u001B[1m");

    /** ANSI escape code for italic text */
    public static final LogColor ITALIC = ansi("\u001B[3m");

    /** ANSI escape code for underline color */
    public static final LogColor UNDERLINE = ansi("\u001B[4m");

    /** ANSI escape code for strikethrough text */
    public static final LogColor STRIKETHROUGH = ansi("\u001B[9m");

    /**
     * Creates a new LogColor instance with the specified ANSI escape code.
     * @param ansi the ANSI escape code for the color
     * @return a LogColor instance with the specified ANSI escape code
     */
    private static LogColor ansi(String ansi) {
        LogColor color = new LogColor();
        color.ansi = ansi;
        return color;
    }

    /** ANSI escape code for the color */
    private String ansi = "";

    /** RGB color value */
    private Color color = new Color(0, 0, 0);

    /**
     * Private constructor to prevent instantiation without parameters.
     * This constructor is used for static final fields.
     */
    private LogColor() { /* ... */ }

    /**
     * Creates a new LogColor instance with the specified hexadecimal color value.
     * @param hexa the hexadecimal color value (e.g., #FF0000 for red)
     * @throws IllegalArgumentException if the color string is not in the correct format
     */
    public LogColor(String hexa) {
        setColor(hexa);
    }

    /**
     * Creates a new LogColor instance with the specified RGB color values.
     * @param r the red component (0-255)
     * @param g the green component (0-255)
     * @param b the blue component (0-255)
     */
    public LogColor(int r, int g, int b) {
        setColor(r, g, b);
    }

    /**
     * Creates a new LogColor instance with the specified hexadecimal color value.
     * @param hexa the hexadecimal color value (e.g., 0xFF0000 for red)
     */
    public LogColor(int hexa) {
        setColor(hexa);
    }

    /**
     * Creates a new LogColor instance with the specified RGB color value.
     * @param color the Color object representing the color
     */
    public LogColor(Color color) {
        setColor(color);
    }

    /**
     * Returns the ANSI escape code as a string.
     * @return the ANSI escape code
     */
    @Override
    public String toString() {
        return (ansi == null || ansi.isEmpty()) ? RESET.toString() : ansi;
    }

    /**
     * Returns the RGB color value as a Color object.
     * @return the Color object representing the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets the color using the specified RGB color values.
     * @param color the Color object representing the color
     */
    public void setColor(Color color) {
        this.color = color;
        this.ansi = String.format("\u001B[38;2;%d;%d;%dm", color.getRed(), color.getGreen(), color.getBlue());
    }

    /**
     * Sets the color using the specified RGB color values.
     * @param r the red component (0-255)
     * @param g the green component (0-255)
     * @param b the blue component (0-255)
     */
    public void setColor(int r, int g, int b) {
        this.color = new Color(r, g, b);
        this.ansi = String.format("\u001B[38;2;%d;%d;%dm", r, g, b);
    }

    /**
     * Sets the color using the specified hexadecimal color value.
     * @param hexa the hexadecimal color value (e.g., 0xFF0000 for red)
     */
    public void setColor(int hexa) {
        int r = (hexa >> 16) & 0xFF;
        int g = (hexa >> 8) & 0xFF;
        int b = hexa & 0xFF;
        this.color = new Color(r, g, b);
        this.ansi = String.format("\u001B[38;2;%d;%d;%dm", r, g, b);
    }

    /**
     * Sets the color using the specified hexadecimal color value.
     * @param hexa the hexadecimal color value (e.g., #FF0000 for red)
     */
    public void setColor(String hexa) {
        if(hexa.length() != 7 || !hexa.startsWith("#")) {
            throw new IllegalArgumentException("Invalid color format. Expected #RRGGBB.");
        }
        int r = Integer.parseInt(hexa.substring(1, 3), 16);
        int g = Integer.parseInt(hexa.substring(3, 5), 16);
        int b = Integer.parseInt(hexa.substring(5, 7), 16);
        this.color = new Color(r, g, b);
        this.ansi = String.format("\u001B[38;2;%d;%d;%dm", r, g, b);
    }

    /**
     * Returns the hexadecimal color value as a string.
     * @return the hexadecimal color value (e.g., #FF0000 for red)
     */
    public String getHexa() {
        return String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
    }
}
