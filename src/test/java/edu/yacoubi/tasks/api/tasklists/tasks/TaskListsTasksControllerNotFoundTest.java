package edu.yacoubi.tasks.api.tasklists.tasks;

import edu.yacoubi.tasks.api.base.TaskApiRestAssuredTestBase;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

class TaskListsTasksControllerNotFoundTest extends TaskApiRestAssuredTestBase {

    @Test
    void testGetTasksByListId_NotFound() {
        given()
                .when()
                .get("/tasklists/" + UUID.randomUUID() + "/tasks")
                .then()
                .statusCode(404)
                .body("message", equalTo("Ressource nicht gefunden"));
    }

    @Test
    void testDeleteTask_NotFound() {
        String listId = createTaskList();

        given()
                .when()
                .delete("/tasklists/" + listId + "/tasks/" + UUID.randomUUID())
                .then()
                .statusCode(404)
                .body("message", equalTo("Ressource nicht gefunden"));
    }
}
