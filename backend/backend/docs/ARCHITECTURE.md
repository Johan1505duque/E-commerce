# Arquitectura y Funcionalidad del Backend - E-Commerce

Este documento describe la arquitectura, el flujo de datos y las funcionalidades implementadas hasta el momento en el backend del proyecto E-Commerce.

## 1. Arquitectura del Sistema

El proyecto sigue una **Arquitectura en Capas** estándar de Spring Boot, lo que facilita el mantenimiento, las pruebas y la escalabilidad.

### Capas de la Aplicación:
1.  **Controladores (REST Controllers):** Exponen los endpoints de la API y gestionan las peticiones HTTP.
2.  **Servicios (Service Layer):** Contienen la lógica de negocio. Se utiliza el patrón `Service/ServiceImpl` para desacoplar la interfaz de la implementación.
3.  **Repositorios (Repository Layer):** Gestionan el acceso a los datos utilizando Spring Data.
4.  **Modelos/Entidades (Entities/Documents):** Representan las estructuras de datos en las bases de datos.
5.  **DTOs (Data Transfer Objects):** Objetos utilizados para transferir datos entre la API y la capa de servicio, evitando exponer directamente las entidades de la base de datos.
6.  **Mappers:** Componentes encargados de la conversión entre Entidades y DTOs.

---

## 2. Estrategia de Persistencia Híbrida

Una de las características clave de este backend es su **arquitectura de base de datos políglota**, eligiendo la mejor tecnología para cada necesidad:

### PostgreSQL (Relacional)
Se utiliza para datos que requieren integridad referencial estricta y transacciones complejas.
*   **Usuarios:** Gestión de cuentas, roles y seguridad.
*   **Productos:** Catálogo maestro de productos.
*   **Órdenes:** Historial de compras y relación cliente-producto.
*   **Pagos:** Registro de transacciones financieras.

### MongoDB (NoSQL de Documentos)
Se utiliza para datos que requieren alta disponibilidad y flexibilidad.
*   **Inventario:** Gestión de stock y ubicaciones en bodega. Permite escalar horizontalmente y manejar atributos variables de inventario de forma eficiente.

---

## 3. Flujo de Datos (Arquitectura de Flujo)

El flujo típico de una petición (por ejemplo, consultar inventario) es el siguiente:

1.  **Cliente (Frontend/Postman):** Envía una petición `GET /api/inventarios/producto/{id}`.
2.  **Controller:** Recibe la petición, extrae los parámetros y llama al método correspondiente en el `Service`.
3.  **Service:**
    *   Valida la lógica de negocio.
    *   Interactúa con el `InventarioRepository` (MongoDB).
    *   Si es necesario (ej. al crear), valida la existencia del producto en el `ProductoRepository` (PostgreSQL).
4.  **Repository:** Realiza la consulta a la base de datos correspondiente.
5.  **Mapper:** El `Service` utiliza el `Mapper` para convertir el documento de MongoDB a un `InventarioDTO`.
6.  **Controller:** Devuelve el `InventarioDTO` como una respuesta JSON con el código de estado HTTP adecuado.

---

## 4. Funcionalidades Implementadas

### Gestión de Usuarios (PostgreSQL)
*   Registro y mantenimiento de usuarios.
*   Roles diferenciados (ADMIN, CUSTOMER).

### Catálogo de Productos (PostgreSQL)
*   CRUD completo de productos.
*   Gestión de precios y descripciones.

### Gestión de Inventario (MongoDB)
*   **Sincronización:** Vinculación de stock con productos de PostgreSQL mediante `productoId`.
*   **Control de Stock:** Operaciones para actualizar cantidades y validar disponibilidad.
*   **Alertas:** Funcionalidad para listar productos con stock bajo.

### Órdenes y Pagos (PostgreSQL)
*   Creación de órdenes de compra.
*   Integración (simulada) con pasarela de pagos.

---

## 5. Tecnologías Utilizadas
*   **Java 17** & **Spring Boot 3.x**
*   **Spring Data JPA** & **Spring Data MongoDB**
*   **PostgreSQL** & **MongoDB**
*   **Lombok:** Para reducir el código boilerplate.
*   **MapStruct/Custom Mappers:** Para transformación de datos.
*   **Swagger/OpenAPI:** Para documentación interactiva de la API.
