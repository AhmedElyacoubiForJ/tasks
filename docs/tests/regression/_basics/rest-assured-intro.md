# 📄 *Kompakte Einführung in REST‑Assured (projektunabhängig)*

## Was ist REST‑Assured?
REST‑Assured ist ein Java‑Framework zum Testen von REST‑APIs.  
Es folgt einem klaren, lesbaren Aufbau:

```
given()  → Setup (Headers, Body, Auth, Content-Type)
when()   → HTTP‑Call (GET, POST, PUT, PATCH, DELETE)
then()   → Assertions (Status, JSON‑Felder, Werte)
```

---

## Setup (Base‑URI)

```java
import static io.restassured.RestAssured.*;

@BeforeAll
static void setup() {
    baseURI = "http://localhost:8080";
}
```

---

## GET‑Test (Beispiel)

```java
given()
    .when()
        .get("/tasklists")
    .then()
        .statusCode(200);
```

---

## POST‑Test (Beispiel)

```java
given()
    .contentType("application/json")
    .body("{\"name\":\"My List\"}")
.when()
    .post("/tasklists")
.then()
    .statusCode(201)
    .body("name", equalTo("My List"));
```

---

## PUT‑Test

```java
given()
    .contentType("application/json")
    .body("{\"name\":\"Updated\"}")
.when()
    .put("/tasklists/1")
.then()
    .statusCode(200);
```

---

## PATCH‑Test

```java
given()
    .contentType("application/json")
    .body("{\"name\":\"Partial\"}")
.when()
    .patch("/tasklists/1")
.then()
    .statusCode(200);
```

---

## DELETE‑Test

```java
when()
    .delete("/tasklists/1")
.then()
    .statusCode(204);
```

---

## Assertions (Status, Felder, Werte)

```java
.then()
    .statusCode(200)
    .body("id", notNullValue())
    .body("name", equalTo("My List"));
```

---

## JSON‑Body‑Validierung

```java
.then()
    .body("tasks.size()", greaterThan(0))
    .body("tasks[0].title", equalTo("Buy milk"));
```

---

## Kernkonzept ⭐⭐⭐⭐

### **given()**
- Body
- Headers
- Content‑Type
- Auth
- Query‑Params
- Path‑Params

### **when()**
- GET
- POST
- PUT
- PATCH
- DELETE

### **then()**
- Statuscodes
- JSON‑Felder
- Datentypen
- Werte
- Fehlerfälle

---

Damit hat man:

- **eine generische REST‑Assured‑Einführung**
- **danach projektspezifischen Regressionstests**
- **saubere Trennung**
- **maximale Wiederverwendbarkeit**
