// =============================================
// E-COMMERCE DATABASE SCHEMA (MongoDB)
// Autor: Jhoan Duque
// Propósito: Gestión de Inventario y Carrito de Compras
// =============================================

// 1. Cambiar a la base de datos de inventario y carrito
use ecommerce_db_inventory;

// --- Configuración para la colección 'inventarios' ---
// 2. Crear la colección 'inventarios' (se crea automáticamente al insertar, pero definimos los índices)
db.createCollection("inventarios");

// 3. Crear índices para optimizar búsquedas y asegurar integridad
// Índice único para evitar duplicar inventario de un mismo producto
db.inventarios.createIndex(
    { "productoId": 1 },
    { unique: true, name: "idx_inventario_producto_id_unique" }
);

// Índice para búsquedas rápidas por cantidad (ej: stock bajo)
db.inventarios.createIndex(
    { "cantidad": 1 },
    { name: "idx_inventario_cantidad_asc" }
);

// Índice para búsquedas por ubicación
db.inventarios.createIndex(
    { "ubicacionBodega": 1 },
    { name: "idx_inventario_ubicacion_text" }
);

// --- Configuración para la colección 'carritos' ---
// 4. Crear la colección 'carritos'
db.createCollection("carritos");

// 5. Crear índice único para asegurar que cada usuario tenga solo un carrito
db.carritos.createIndex(
    { "usuarioId": 1 },
    { unique: true, name: "idx_carrito_usuario_id_unique" }
);

// 6. Crear índice para búsquedas por items.productoId dentro de los items del carrito (opcional, si se busca mucho por producto dentro del carrito)
db.carritos.createIndex(
    { "items.productoId": 1 },
    { name: "idx_carrito_items_producto_id" }
);


print("Configuración de MongoDB completada correctamente para inventario y carritos.");
