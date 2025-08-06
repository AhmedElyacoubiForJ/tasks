package edu.yacoubi.tasks.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.resource.ResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

import java.util.Collections;
import java.util.List;

@Configuration
public class CustomUrlHandlerConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Statische Ressourcen für den spezifischen Pfad deaktivieren
        registry.addResourceHandler("/appspecific/com.chrome.devtools.json")
                .resourceChain(true)
                .addResolver(new NoOpResourceResolver());
    }

    @Bean
    public SimpleUrlHandlerMapping ignoreSpecificRequests() {
        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setUrlMap(
                Collections.singletonMap("/appspecific/com.chrome.devtools.json",
                        (HttpRequestHandler) (request, response) -> {
                            response.setStatus(HttpServletResponse.SC_NOT_FOUND); // Ignoriere die Anfrage mit einem 404-Status
                        }));
        mapping.setOrder(-1); // Höhere Priorität als die Standard-Handler
        return mapping;
    }

    private static class NoOpResourceResolver implements ResourceResolver {
        @Override
        public org.springframework.core.io.Resource resolveResource(HttpServletRequest request, String requestPath, List<? extends org.springframework.core.io.Resource> locations, ResourceResolverChain chain) {
            return null; // Keine Ressource auflösen
        }

        @Override
        public String resolveUrlPath(String resourcePath, List<? extends org.springframework.core.io.Resource> locations, ResourceResolverChain chain) {
            return null; // Keine URL auflösen
        }
    }
}