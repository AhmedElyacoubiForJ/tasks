package edu.yacoubi.tasks.api.tasklists.tasks;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

import edu.yacoubi.tasks.api.base.TaskApiRestAssuredTestBase;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

class TaskListsTasksControllerHappyPathTest extends TaskApiRestAssuredTestBase {

    // ------------------------------------------------------------
    // POST /api/tasklists/{taskListId}/tasks
    // ------------------------------------------------------------
    @Test
    void testCreateTask() {
        String listId = createTaskList();

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "title": "Neuer Task",
                          "description": "Beschreibung",
                          "priority": "HIGH"
                        }
                        """)
                .when()
                .post("/tasklists/" + listId + "/tasks")
                .then()
                .log().all()
                .statusCode(201)
                .body("data.title", equalTo("Neuer Task"))
                .body("data.priority", equalTo("HIGH"));
    }

    @Test
    void testGetTasksByListId() {
        String listId = createTaskList();
        createTask(listId);

        given()
                .when()
                .get("/tasklists/" + listId + "/tasks")
                .then()
                .statusCode(200)
                .body("data.size()", greaterThan(0));
    }

    // ------------------------------------------------------------
    // PATCH /api/tasklists/{taskListId}/tasks/{taskId}
    // ------------------------------------------------------------
    @Test
    void testPatchTask() {
        // 1. TaskList erstellen
        String listId = createTaskList();

        // 2. Task erstellen (ID ist hier noch null!)
        createTask(listId);

        // 3. Echte Task-ID über GET holen (Workaround)
        // Workaround: echte Task-ID aus der DB holen (Create liefert aktuell id = null)
        String taskId = given()
                .when()
                .get("/tasklists/" + listId + "/tasks")
                .then()
                .statusCode(200)
                .extract()
                .path("data[0].id");   // <-- echte ID aus DB


        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "priority": "HIGH"
                        }
                        """)
                .when()
                .patch("/tasklists/" + listId + "/tasks/" + taskId)
                .then()
                .log().all()
                .statusCode(200)
                .body("data.priority", equalTo("HIGH"));
    }

    // Task vollständig aktualisieren
    // PUT /api/tasklists/{taskListId}/tasks/{taskId}
    // HINWEIS / TODO:
    // Dieser Test verwendet einen Workaround, um die Task-ID über GET abzurufen,
    // da die Create-Task-Response aktuell "id = null" zurückliefert.
    // Ursache ist das noch ungelöste bidirektionale Mapping-Problem (Task <-> TaskList),
    // wodurch Hibernate die generierte ID nicht in das zurückgegebene DTO schreibt.
    //
    // Der Workaround ermöglicht es uns, den PUT-Endpunkt trotzdem vollständig zu testen.
    // Sobald das Domain-Modell korrigiert ist, kann dieser Workaround entfernt werden.
    @Test
    void testFullUpdateTask() {
        // 1. TaskList erstellen
        String listId = createTaskList();

        // 2. Task erstellen (ID ist hier noch null!)
        createTask(listId);

        // 3. Echte Task-ID über GET holen (Workaround)
        // Workaround: echte Task-ID aus der DB holen (Create liefert aktuell id = null)
        String taskId = given()
                .when()
                .get("/tasklists/" + listId + "/tasks")
                .then()
                .statusCode(200)
                .extract()
                .path("data[0].id");   // <-- echte ID aus DB

        // 4. Full Update durchführen
        given()
                .contentType(ContentType.JSON)
                .body("""
                    {
                      "title": "Updated Task",
                      "description": "Neue Beschreibung",
                      "priority": "LOW",
                      "dueDate": "2030-01-01T10:00:00",
                      "status": "OPEN"
                    }
                    """)
                .when()
                .put("/tasklists/" + listId + "/tasks/" + taskId)
                .then()
                .log().all()
                .statusCode(200)
                .body("data.title", equalTo("Updated Task"))
                .body("data.priority", equalTo("LOW"))
                .body("data.status", equalTo("OPEN"));
    }

    @Test
    void testDeleteTask() {
        String listId = createTaskList();
        createTask(listId);

        String taskId = given()
                .when()
                .get("/tasklists/" + listId + "/tasks")
                .then()
                .extract()
                .path("data[0].id");

        given()
                .when()
                .delete("/tasklists/" + listId + "/tasks/" + taskId)
                .then()
                .statusCode(200);
    }
}
