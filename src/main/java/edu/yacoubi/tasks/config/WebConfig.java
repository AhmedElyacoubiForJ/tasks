package edu.yacoubi.tasks.config;

import edu.yacoubi.tasks.interceptor.ConsoleLoggingInterceptor;
import edu.yacoubi.tasks.interceptor.JsonLoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * Statische Seiten ohne Controller anzeigen
 *
 * Deine API absichern
 *
 * Ressourcen gezielt freigeben
 *
 * Logging oder Auth-Checks einbauen
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${logging.interceptor.enabled}")
    private String activeInterceptor;

    @Autowired
    private ConsoleLoggingInterceptor consoleInterceptor;

    @Autowired
    private JsonLoggingInterceptor jsonInterceptor;

    // ViewController: /about-Seite ohne Controller
    // statische Seite about.html anzeigen,
    // ohne extra einen Controller zu schreiben.
    // man braucht nur src/main/resources/templates/about.html
    // – kein Controller nötig!
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/about").setViewName("about");
    }

    // CORS-Konfiguration: z.B. für externe Clients
    // API für Tasks bereitstellen:
    // 🔐 schützt der API & gezielte Zugriff freigeben.
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("https://mein-client.de")
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }

    // Statische Ressourcen: z.B. für eigene Icons oder CSS
    // 📁 Dann kann man z.B. <img src="/icons/check.svg"> direkt verwenden.
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/icons/**")
                .addResourceLocations("classpath:/static/icons/");
    }

    // Interceptor: z.B. Logging aller Task-Requests
    // 📋 Man bekommt bei jedem Task-Request eine Log-Ausgabe – super für Debugging!
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        if ("json".equalsIgnoreCase(activeInterceptor)) {
            registry.addInterceptor(jsonInterceptor)
                    .addPathPatterns("/tasklists/**")
                    .addPathPatterns("/task-lists/**");
        } else {
            registry.addInterceptor(consoleInterceptor)
                    .addPathPatterns("/tasklists/**")
                    .addPathPatterns("/task-lists/**");
        }
    }
}