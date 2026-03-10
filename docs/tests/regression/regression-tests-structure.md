# 📄 regression-tests-structure.md
**(kompakt, nur das Wesentliche, perfekt für schnelle Orientierung)**

## Struktur der API‑Regressionstests

Die Tests sind nach API‑Bereichen gruppiert und spiegeln exakt die API‑Interfaces wider:

- **ITaskListsCrudApi**
- **ITaskListsTasksCrudApi**
- **ITaskListsUseCaseApi**

Jeder Bereich besitzt:
- Happy‑Path‑Tests
- Validation‑Tests
- NotFound‑Tests
- MethodNotAllowed‑Tests
- Eine eigene TestSuite

---

## Verzeichnisstruktur (aus `scripts/list_api_tests.sh`)

```
src/test/java/edu/yacoubi/tasks/api/
  TaskApiFullTestSuite.java
  base/
    TaskApiRestAssuredTestBase.java

  tasklists/
    crud/
      TaskListsCrudControllerHappyPathTest.java
      TaskListsCrudControllerMethodNotAllowedTest.java
      TaskListsCrudControllerNotFoundTest.java
      TaskListsCrudControllerValidationTest.java
      TaskListsCrudTestSuite.java

    tasks/crud/
      TaskListsTasksCrudControllerHappyPathTest.java
      TaskListsTasksCrudControllerMethodNotAllowedTest.java
      TaskListsTasksCrudControllerNotFoundTest.java
      TaskListsTasksCrudControllerValidationTest.java
      TaskListsTasksCrudTestSuite.java

    usecase/
      TaskListsTasksUseCaseControllerDomainErrorTest.java
      TaskListsUseCaseControllerHappyPathTest.java
      TaskListsUseCaseControllerTechnicalErrorTest.java
      TaskListsUseCaseTestSuite.java
```

---

## Testgruppen (kompakt)

### **TaskLists CRUD**
```
TaskListsCrudControllerHappyPathTest
TaskListsCrudControllerValidationTest
TaskListsCrudControllerNotFoundTest
TaskListsCrudControllerMethodNotAllowedTest
TaskListsCrudTestSuite
```

### **TaskLists → Tasks CRUD**
```
TaskListsTasksCrudControllerHappyPathTest
TaskListsTasksCrudControllerValidationTest
TaskListsTasksCrudControllerNotFoundTest
TaskListsTasksCrudControllerMethodNotAllowedTest
TaskListsTasksCrudTestSuite
```

### **TaskLists UseCases**
```
TaskListsUseCaseControllerHappyPathTest
TaskListsTasksUseCaseControllerDomainErrorTest
TaskListsUseCaseControllerTechnicalErrorTest
TaskListsUseCaseTestSuite
```

### **Gesamtsuite**
```
TaskApiFullTestSuite
```

---

## Ausführung (kompakt)

### Einzeltest
```
mvn -Dtest=TaskListsCrudControllerHappyPathTest test
```

### Bereichssuite
```
mvn -Dtest=TaskListsCrudTestSuite test
```

### Gesamte API‑Suite
```
mvn -Dtest=TaskApiFullTestSuite test
```

### Voraussetzung
- App muss laufen (`local-dev`, H2, Port 8080)

---

## Warum diese Struktur ideal ist
- 1:1 Spiegelung der API‑Interfaces
- klare Trennung CRUD / Tasks / UseCases
- jede Suite kann separat laufen
- perfekt für spätere CI‑Automatisierung
- extrem leicht erweiterbar
