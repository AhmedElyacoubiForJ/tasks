package edu.yacoubi.tasks.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class JsonLoggingInterceptor implements HandlerInterceptor {

    private static final Logger jsonLogger = LoggerFactory.getLogger("jsonLogger");
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final String TRACE_ID = "traceId";
    private static final String START_TIME = "startTime";

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        request.setAttribute(TRACE_ID, UUID.randomUUID().toString());
        request.setAttribute(START_TIME, Instant.now());

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {

        Instant startTime = (Instant) request.getAttribute(START_TIME);
        String traceId = (String) request.getAttribute(TRACE_ID);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        Map<String, Object> logData = new LinkedHashMap<>();
        logData.put("timestamp", LocalDateTime.now().format(formatter));
        logData.put("traceId", traceId);
        logData.put("method", request.getMethod());
        logData.put("path", request.getRequestURI());
        logData.put("status", response.getStatus());
        logData.put("durationMs", Duration.between(startTime, Instant.now()).toMillis());

        if (ex != null) {
            logData.put("exception", ex.getClass().getSimpleName());
            logData.put("errorMessage", ex.getMessage());
        }

        try {
            jsonLogger.info(objectMapper.writeValueAsString(logData));
        } catch (Exception e) {
            jsonLogger.warn("⚠️ Fehler beim Serialisieren des Log-Eintrags: {}", e.getMessage());
        }
    }
}
