package edu.yacoubi.tasks.api.base;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.given;

public abstract class TaskApiRestAssuredTestBase {

    @BeforeAll
    static void setupRestAssured() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/api"; // WICHTIG: ist der API-Prefix
    }

    /**
     * Hilfsmethode: Erstellt eine TaskList und gibt deren ID zurück.
     */
    protected String createTaskList() {
        return given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "title": "Meine Liste",
                          "description": "Beschreibung"
                        }
                        """)
                .when()
                .post("/tasklists")
                .then()
                .log().body() // logging
                .statusCode(201)
                .extract()
                .path("data.id");
    }

    /**
     * Hilfsmethode: Erstellt einen Task in einer TaskList und gibt dessen ID zurück.
     */
    protected String createTask(String listId) {
        return given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "title": "Task A",
                          "description": "Desc",
                          "priority": "MEDIUM"
                        }
                        """)
                .when()
                .post("/tasklists/" + listId + "/tasks")
                .then()
                .log().body() // logging
                .statusCode(201)
                .extract()
                .path("data.id");
    }

    /**
     * Hilfsmethode: Erstellt eine TaskList mit einem bestimmten Titel.
     */
    protected String createTaskList(String title) {
        return given()
                .contentType(ContentType.JSON)
                .body("""
                    {
                      "title": "%s",
                      "description": "Beschreibung"
                    }
                    """.formatted(title))
                .when()
                .post("/tasklists")
                .then()
                .log().body() // logging
                .statusCode(201)
                .extract()
                .path("data.id");
    }
}
