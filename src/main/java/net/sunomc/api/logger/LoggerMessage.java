package net.sunomc.api.logger;

import java.time.LocalDateTime;

/**
 * Represents a single log message with its metadata.
 * @since 1.0.0
 */
public class LoggerMessage {
    private final LoggerType type;
    private final String text;
    private final LocalDateTime timeStamp;

    /**
     * Creates a new LoggerMessage instance.
     *
     * @param type The log level/type of the message
     * @param text The content of the message
     * @param timeStamp The timestamp when the message was logged
     */
    public LoggerMessage(LoggerType type, String text, LocalDateTime timeStamp) {
        this.type = type;
        this.text = text;
        this.timeStamp = timeStamp;
    }

    /**
     * Gets the log type of this message.
     *
     * @return The LoggerType of this message
     */
    public LoggerType getType() {return type;}

    /**
     * Gets the text content of this message.
     *
     * @return The message text
     */
    public String getText() {return text;}

    /**
     * Gets the timestamp when this message was logged.
     *
     * @return The message timestamp
     */
    public LocalDateTime getTimeStamp() {return timeStamp;}
}