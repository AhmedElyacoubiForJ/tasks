package edu.yacoubi.tasks.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

// Test mit Response‑Logging
// .log().all() // 🔥 vollständige Response in der Konsole
// Nur Body loggen : .then().log().body()
// Nur bei Fehlern loggen : .then().log().ifValidationFails()

// Empfehlung für produktive Test‑Suiten
// .log().ifValidationFails() in allen Tests
// .log().all() nur temporär beim Debuggen
// Utility‑Methoden für TaskList & Task
class TaskApiTest {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost:8080/api";
    }

    // ------------------------------------------------------------
    // Hilfsmethode: TaskList erstellen
    // POST /api/tasklists
    // ------------------------------------------------------------
    private String createTaskList() {
        return given()
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
                .statusCode(201)
                .extract()
                .path("data.id");
    }

    // ------------------------------------------------------------
    // Hilfsmethode: Task erstellen
    // POST /api/tasklists/{taskListId}/tasks
    // ------------------------------------------------------------
    private String createTask(String listId) {
        return given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "title": "Task A",
                          "description": "Desc",
                          "priority": "MEDIUM"
                        }
                        """)
                .when()
                .post("/tasklists/" + listId + "/tasks")
                .then()
                .statusCode(201)
                .extract()
                .path("data.id");
    }

    // ------------------------------------------------------------
    // GET /api/tasklists/{taskListId}/tasks
    // ------------------------------------------------------------
    @Test
    void testGetTasksByListId() {
        String listId = createTaskList();
        createTask(listId);
        createTask(listId);

        given()
                .when()
                .get("/tasklists/" + listId + "/tasks")
                .then()
                .log().body() // all()
                .statusCode(200)
                .body("data", hasSize(2));
    }

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

    // ------------------------------------------------------------
    // DELETE /api/tasklists/{taskListId}/tasks/{taskId}
    // ------------------------------------------------------------
    @Test
    void testDeleteTask() {
        // 1. TaskList erstellen
        String listId = createTaskList();

        // 2. Task erstellen (ID ist hier noch null!)
        createTask(listId);

        // 3. Echte Task-ID über GET holen (Workaround)
        String taskId = given()
                .when()
                .get("/tasklists/" + listId + "/tasks")
                .then()
                .statusCode(200)
                .extract()
                .path("data[0].id");

        // 4. DELETE durchführen
        given()
                .when()
                .delete("/tasklists/" + listId + "/tasks/" + taskId)
                .then()
                .log().all()
                .statusCode(200)
                .body("status", equalTo("SUCCESS"))
                .body("statusCode", equalTo(200))
                .body("message", equalTo("Task erfolgreich gelöscht"));
    }

    @Test
    void testGetTasksByListId_NotFound() {
        given()
                .when()
                .get("/tasklists/" + UUID.randomUUID() + "/tasks")
                .then()
                .log().all()
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
                .log().all()
                .statusCode(404)
                .body("message", equalTo("Ressource nicht gefunden"));
    }

    @Test
    void testDeleteTask_WrongList() {
        // 1. Zwei TaskLists erstellen
        String listA = createTaskList();
        String listB = createTaskList();

        // 2. Task in List A erstellen
        createTask(listA);

        // 3. Echte Task-ID aus List A holen
        String taskId = given()
                .when()
                .get("/tasklists/" + listA + "/tasks")
                .then()
                .statusCode(200)
                .extract()
                .path("data[0].id");

        // 4. Versuch: Task aus der falschen Liste löschen (List B)
        given()
                .when()
                .delete("/tasklists/" + listB + "/tasks/" + taskId)
                .then()
                .log().body() // all()
                .statusCode(409)
                .body("message", equalTo("Domainregel verletzt"));
    }


    /*
    * 👉 TaskListApiTest (OK‑Szenarien)
    * 👉 Fehlerfälle (404, 400, 409)
    * 👉 Statuswechsel‑Tests
    * 👉 Testcontainers Setup
    * */
}
