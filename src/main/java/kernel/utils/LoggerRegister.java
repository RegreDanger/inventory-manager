package kernel.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;

public class LoggerRegister {
    private static LoggerRegister instance;
    private Logger logger;

    private LoggerRegister() {
        logger = LoggerFactory.getLogger(LoggerRegister.class);
    }

    public static LoggerRegister getInstance() {
        if (instance == null) {
            instance = new LoggerRegister();
        }
        return instance;
    }

    public void setClass(Class<?> clazz) {
        logger = LoggerFactory.getLogger(clazz);
    }
    
    public void activate() {
        changeLogLevel(Level.TRACE);
    }

    public void deactivate() {
        changeLogLevel(Level.OFF);
    }

    private void changeLogLevel(Level level) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        ch.qos.logback.classic.Logger rootLogger = context.getLogger("ROOT");
        rootLogger.setLevel(level);
    }
    
    public boolean isActive() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        ch.qos.logback.classic.Logger rootLogger = context.getLogger("ROOT");
        return rootLogger.getLevel() == Level.TRACE;
    }

    public Logger getLogger() {
        return logger;
    }
}
