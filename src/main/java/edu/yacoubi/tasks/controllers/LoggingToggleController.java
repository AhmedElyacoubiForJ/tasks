package edu.yacoubi.tasks.controllers;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import net.logstash.logback.encoder.LogstashEncoder;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//@RestController
@RequestMapping("/logging")
public class LoggingToggleController {

    @PostMapping("/switch")
    public ResponseEntity<String> switchLogging(@RequestParam String mode) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger rootLogger = context.getLogger(Logger.ROOT_LOGGER_NAME);

        rootLogger.detachAndStopAllAppenders();

        if ("json".equalsIgnoreCase(mode)) {
            LogstashEncoder encoder = new LogstashEncoder();
            ConsoleAppender<ILoggingEvent> jsonAppender = new ConsoleAppender<>();
            jsonAppender.setContext(context);
            jsonAppender.setEncoder(encoder);
            jsonAppender.start();
            rootLogger.addAppender(jsonAppender);
        } else {
            PatternLayoutEncoder encoder = new PatternLayoutEncoder();
            encoder.setContext(context);
            encoder.setPattern("%d{HH:mm:ss} %-5level %logger{36} - %msg%n");
            encoder.start();

            ConsoleAppender<ILoggingEvent> consoleAppender = new ConsoleAppender<>();
            consoleAppender.setContext(context);
            consoleAppender.setEncoder(encoder);
            consoleAppender.start();
            rootLogger.addAppender(consoleAppender);
        }

        return ResponseEntity.ok("Logging-Modus gewechselt zu: " + mode);
    }
}

