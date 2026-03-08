package edu.yacoubi.tasks.infrastructure.logging;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Iterator;
import java.util.UUID;

@Component
public class ConsoleLoggingInterceptor implements HandlerInterceptor {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ConsoleLoggingInterceptor.class);
    private static final String TRACE_ID = "traceId";
    private static final String START_TIME = "startTime";

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        // 🔍 Request-Infos sammeln
        String method = request.getMethod();;
        String uri = request.getRequestURI();
        String clientIP = "0:0:0:0:0:0:0:1".equals(request.getRemoteAddr()) ? "localhost" : request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        String[] pathSegments = uri.split("/");
        String username = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "👻 Anonym";
        boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
        String hxRequest = request.getHeader("HX-Request");
        String hxTarget = request.getHeader("HX-Target");
        String hxTrigger = request.getHeader("HX-Trigger");
        String hxBoosted = request.getHeader("HX-Boosted");
        String hxCurrentUrl = request.getHeader("HX-Current-URL");
        String hxPrompt = request.getHeader("HX-Prompt");
        boolean isHtmx = hxRequest != null;


        // 🧭 Trace-ID & Startzeit speichern
        String traceId = UUID.randomUUID().toString();
        Instant startTime = Instant.now();
        request.setAttribute(TRACE_ID, traceId);
        request.setAttribute(START_TIME, startTime);

        // 🎨 Ausgabe
        printAsciiHeader();
        log.info("🔁 Aktiver Logging-Modus: {}", detectLoggingMode());
        log.info("🛠️ ConsoleLoggingInterceptor aktiviert");
        log.info("📥 Anfrage: {} {}", method, uri);
        log.info("🌍 IP-Adresse: {}", clientIP);
        log.info("🧭 User-Agent: {}", userAgent);
        log.info("🙋 Benutzer: {}", username);
        log.info("⚡ AJAX-Request: {}", isAjax);
        log.info("⚡ HTMX-Request: {}", isHtmx);
        if (isHtmx) {
            log.info("📌 HX-Target: {}", hxTarget);
            log.info("🎯 HX-Trigger: {}", hxTrigger);
            log.info("🔗 HX-Current-URL: {}", hxCurrentUrl);
            log.info("🚀 HX-Boosted: {}", hxBoosted);
            log.info("💬 HX-Prompt: {}", hxPrompt);
        }
        log.info("⏱️ Zeitstempel: {}", LocalDateTime.now());
        log.info("🎯 Ziel-Handler: {}", handler.getClass().getSimpleName());
        log.info("🧩 Pfad-Segmente: {}", Arrays.toString(pathSegments));

        // 🎯 Spezialbehandlung für Task-Endpunkte
        if (uri.matches(".*/tasklists.*")) {
            log.debug("🎨 ViewController aktiv – Taskliste geöffnet 🧘");
        } else if (uri.matches(".*/task-lists.*")) {
            log.debug("🔌 REST-API aktiv – Datenstrom läuft 📡");
        }

        log.info("--------------------------------------------------");
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {

        // ⏱️ Dauer berechnen
        Instant startTime = (Instant) request.getAttribute(START_TIME);
        Duration duration = Duration.between(startTime, Instant.now());

        // 📌 Trace-ID & Status
        String traceId = (String) request.getAttribute(TRACE_ID);
        int status = response.getStatus();
        String mode = detectLoggingMode();

        // 🎨 Farbcode für Console-Modus
        String color = "";
        if ("console".equals(mode)) {
            if (status >= 200 && status < 300) {
                color = "\u001B[32m"; // Grün
            } else if (status >= 400) {
                color = "\u001B[31m"; // Rot
            }
        }
        String reset = "\u001B[0m";

        // 📊 Ergebnis-Logging
        log.info("{}📌 Trace-ID: {}{}", color, traceId, reset);
        log.info("{}📊 Dauer: {} ms{}", color, duration.toMillis(), reset);
        log.info("{}📤 HTTP-Status: {}{}", color, status, reset);
        log.info("--------------------------------------------------");
    }

    // 🖼️ ASCII-Art-Header für jede Anfrage
    private void printAsciiHeader() {
        System.out.println("""
                ╔══════════════════════════════╗
                ║     🚀 TASKLIST REQUEST      ║
                ╚══════════════════════════════╝
                """);
    }

    // 🔍 Logging-Modus erkennen (console/json)
    private String detectLoggingMode() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        ch.qos.logback.classic.Logger rootLogger = context.getLogger("ROOT");

        Iterator<Appender<ILoggingEvent>> iterator = rootLogger.iteratorForAppenders();
        while (iterator.hasNext()) {
            Appender<ILoggingEvent> appender = iterator.next();
            String appenderClass = appender.getClass().getSimpleName();
            if (appenderClass.contains("ConsoleAppender")) {
                return appenderClass.contains("Logstash") ? "json" : "console";
            }
        }
        return "unknown";
    }
}
