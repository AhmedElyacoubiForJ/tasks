package edu.yacoubi.tasks.api.tasklists.crud;

import edu.yacoubi.tasks.api.base.TaskApiRestAssuredTestBase;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * TaskListsCrudControllerNotFoundTest
 * -----------------------------------
 * Testet alle 404-Fehlerfälle der TaskList-CRUD-API.
 *
 * Abgedeckte Endpunkte:
 *   - GET    /api/tasklists/{id}
 *   - PUT    /api/tasklists/{id}
 *   - PATCH  /api/tasklists/{id}
 *   - DELETE /api/tasklists/{id}
 *
 * Zweck:
 *   Sicherstellen, dass die API korrekt mit "Ressource nicht gefunden" (404)
 *   reagiert, wenn eine TaskList-ID nicht existiert.
 */
class TaskListsCrudControllerNotFoundTest extends TaskApiRestAssuredTestBase {

    private static final String UNKNOWN_ID = UUID.randomUUID().toString();

    // ------------------------------------------------------------
    // GET /api/tasklists/{id}
    // → 404 Ressource nicht gefunden
    // ------------------------------------------------------------
    @Test
    void testGetTaskList_NotFound() {
        given()
                .when()
                .get("/tasklists/" + UNKNOWN_ID)
                .then()
                .log().body()
                .statusCode(404)
                .body("message", equalTo("Ressource nicht gefunden"));
    }

    // ------------------------------------------------------------
    // PUT /api/tasklists/{id}
    // → 404 Ressource nicht gefunden
    // ------------------------------------------------------------
    @Test
    void testUpdateTaskList_NotFound() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "title": "Neu",
                          "description": "Desc"
                        }
                        """)
                .when()
                .put("/tasklists/" + UNKNOWN_ID)
                .then()
                .log().body()
                .statusCode(404)
                .body("message", equalTo("Ressource nicht gefunden"));
    }

    // ------------------------------------------------------------
    // PATCH /api/tasklists/{id}
    // → 404 Ressource nicht gefunden
    // ------------------------------------------------------------
    @Test
    void testPatchTaskList_NotFound() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "title": "Patch"
                        }
                        """)
                .when()
                .patch("/tasklists/" + UNKNOWN_ID)
                .then()
                .log().body()
                .statusCode(404)
                .body("message", equalTo("Ressource nicht gefunden"));
    }

    // ------------------------------------------------------------
    // DELETE /api/tasklists/{id}
    // → 404 Ressource nicht gefunden
    // ------------------------------------------------------------
    @Test
    void testDeleteTaskList_NotFound() {
        given()
                .when()
                .delete("/tasklists/" + UNKNOWN_ID)
                .then()
                .log().body()
                .statusCode(404)
                .body("message", equalTo("Ressource nicht gefunden"));
    }
}
