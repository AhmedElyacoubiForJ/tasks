package edu.yacoubi.tasks.api.tasklists.crud;

import edu.yacoubi.tasks.api.base.TaskApiRestAssuredTestBase;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * TaskListsCrudControllerHappyPathTest
 * ------------------------------------
 * Testet alle erfolgreichen CRUD-Operationen für TaskLists.
 *
 * Abgedeckte Endpunkte:
 *   - GET    /api/tasklists
 *   - GET    /api/tasklists/{id}
 *   - POST   /api/tasklists
 *   - PUT    /api/tasklists/{id}
 *   - PATCH  /api/tasklists/{id}
 *   - DELETE /api/tasklists/{id}
 *
 * Zweck:
 *   Sicherstellen, dass alle CRUD-Operationen im Happy Path korrekt funktionieren.
 */
class TaskListsCrudControllerHappyPathTest extends TaskApiRestAssuredTestBase {

    // ------------------------------------------------------------
    // POST /api/tasklists
    // → TaskList erfolgreich erstellen
    // ------------------------------------------------------------
    @Test
    void testCreateTaskList() {
        given()
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
                .log().body()
                .statusCode(201)
                .body("data.id", notNullValue())
                .body("data.title", equalTo("Meine Liste"));
    }

    // ------------------------------------------------------------
    // GET /api/tasklists
    // → Alle TaskLists abrufen
    // ------------------------------------------------------------
    @Test
    void testGetAllTaskLists() {
        createTaskList("Liste A");
        createTaskList("Liste B");

        given()
                .when()
                .get("/tasklists")
                .then()
                .log().body()
                .statusCode(200)
                .body("data", hasSize(greaterThanOrEqualTo(2)));
    }

    // ------------------------------------------------------------
    // GET /api/tasklists/{id}
    // → Einzelne TaskList abrufen
    // ------------------------------------------------------------
    @Test
    void testGetTaskListById() {
        String id = createTaskList("Einzelne Liste");

        given()
                .when()
                .get("/tasklists/" + id)
                .then()
                .log().body()
                .statusCode(200)
                .body("data.id", equalTo(id))
                .body("data.title", equalTo("Einzelne Liste"));
    }

    // ------------------------------------------------------------
    // PUT /api/tasklists/{id}
    // → TaskList vollständig aktualisieren
    // ------------------------------------------------------------
    @Test
    void testUpdateTaskList() {
        String id = createTaskList("Alt");

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "title": "Neu",
                          "description": "Neue Beschreibung"
                        }
                        """)
                .when()
                .put("/tasklists/" + id)
                .then()
                .log().body()
                .statusCode(200)
                .body("data.title", equalTo("Neu"));
    }

    // ------------------------------------------------------------
    // PATCH /api/tasklists/{id}
    // → TaskList teilweise aktualisieren
    // ------------------------------------------------------------
    @Test
    void testPatchTaskList() {
        String id = createTaskList("Patch Alt");

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "title": "Patch Neu"
                        }
                        """)
                .when()
                .patch("/tasklists/" + id)
                .then()
                .log().body()
                .statusCode(200)
                .body("data.title", equalTo("Patch Neu"));
    }

    // ------------------------------------------------------------
    // DELETE /api/tasklists/{id}
    // → TaskList löschen
    // ------------------------------------------------------------
    @Test
    void testDeleteTaskList() {
        String id = createTaskList("Zum Löschen");

        given()
                .when()
                .delete("/tasklists/" + id)
                .then()
                .log().body()
                .statusCode(200);

        // Sicherstellen, dass sie weg ist
        given()
                .when()
                .get("/tasklists/" + id)
                .then()
                .statusCode(404);
    }
}
