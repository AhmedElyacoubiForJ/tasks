package edu.yacoubi.tasks.api.tasklists.tasks.crud;

import edu.yacoubi.tasks.api.base.TaskApiRestAssuredTestBase;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

/**
 * TaskListsTasksCrudControllerMethodNotAllowedTest
 * ------------------------------------------------
 * Testet alle 405 METHOD NOT ALLOWED Fälle für:
 *
 *   /api/tasklists/{taskListId}/tasks
 *   /api/tasklists/{taskListId}/tasks/{taskId}
 */
class TaskListsTasksCrudControllerMethodNotAllowedTest extends TaskApiRestAssuredTestBase {

    // ------------------------------------------------------------
    // COLLECTION: /tasklists/{listId}/tasks
    // ------------------------------------------------------------

    @Test
    void testPutOnTasksCollection_NotAllowed() {
        String listId = createTaskList();

        given()
                .when()
                .put("/tasklists/" + listId + "/tasks")
                .then()
                .statusCode(405);
    }

    @Test
    void testPatchOnTasksCollection_NotAllowed() {
        String listId = createTaskList();

        given()
                .when()
                .patch("/tasklists/" + listId + "/tasks")
                .then()
                .statusCode(405);
    }

    @Test
    void testDeleteOnTasksCollection_NotAllowed() {
        String listId = createTaskList();

        given()
                .when()
                .delete("/tasklists/" + listId + "/tasks")
                .then()
                .statusCode(405);
    }

    // ------------------------------------------------------------
    // ITEM: /tasklists/{listId}/tasks/{taskId}
    // ------------------------------------------------------------

    @Test
    void testPostOnTaskItem_NotAllowed() {
        String listId = createTaskList();
        String taskId = createTask(listId);

        given()
                .when()
                .post("/tasklists/" + listId + "/tasks/" + taskId)
                .then()
                .statusCode(405);
    }
}
