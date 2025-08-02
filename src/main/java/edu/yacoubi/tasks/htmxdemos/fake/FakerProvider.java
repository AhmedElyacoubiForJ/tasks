package edu.yacoubi.tasks.htmxdemos.fake;

import com.github.javafaker.Faker;
import org.springframework.stereotype.Component;

@Component
public class FakerProvider {
    private final Faker faker = new Faker();

    public Faker getFaker() {
        return faker;
    }
}