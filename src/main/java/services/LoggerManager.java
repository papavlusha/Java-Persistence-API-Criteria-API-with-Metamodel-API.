package services;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
public class LoggerManager {

    private static final Logger logger = LogManager.getLogger(LoggerManager.class);

    public static void logException(Exception exception) {
        logger.error(exception, exception.getCause());
    }
}
