package edu.yacoubi.tasks.htmxdemos.inlineBearbeitung.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@AllArgsConstructor
@Getter
@Setter
public class Kunde {
    private Long id;
    private String name;
    private String email;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Kunde kunde = (Kunde) o;
        return Objects.equals(id, kunde.id) &&
                Objects.equals(name, kunde.name)
                && Objects.equals(email, kunde.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email);
    }

    @Override
    public String toString() {
        return "Kunde{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
