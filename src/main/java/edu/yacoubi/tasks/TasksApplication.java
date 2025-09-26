package edu.yacoubi.tasks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TasksApplication {

    public static void main(String[] args) {
        SpringApplication.run(TasksApplication.class, args);
    }

/*
    @Bean
    public CommandLineRunner run(MessageSource messageSource) {
        return args -> {
            Resource resource = new ClassPathResource("messages_de.properties");

            try {
                Properties props = PropertiesLoaderUtils.loadProperties(resource);
                //props.forEach((k, v) -> System.out.println(k + ": " + v));
            } catch (IOException e) {
                System.err.println("Fehler beim Laden der Properties-Datei: " + e.getMessage());
            }

            // 🔍 Test mit MessageSource
            //String title = messageSource.getMessage("export.pdf.title", null, "Kein Titel gefunden", Locale.GERMAN);
            //System.out.println("📄 Titel via MessageSource: " + title);
        };
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }*/
}
