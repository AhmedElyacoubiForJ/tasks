package edu.yacoubi.tasks.htmxdemos.einstieg.model;

public record Person(
        String name,
        String email
) {
    public Person() {
        this("", "");
    }
}
