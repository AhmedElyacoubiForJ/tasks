package edu.yacoubi.tasks.api.tasklists.usecase;

import edu.yacoubi.tasks.api.base.TaskApiRestAssuredTestBase;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * TaskListsUseCaseControllerHappyPathTest
 * ---------------------------------------
 * Testet alle erfolgreichen fachlichen Use-Case-Operationen für TaskLists.
 *
 * Abgedeckte Endpunkte:
 *   - PUT    /api/tasklists/{id}/archive
 *   - POST   /api/tasklists/{id}/activate
 *   - PATCH  /api/tasklists/{id}/status
 *
 * Zweck:
 *   Sicherstellen, dass alle fachlichen Abläufe (Use-Cases) im Happy Path
 *   korrekt funktionieren – ohne technische Fehler, ohne Domainfehler.
 *
 * NICHT enthalten:
 *   - CRUD-Operationen (POST/GET/PUT/PATCH/DELETE auf /tasklists)
 *   - Validierungsfehler (400)
 *   - Technische Fehler (404/500)
 *   - Domainregel-Verletzungen (409/422)
 *
 * Aufruf (CLI):
 *   ./mvnw test -Dtest=TaskListsUseCaseControllerHappyPathTest
 */
class TaskListsUseCaseControllerHappyPathTest extends TaskApiRestAssuredTestBase {

    // ------------------------------------------------------------
    // ARCHIVIEREN
    // ------------------------------------------------------------
    @Test
    void testArchive_Succeeds() {
        String id = createTaskList("Liste");

        given()
                .when()
                .put("/tasklists/" + id + "/archive")
                .then()
                .statusCode(200)
                .body("message", equalTo("Archiviert"));
    }

    // ------------------------------------------------------------
    // AKTIVIEREN
    // ------------------------------------------------------------
    @Test
    void testActivate_Succeeds() {
        String id = createTaskList("Liste");

        given().when().put("/tasklists/" + id + "/archive").then().statusCode(200);

        given()
                .when()
                .post("/tasklists/" + id + "/activate")
                .then()
                .statusCode(200)
                .body("message", equalTo("Aktiviert"));
    }

    // ------------------------------------------------------------
    // STATUSWECHSEL
    // ------------------------------------------------------------
    @Test
    void testChangeStatus_Succeeds() {
        String id = createTaskList("Liste");

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "status": "IN_PROGRESS"
                        }
                        """)
                .when()
                .patch("/tasklists/" + id + "/status")
                .then()
                .statusCode(200)
                .body("data.status", equalTo("IN_PROGRESS"));
    }
}
