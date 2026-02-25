package edu.yacoubi.tasks.api.tasklists.tasks;

import edu.yacoubi.tasks.api.base.TaskApiRestAssuredTestBase;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

class TaskListsTasksControllerDomainTest extends TaskApiRestAssuredTestBase {

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
