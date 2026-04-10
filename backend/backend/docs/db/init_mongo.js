// =============================================
// E-COMMERCE DATABASE SCHEMA (MongoDB)
// Autor: Jhoan Duque
// Propósito: Gestión de Inventario
// =============================================

// 1. Cambiar a la base de datos de inventario
use ecommerce_db_inventory;

// 2. Crear la colección de inventarios (se crea automáticamente al insertar, pero definimos los índices)
db.createCollection("inventarios");

// 3. Crear índices para optimizar búsquedas y asegurar integridad
// Índice único para evitar duplicar inventario de un mismo producto
db.inventarios.createIndex(
    { "productoId": 1 },
    { unique: true, name: "idx_producto_id_unique" }
);

// Índice para búsquedas rápidas por cantidad (ej: stock bajo)
db.inventarios.createIndex(
    { "cantidad": 1 },
    { name: "idx_cantidad_asc" }
);

// Índice para búsquedas por ubicación
db.inventarios.createIndex(
    { "ubicacionBodega": 1 },
    { name: "idx_ubicacion_text" }
);

print("Configuración de MongoDB completada correctamente.");
