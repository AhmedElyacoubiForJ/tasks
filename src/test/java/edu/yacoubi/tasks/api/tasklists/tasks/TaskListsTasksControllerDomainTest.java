package edu.yacoubi.tasks.api.tasklists.tasks;

import edu.yacoubi.tasks.api.base.TaskApiRestAssuredTestBase;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * TaskListsTasksControllerDomainTest
 * ----------------------------------
 * Testet Domainregel-Verletzungen für Endpunkte unter:
 *
 *   /api/tasklists/{taskListId}/tasks/{taskId}
 *
 * Aufruf (CLI):
 *   ./mvnw test -Dtest=TaskListsTasksControllerDomainTest
 *
 * Abgedeckter Endpunkt:
 *   - DELETE /api/tasklists/{taskListId}/tasks/{taskId}
 *
 * Zweck:
 *   Sicherstellen, dass die API korrekt reagiert, wenn eine Task
 *   nicht zu der angegebenen TaskList gehört.
 *
 * Erwartetes Verhalten:
 *   → 409 CONFLICT + Fehlermeldung "Domainregel verletzt"
 */
class TaskListsTasksControllerDomainTest extends TaskApiRestAssuredTestBase {

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
}
