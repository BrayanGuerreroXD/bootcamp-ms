# Pendiente: Bootcamp MS Refactoring

## PARTE #3 - Publicación de evento sync.bootcamp.capacity.match

### Pendiente
1. **CreateBootcampUseCase.java** - Publicar evento post-persist de forma asíncrona
2. **UpdateBootcampUseCase.java** - Publicar evento post-persist de forma asíncrona

### Detalle del cambio

En ambos use cases, después de persistir las capacidades del bootcamp:

```java
// Extraer capacityIds de bootcamp.getCapacities()
List<Long> capacityIds = bootcamp.getCapacities().stream()
    .map(CapacityCatalog::getId)
    .toList();

// Construir y publicar evento asíncronamente
BootcampCapacityMatchEvent event = BootcampCapacityMatchEvent.builder()
    .bootcampId(saved.getId())
    .capacityIds(capacityIds)
    .build();

eventGateway.publishBootcampCapacityMatch(event)
    .subscribe(); // fire & forget
```

### Archivos ya creados/modificados
- `BootcampCapacityMatchEvent.java` ✅
- `EventGateway.java` (nuevo método) ✅
- `EventPublisherAdapter.java` (implementación) ✅
- `KafkaTopicsProperties.java` (nuevo campo) ✅
- `application.yml` (nuevo topic) ✅

---

## Verificación
Ejecutar `./gradlew build` para validar compilación y tests.