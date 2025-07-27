package edu.yacoubi.tasks.htmxdemo.model;

public record Person(
        String name,
        String email
) {
    public Person() {
        this("", "");
    }
}
