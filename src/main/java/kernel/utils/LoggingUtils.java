package kernel.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;

public final class LoggingUtils {
    private LoggingUtils() {}
    
    // Factory methods
    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }
    
    // Configuration methods
    public static void enableTracing() {
        setGlobalLevel(Level.TRACE);
    }
    
    public static void disableLogging() {
        setGlobalLevel(Level.OFF);
    }
    
    public static boolean isTracingEnabled() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        return context.getLogger("ROOT").getLevel() == Level.TRACE;
    }
    
    private static void setGlobalLevel(Level level) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.getLogger("ROOT").setLevel(level);
    }
}