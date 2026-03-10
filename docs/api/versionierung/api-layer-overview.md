[//]: # (docs/api/versionierung/api-layer-overview.md)
### **API‑Schicht – Designüberblick**

> Die API‑Schicht ist so aufgebaut, dass sie klar von Domain, Application und Infrastruktur getrennt bleibt. Sie bildet ausschließlich den öffentlichen API‑Vertrag ab und ist vollständig versionierbar.

---

### **Contract‑Bereich**

- **annotations/**  
  Enthält wiederverwendbare API‑Annotationen für Swagger/OpenAPI.  
  Sie sind technisch, nicht fachlich.

- **dto/request/**  
  Request‑Modelle für die API.  
  Sie sind versionierbar und gehören nicht in Domain oder Application.

- **dto/request/validation/**  
  Custom‑Validation für API‑Requests.  
  Dient der besseren Darstellung in Swagger und bleibt API‑spezifisch.

- **dto/response/**  
  Response‑Modelle der API.  
  Rein technische Output‑Strukturen.

- **responses/**  
  API‑Response‑Infrastruktur (Error‑Modelle, Envelopes, Status‑Objekte).

- **responses/swagger/**  
  Wrapper für Swagger, da Generics nicht korrekt dargestellt werden.

- **utils/**  
  Kleine technische Helfer für API‑Antworten.

- **Interfaces (IApiPrefix, ITaskListsCrudApi, …)**  
  Definieren den API‑Vertrag.  
  Keine Implementierungsdetails.

---

### **Implementierungs‑Bereich**

- **impl/**  
  Enthält die Controller‑Implementierungen.  
  Keine Swagger‑Annotationen, keine DTO‑Definitionen.  
  Rein technische Umsetzung des API‑Vertrags.

---

### **Warum diese Struktur sinnvoll ist**

- API‑Definition ist klar von der Implementierung getrennt.
- DTOs sind sauber nach Request/Response organisiert.
- Swagger‑Wrapper sind isoliert und verschmutzen die API nicht.
- Validation bleibt API‑spezifisch und berührt die Domain nicht.
- Die API ist vollständig versionierbar (`v1`, `v2`, …).
- Domain und Application bleiben stabil, auch wenn die API wächst.