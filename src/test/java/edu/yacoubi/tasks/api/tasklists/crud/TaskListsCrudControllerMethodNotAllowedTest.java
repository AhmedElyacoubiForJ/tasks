package edu.yacoubi.tasks.api.tasklists.crud;

import edu.yacoubi.tasks.api.base.TaskApiRestAssuredTestBase;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

/**
 * TaskListsCrudControllerMethodNotAllowedTest
 * -------------------------------------------
 * Testet alle 405 METHOD NOT ALLOWED Fälle der TaskList-CRUD-API.
 *
 * Abgedeckte Endpunkte:
 *   - /api/tasklists
 *   - /api/tasklists/{id}
 *
 * Zweck:
 *   Sicherstellen, dass verbotene HTTP-Methoden korrekt mit 405 beantwortet werden.
 *
 * Aufruf (CLI):
 *   ./mvnw test -Dtest=TaskListsCrudControllerMethodNotAllowedTest
 */
class TaskListsCrudControllerMethodNotAllowedTest extends TaskApiRestAssuredTestBase {

    // ------------------------------------------------------------
    // COLLECTION: /tasklists
    // ------------------------------------------------------------

    @Test
    void testPutOnTaskListsCollection_NotAllowed() {
        given()
                .when()
                .put("/tasklists")
                .then()
                .statusCode(405);
    }

    @Test
    void testPatchOnTaskListsCollection_NotAllowed() {
        given()
                .when()
                .patch("/tasklists")
                .then()
                .statusCode(405);
    }

    @Test
    void testDeleteOnTaskListsCollection_NotAllowed() {
        given()
                .when()
                .delete("/tasklists")
                .then()
                .statusCode(405);
    }

    // ------------------------------------------------------------
    // ITEM: /tasklists/{id}
    // ------------------------------------------------------------

    @Test
    void testPostOnTaskListItem_NotAllowed() {
        String id = createTaskList("Liste");

        given()
                .when()
                .post("/tasklists/" + id)
                .then()
                .statusCode(405);
    }
}
