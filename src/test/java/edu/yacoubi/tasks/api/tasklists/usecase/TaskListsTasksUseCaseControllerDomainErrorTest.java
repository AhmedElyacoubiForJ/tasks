package edu.yacoubi.tasks.api.tasklists.usecase;

import edu.yacoubi.tasks.api.base.TaskApiRestAssuredTestBase;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * TaskListsUseCaseControllerDomainErrorTest
 * -----------------------------------------
 * Testet alle fachlichen Domainregel-Verletzungen (409 / 422) der
 * TaskList-Use-Case-API.
 *
 * Abgedeckte Endpunkte (Use-Cases):
 *   - PUT    /api/tasklists/{id}/archive
 *   - POST   /api/tasklists/{id}/activate
 *   - PATCH  /api/tasklists/{id}/status
 *   - POST   /api/tasklists/{id}/tasks
 *   - PATCH  /api/tasklists/{id}/tasks/{taskId}/status
 *   - DELETE /api/tasklists/{id}/tasks/{taskId}
 *
 * Fehlercodes:
 *   - 409 → DomainRuleViolationException (fachliche Regel verletzt)
 *   - 422 → DomainValidationException (fachliche Validierung fehlgeschlagen)
 *
 * Zweck:
 *   Sicherstellen, dass alle Domainregeln des TaskList-Aggregats korrekt
 *   durchgesetzt werden, wenn Use-Case-Endpunkte aufgerufen werden.
 *
 * NICHT enthalten:
 *   - CRUD-Fehler (400 / 404 / 405)
 *   - Technische Fehler (z.B. JSON-Parsing, DTO-Validierung)
 *   - Happy-Path-Use-Cases (200 / 204)
 *
 * Aufruf (CLI):
 *   ./mvnw test -Dtest=TaskListsUseCaseControllerDomainErrorTest
 */

class TaskListsTasksUseCaseControllerDomainErrorTest extends TaskApiRestAssuredTestBase {

    // ------------------------------------------------------------
    // DELETE /api/tasklists/{taskListId}/tasks/{taskId}
    // → Task gehört NICHT zur angegebenen TaskList → 409
    // ------------------------------------------------------------
    @Test
    void testDeleteTask_WrongList() {
        String listA = createTaskList();
        String listB = createTaskList();

        createTask(listA);

        String taskId =
                given().when().get("/tasklists/" + listA + "/tasks").then().extract().path("data[0].id");

        given()
                .when()
                .delete("/tasklists/" + listB + "/tasks/" + taskId)
                .then()
                .log()
                .body() // all()
                .statusCode(409)
                .body("message", equalTo("Domainregel verletzt"));
    }

    // ------------------------------------------------------------
    // ARCHIVIERUNG
    // ------------------------------------------------------------

    @Test
    void testArchive_Fails_WhenOpenTasksExist() {
        String id = createTaskList("Liste");
        createTask(id);

        given()
                .when()
                .put("/tasklists/" + id + "/archive")
                .then()
                .log().body()
                .statusCode(409)
                .body("message", equalTo("Domainregel verletzt"));
    }

    //@Test TODO implement status use case
    void testArchive_Succeeds_WhenAllTasksCompleted() {
        String id = createTaskList("Liste");

        // Task erstellen (ID ist hier noch null!)
        createTask(id);

        // Echte Task-ID holen
        String taskId =
                given()
                        .when()
                        .get("/tasklists/" + id + "/tasks")
                        .then()
                        .log().body()
                        .statusCode(200)
                        .extract()
                        .path("data[0].id");

        // Task abschließen
        given()
                .contentType(ContentType.JSON)
                .body("""
                    {
                        "status": "COMPLETED"
                    }
                    """)
                .when()
                .patch("/tasklists/" + id + "/tasks/" + taskId + "/status")
                .then()
                .log().body()
                .statusCode(200);

        // Archivieren
        given()
                .when()
                .put("/tasklists/" + id + "/archive")
                .then()
                .log().body()
                .statusCode(200);
    }


    // ------------------------------------------------------------
    // REAKTIVIERUNG
    // ------------------------------------------------------------

    // @Test TODO implement activate use case
    void testActivate_Succeeds_WhenArchived() {
        String id = createTaskList("Liste");

        given().when().post("/tasklists/" + id + "/archive").then().statusCode(200);

        given()
                .when()
                .post("/tasklists/" + id + "/activate")
                .then()
                .log().body()
                .statusCode(200);
    }

    // ------------------------------------------------------------
    // STATUSWECHSEL
    // ------------------------------------------------------------

    // @Test TODO
    void testChangeStatus_Fails_WhenNull() {
        String id = createTaskList("Liste");

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                            "status": null
                        }
                        """)
                .when()
                .patch("/tasklists/" + id + "/status")
                .then()
                .log().body()
                .statusCode(422)
                .body("message", equalTo("Domain-Validierung fehlgeschlagen"));
    }

    // @Test
    void testChangeStatus_Fails_WhenArchivedRequested() {
        String id = createTaskList("Liste");

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                            "status": "ARCHIVED"
                        }
                        """)
                .when()
                .patch("/tasklists/" + id + "/status")
                .then()
                .log().body()
                .statusCode(409)
                .body("message", equalTo("Domainregel verletzt"));
    }

    // ------------------------------------------------------------
    // TITEL-REGELN
    // ------------------------------------------------------------

    // @Test
    void testRename_Fails_WhenTitleBlank() {
        String id = createTaskList("Liste");

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                            "title": ""
                        }
                        """)
                .when()
                .patch("/tasklists/" + id)
                .then()
                .log().body()
                .statusCode(422)
                .body("message", equalTo("Domain-Validierung fehlgeschlagen"));
    }

    // ------------------------------------------------------------
    // TASK-REGELN
    // ------------------------------------------------------------

    // @Test
    void testCreateTask_Fails_WhenListArchived() {
        String id = createTaskList("Liste");

        given().when().post("/tasklists/" + id + "/archive").then().statusCode(200);

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                            "title": "Task",
                            "description": "Desc",
                            "priority": "HIGH"
                        }
                        """)
                .when()
                .post("/tasklists/" + id + "/tasks")
                .then()
                .log().body()
                .statusCode(409)
                .body("message", equalTo("Domainregel verletzt"));
    }

    // @Test
    void testChangeTaskStatus_Fails_WhenListArchived() {
        String id = createTaskList("Liste");
        String taskId = createTask(id);

        // Task abschließen
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                            "status": "COMPLETED"
                        }
                        """)
                .when()
                .patch("/tasklists/" + id + "/tasks/" + taskId + "/status")
                .then()
                .statusCode(200);

        // Archivieren
        given().when().post("/tasklists/" + id + "/archive").then().statusCode(200);

        // Status ändern → verboten
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                            "status": "IN_PROGRESS"
                        }
                        """)
                .when()
                .patch("/tasklists/" + id + "/tasks/" + taskId + "/status")
                .then()
                .log().body()
                .statusCode(409)
                .body("message", equalTo("Domainregel verletzt"));
    }

    //@Test
    void testRemoveTask_Fails_WhenTaskNotInList() {
        String id = createTaskList("Liste");
        String otherList = createTaskList("Andere Liste");
        String foreignTask = createTask(otherList);

        given()
                .when()
                .delete("/tasklists/" + id + "/tasks/" + foreignTask)
                .then()
                .log().body()
                .statusCode(409)
                .body("message", equalTo("Domainregel verletzt"));
    }
}
