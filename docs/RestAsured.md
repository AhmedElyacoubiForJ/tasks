Alles klar, Ahmed — ich zeige dir **genau**, wie du eine professionelle **REST‑Assured Testklasse** für deine TaskList‑API baust.  
Ich mache das so, dass du sie **1:1 in dein Projekt übernehmen** kannst, inklusive:

- Setup
- Base‑URI
- GET‑Test
- POST‑Test
- PUT‑Test
- PATCH‑Test
- DELETE‑Test
- Assertions
- JSON‑Body‑Validierung

Und ich erkläre dir nebenbei, **wie REST‑Assured funktioniert**, damit du später selbst Tests erweitern kannst.

---

# ⭐ 1) **REST‑Assured in Maven aktivieren**

Falls du es noch nicht hast, füge in `pom.xml` hinzu:

```xml
<dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>rest-assured</artifactId>
    <version>5.4.0</version>
    <scope>test</scope>
</dependency>
```

Optional (für JSON‑Parsing):

```xml
<dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>json-path</artifactId>
    <version>5.4.0</version>
    <scope>test</scope>
</dependency>
```

---

# ⭐ 2) **Basis‑Testklasse erstellen**

Erstelle:

```
src/test/java/edu/yacoubi/tasks/api/TaskListApiTest.java
```

---

# ⭐ 3) **Komplette REST‑Assured Testklasse (copy‑paste ready)**

```java
package edu.yacoubi.tasks.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class TaskListApiTest {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost:8080/api/v1";
    }

    @Test
    void testGetAllTaskLists() {
        given()
                .when()
                .get("/tasklists")
                .then()
                .statusCode(200)
                .body("status", equalTo("SUCCESS"))
                .body("data", not(empty()));
    }

    @Test
    void testCreateTaskList() {
        String body = """
                {
                  "title": "🧪 Testliste",
                  "description": "REST-Assured Test"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/tasklists")
                .then()
                .statusCode(201)
                .body("data.title", equalTo("🧪 Testliste"))
                .body("data.description", equalTo("REST-Assured Test"));
    }

    @Test
    void testUpdateTaskList() {
        // 1) Neue Liste erstellen
        String id =
                given()
                        .contentType(ContentType.JSON)
                        .body("""
                                {
                                  "title": "PUT Test",
                                  "description": "Before update"
                                }
                                """)
                        .when()
                        .post("/tasklists")
                        .then()
                        .statusCode(201)
                        .extract()
                        .path("data.id");

        // 2) PUT Update
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "title": "PUT Updated",
                          "description": "After update",
                          "status": "ACTIVE"
                        }
                        """)
                .when()
                .put("/tasklists/" + id)
                .then()
                .statusCode(200)
                .body("data.title", equalTo("PUT Updated"))
                .body("data.description", equalTo("After update"));
    }

    @Test
    void testPatchTaskList() {
        // 1) Neue Liste erstellen
        String id =
                given()
                        .contentType(ContentType.JSON)
                        .body("""
                                {
                                  "title": "PATCH Test",
                                  "description": "Before patch"
                                }
                                """)
                        .when()
                        .post("/tasklists")
                        .then()
                        .statusCode(201)
                        .extract()
                        .path("data.id");

        // 2) PATCH Update
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "description": "After patch"
                        }
                        """)
                .when()
                .patch("/tasklists/" + id)
                .then()
                .statusCode(200)
                .body("data.description", equalTo("After patch"));
    }

    @Test
    void testDeleteTaskList() {
        // 1) Neue Liste erstellen
        String id =
                given()
                        .contentType(ContentType.JSON)
                        .body("""
                                {
                                  "title": "DELETE Test",
                                  "description": "To be deleted"
                                }
                                """)
                        .when()
                        .post("/tasklists")
                        .then()
                        .statusCode(201)
                        .extract()
                        .path("data.id");

        // 2) Löschen
        given()
                .when()
                .delete("/tasklists/" + id)
                .then()
                .statusCode(200);

        // 3) Sicherstellen, dass sie weg ist
        given()
                .when()
                .get("/tasklists/" + id)
                .then()
                .statusCode(404);
    }
}
```

---

# ⭐ 4) **Wie REST‑Assured funktioniert (kurz & klar)**

### **given()**
→ Setup  
Headers, Body, Auth, Content‑Type

### **when()**
→ HTTP‑Call  
GET, POST, PUT, PATCH, DELETE

### **then()**
→ Assertions  
Statuscodes, JSON‑Felder, Typen, Werte

---

# ⭐ 5) Was du jetzt testen kannst

- PATCH/PUT funktionieren
- Domain‑Regeln greifen
- API‑Response‑Wrapper korrekt
- Statuswechsel korrekt
- Fehlerfälle korrekt

---

Wenn du willst, kann ich dir jetzt:

👉 **eine vollständige Testklasse für Task‑Endpoints** erstellen  
👉 **Integrationstests für Domain‑Regeln** bauen  
👉 **MockMvc‑Tests statt REST‑Assured** generieren  
👉 **Testdaten‑Setup (SQL oder Java)** erstellen

Sag einfach, wohin wir weitergehen.