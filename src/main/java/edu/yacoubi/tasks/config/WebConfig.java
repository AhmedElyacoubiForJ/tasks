package edu.yacoubi.tasks.config;

import edu.yacoubi.tasks.interceptor.ConsoleLoggingInterceptor;
import edu.yacoubi.tasks.interceptor.JsonLoggingInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * WebConfig – zentrale Konfiguration für:
 * - Statische Seiten ohne Controller
 * - CORS-Zugriffssteuerung
 * - Ressourcenfreigabe
 * - Logging-Interceptor
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    @Value("${logging.interceptor.enabled}")
    private String activeInterceptor;

    private final ConsoleLoggingInterceptor consoleInterceptor;
    private final JsonLoggingInterceptor jsonInterceptor;

    /**
     * Zeigt die Seite /about ohne eigenen Controller.
     * Voraussetzung: Datei liegt unter src/main/resources/templates/about.html
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/about").setViewName("about");
    }

    /**
     * CORS-Konfiguration für externe Clients.
     * Erlaubt Zugriff auf die API unter /api/** von https://mein-client.de
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("https://mein-client.de")
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }

    /**
     * Statische Ressourcen wie Icons oder CSS-Dateien freigeben.
     * Beispiel: <img src="/icons/check.svg">
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/icons/**")
                .addResourceLocations("classpath:/static/icons/");
    }

    /**
     * Logging-Interceptor aktivieren für Task-Endpoints.
     * Auswahl über Property: logging.interceptor.enabled = json | console
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        var paths = new String[]{"/tasklists/**", "/task-lists/**"};

        if ("json".equalsIgnoreCase(activeInterceptor)) {
            registry.addInterceptor(jsonInterceptor).addPathPatterns(paths);
        } else {
            registry.addInterceptor(consoleInterceptor).addPathPatterns(paths);
        }
    }
}
