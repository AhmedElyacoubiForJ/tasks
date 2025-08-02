package edu.yacoubi.tasks.htmxdemos.config;

import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class LayoutDialectProvider {
    @Bean
    public LayoutDialect layoutDialect() {
        return new LayoutDialect();
    }
}
