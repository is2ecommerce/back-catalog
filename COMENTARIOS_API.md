# API de Comentarios y Calificaciones

## Endpoints implementados

### 1. Agregar comentario a producto
**POST** `/api/productos/{id}/comentarios`

Agrega un comentario con calificación a un producto. La calificación promedio del producto se recalcula automáticamente.

**Body (JSON):**
```json
{
  "autor": "Juan Pérez",
  "texto": "Excelente producto, muy buena calidad",
  "calificacion": 4.5
}
```

**Validaciones:**
- `calificacion`: debe estar entre 1.0 y 5.0
- `texto`: obligatorio, no puede estar vacío
- `autor`: obligatorio, no puede estar vacío
- `fecha`: se asigna automáticamente (LocalDateTime.now())

**Respuesta exitosa (201 Created):**
```json
{
  "id": "507f1f77bcf86cd799439011",
  "nombre": "Laptop Dell",
  "descripcion": "...",
  "precio": 1200.50,
  "calificacion": 4.3,
  "comentarios": [
    {
      "autor": "Juan Pérez",
      "texto": "Excelente producto, muy buena calidad",
      "calificacion": 4.5,
      "fecha": "2025-11-06T14:30:00"
    }
  ],
  ...
}
```

**Ejemplo con curl (PowerShell):**
```powershell
$body = @{
    autor = "Juan Pérez"
    texto = "Excelente producto, muy buena calidad"
    calificacion = 4.5
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/productos/507f1f77bcf86cd799439011/comentarios" -Method POST -Body $body -ContentType "application/json"
```

---

### 2. Obtener comentarios de un producto
**GET** `/api/productos/{id}/comentarios`

Devuelve todos los comentarios de un producto.

**Respuesta exitosa (200 OK):**
```json
[
  {
    "autor": "María García",
    "texto": "Buena relación calidad-precio",
    "calificacion": 4.0,
    "fecha": "2025-11-05T10:15:00"
  },
  {
    "autor": "Juan Pérez",
    "texto": "Excelente producto, muy buena calidad",
    "calificacion": 4.5,
    "fecha": "2025-11-06T14:30:00"
  }
]
```

**Ejemplo con curl (PowerShell):**
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/productos/507f1f77bcf86cd799439011/comentarios" -Method GET
```

---

### 3. Actualizar calificación manualmente
**PUT** `/api/productos/{id}/calificacion?calificacion={valor}`

Actualiza manualmente la calificación de un producto sin agregar un comentario.

**Parámetros:**
- `calificacion` (query param): valor entre 1.0 y 5.0

**Respuesta exitosa (200 OK):**
```json
{
  "id": "507f1f77bcf86cd799439011",
  "nombre": "Laptop Dell",
  "calificacion": 4.7,
  ...
}
```

**Ejemplo con curl (PowerShell):**
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/productos/507f1f77bcf86cd799439011/calificacion?calificacion=4.7" -Method PUT
```

---

## Cálculo automático de calificación

Cuando agregas un comentario con `POST /{id}/comentarios`, el sistema:

1. Valida que la calificación esté entre 1.0 y 5.0
2. Agrega el comentario a la lista del producto
3. **Recalcula automáticamente** la calificación del producto como el promedio de todas las calificaciones de los comentarios
4. Redondea el resultado a 1 decimal

**Ejemplo:**
- Comentario 1: calificación 4.0
- Comentario 2: calificación 5.0
- Comentario 3: calificación 4.5
- **Calificación del producto: 4.5** (promedio redondeado)

---

## Cambios en el modelo

### Antes:
```java
private List<String> comentarios; // Solo texto
```

### Ahora:
```java
private List<Comentario> comentarios; // Objeto completo con autor, texto, calificación y fecha
```

### Estructura del modelo Comentario:
```java
public class Comentario {
    private String autor;
    private String texto;
    private Double calificacion; // 1.0 a 5.0
    private LocalDateTime fecha;
}
```

---

## Errores comunes

### 400 Bad Request
- Calificación fuera del rango 1.0 - 5.0
- Texto o autor vacíos

### 404 Not Found
- Producto no encontrado con el ID especificado

### 500 Internal Server Error
- Error al guardar en la base de datos

---

## Probar en Swagger

1. Inicia la aplicación
2. Abre http://localhost:8080/swagger-ui.html
3. Busca la sección **"Productos"**
4. Encontrarás los endpoints:
   - `POST /api/productos/{id}/comentarios`
   - `GET /api/productos/{id}/comentarios`
   - `PUT /api/productos/{id}/calificacion`
