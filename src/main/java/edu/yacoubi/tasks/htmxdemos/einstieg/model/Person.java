package edu.yacoubi.tasks.htmxdemos.model;

public record Person(
        String name,
        String email
) {
    public Person() {
        this("", "");
    }
}
