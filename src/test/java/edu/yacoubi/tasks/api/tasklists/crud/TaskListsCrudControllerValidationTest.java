package edu.yacoubi.tasks.api.tasklists.crud;

import edu.yacoubi.tasks.api.base.TaskApiRestAssuredTestBase;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * TaskListsCrudControllerValidationTest
 * -------------------------------------
 * Testet alle Validierungsfehler der TaskList-CRUD-API.
 *
 * Abgedeckte Endpunkte:
 *   - POST   /api/tasklists
 *   - PUT    /api/tasklists/{id}
 *   - PATCH  /api/tasklists/{id}
 *
 * Zweck:
 *   Sicherstellen, dass ungültige Eingaben korrekt mit 400 BAD REQUEST
 *   beantwortet werden.
 */
class TaskListsCrudControllerValidationTest extends TaskApiRestAssuredTestBase {

    // ------------------------------------------------------------
    // POST /api/tasklists
    // → title fehlt → 400 Validierungsfehler
    // ------------------------------------------------------------
    @Test
    void testCreateTaskList_MissingTitle() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "description": "Desc"
                        }
                        """)
                .when()
                .post("/tasklists")
                .then()
                .log().body()
                .statusCode(400)
                .body("message", equalTo("Validierungsfehler"))
                .body("errors[0].message", equalTo("title: Titel darf nicht leer sein"));
    }

    // ------------------------------------------------------------
    // POST /api/tasklists
    // → ungültiges JSON → 400 Ungültiges JSON
    // ------------------------------------------------------------
    @Test
    void testCreateTaskList_InvalidJson() {
        given()
                .contentType(ContentType.JSON)
                .body("{ invalid json }")
                .when()
                .post("/tasklists")
                .then()
                .log().body()
                .statusCode(400)
                .body("message", equalTo("Ungültiges JSON"))
                .body("errors[0].message", equalTo("JSON konnte nicht gelesen werden"));
    }

    // ------------------------------------------------------------
    // PUT /api/tasklists/{id}
    // → Body leer → 400 Validierungsfehler
    // ------------------------------------------------------------
    @Test
    void testUpdateTaskList_EmptyBody() {
        String id = createTaskList("Alt");

        given()
                .contentType(ContentType.JSON)
                .body("{}")
                .when()
                .put("/tasklists/" + id)
                .then()
                .log().body()
                .statusCode(400)
                .body("message", equalTo("Validierungsfehler"));
    }

    // ------------------------------------------------------------
    // PUT /api/tasklists/{id}
    // → ungültiges JSON → 400 Ungültiges JSON
    // ------------------------------------------------------------
    @Test
    void testUpdateTaskList_InvalidJson() {
        String id = createTaskList("Alt");

        given()
                .contentType(ContentType.JSON)
                .body("{ invalid json }")
                .when()
                .put("/tasklists/" + id)
                .then()
                .log().body()
                .statusCode(400)
                .body("message", equalTo("Ungültiges JSON"))
                .body("errors[0].message", equalTo("JSON konnte nicht gelesen werden"));
    }

    // ------------------------------------------------------------
    // PATCH /api/tasklists/{id}
    // → ungültiges JSON → 400 Ungültiges JSON
    // ------------------------------------------------------------
    @Test
    void testPatchTaskList_InvalidJson() {
        String id = createTaskList("Patch");

        given()
                .contentType(ContentType.JSON)
                .body("{ invalid json }")
                .when()
                .patch("/tasklists/" + id)
                .then()
                .log().body()
                .statusCode(400)
                .body("message", equalTo("Ungültiges JSON"))
                .body("errors[0].message", equalTo("JSON konnte nicht gelesen werden"));
    }
}
