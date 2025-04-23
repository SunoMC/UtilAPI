package net.sunomc.api.logger;

/**
 * Enum representing different log message types with their associated formatting.
 * @since 1.0.0
 */
public enum LoggerType {
    /** Error messages (highest severity) */
    ERROR("\033[31m[ERROR]\033[0m", "\033[31m", 4),

    /** Warning messages (high severity) */
    WARNING("\033[33m[WARNING]\033[0m", "\033[33m", 3),

    /** Soft warning messages (medium severity) */
    SOFT_WARNING("\033[33m[WARNING]\033[0m", "\033[0m", 2),

    /** Debug messages (low severity) */
    DEBUG("\033[36m[DEBUG]\033[0m", "\033[90m", 1),

    /** Informational messages (lowest severity) */
    INFO("\033[0m[SUNO]\033[0m", "\033[90m", 0);

    private final String prefix;
    private final String color;
    private final int level;

    /**
     * Creates a new LoggerType enum value.
     *
     * @param prefix The prefix shown before messages of this type
     * @param color The ANSI color code for this message type
     * @param level The severity level (higher = more severe)
     */
    LoggerType(String prefix, String color, int level) {
        this.prefix = prefix;
        this.color = color;
        this.level = level;
    }

    /**
     * Gets the formatted prefix for this log type.
     *
     * @return The type prefix string
     */
    public String getTypePrefix() {return prefix;}

    /**
     * Gets the ANSI color code for this log type.
     *
     * @return The color code string
     */
    public String getTypeColor() {return color;}

    /**
     * Gets the severity level of this log type.
     *
     * @return The severity level (higher = more severe)
     */
    public int getTypeLevel() {return level;}
}