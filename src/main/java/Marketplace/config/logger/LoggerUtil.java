package Marketplace.config.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

@Component
public class LoggerUtil {
    private static final Logger logger = LoggerFactory.getLogger(LoggerUtil.class);

    public static void logINFO(String message) {
        logger.info(message);
    }

    public static void logERROR(String message, Throwable throwable) {
        logger.error(message, throwable);
    }

    public static void logWARNING(String message){
        logger.warn(message);
    }
}
