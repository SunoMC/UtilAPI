package net.sunomc.api.logger;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * A utility class for logging messages with different severity levels to both console and log files.
 * <p>
 * Provides static methods for logging messages with different log levels (INFO, DEBUG, WARNING, ERROR).
 * Logs are written to both the console and log files in the solar/logs directory.
 * @since 1.0.0
 */
public final class Logger {
    private static final CommandSender logger;
    private static final LocalDateTime startTimeStamp;
    private static final List<LoggerMessage> log;

    static {
        logger = Bukkit.getConsoleSender();
        startTimeStamp = LocalDateTime.now();
        log = new ArrayList<>();
    }

    /**
     * Deletes existing log files (latest.log and timestamp-based log file) at startup.
     */
    private static void deleteLogFiles() {
        File latestLog = new File("suno/logs/latest.log");
        File startTimeLog = new File("suno/logs/" + startTimeStamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + ".log");

        try {
            if (latestLog.exists()) {
                latestLog.delete();
            }
            if (startTimeLog.exists()) {
                startTimeLog.delete();
            }
        } catch (Exception e) {
            Logger.warn(e.getMessage());
        }
    }

    /**
     * Logs a message with the specified log level.
     *
     * @param type The log level/type of the message
     * @param message The message to be logged
     */
    public static void log(LoggerType type, String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String logMessage = type.getTypePrefix() + " " + type.getTypeColor() + message + "\033[0m";

        logger.sendMessage(logMessage);

        log.add(new LoggerMessage(type, message, LocalDateTime.now()));

        writeToLogFiles("[" + timestamp + "] : " + removeEscapeSequences(logMessage));
    }

    /**
     * Writes a log message to both latest.log and timestamp-based log file.
     *
     * @param message The message to be written to log files
     */
    private static void writeToLogFiles(String message) {
        try {
            FileWriter latestLogWriter = new FileWriter("suno/logs/latest.log", false);
            PrintWriter latestLogPrinter = new PrintWriter(latestLogWriter);
            latestLogPrinter.println(message);
            latestLogPrinter.close();

            FileWriter startLogWriter = new FileWriter("suno/logs/" + startTimeStamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + ".log", false);
            PrintWriter startLogPrinter = new PrintWriter(startLogWriter);
            startLogPrinter.println(message);
            startLogPrinter.close();
        } catch (IOException e) {
            sendMessage(e.getMessage());
        }
    }

    /**
     * Removes ANSI escape sequences (color codes) from a string.
     *
     * @param message The string containing escape sequences
     * @return The string with escape sequences removed
     */
    private static String removeEscapeSequences(String message) {
        return message.replaceAll("\033\\[[;\\d]*m", "");
    }

    /**
     * Logs an informational message.
     *
     * @param message The informational message to log
     */
    public static void info(String message) { log(LoggerType.INFO, message); }

    /**
     * Logs a debug message.
     *
     * @param message The debug message to log
     */
    public static void debug(String message) { log(LoggerType.DEBUG, message); }

    /**
     * Logs a soft warning message (less severe than regular warning).
     *
     * @param message The warning message to log
     */
    public static void softWarn(String message) { log(LoggerType.SOFT_WARNING, message); }

    /**
     * Logs a warning message.
     *
     * @param message The warning message to log
     */
    public static void warn(String message) { log(LoggerType.WARNING, message); }

    /**
     * Logs an error message.
     *
     * @param message The error message to log
     */
    public static void error(String message) { log(LoggerType.ERROR, message); }

    /**
     * Sends a raw message to the console without logging.
     *
     * @param message The message to send
     */
    public static void sendMessage(String message) {logger.sendMessage(message);}
}