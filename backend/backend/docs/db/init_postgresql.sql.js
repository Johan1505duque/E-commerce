// =============================================
// E-COMMERCE DATABASE SCHEMA (PostgreSQL)
// Autor: Jhoan Duque
// NOTA: La gestión de inventario ha sido migrada a MongoDB.
// Ver archivo: init_mongo.js para la configuración de inventario.
// =============================================

/*
Este archivo contiene el script SQL para inicializar la base de datos PostgreSQL.
Puedes copiar y ejecutar este código en tu gestor de base de datos (pgAdmin, DBeaver, etc.).

CREATE DATABASE ecommerce_db;

-- USUARIOS
CREATE TABLE IF NOT EXISTS usuarios (
    id                  BIGSERIAL       PRIMARY KEY,
    nombre              VARCHAR(100)    NOT NULL,
    apellido            VARCHAR(100)    NOT NULL,
    correo_electronico  VARCHAR(150)    NOT NULL UNIQUE,
    password            VARCHAR(255)    NOT NULL,
    rol                 VARCHAR(20)     NOT NULL DEFAULT 'CUSTOMER',
    activo              BOOLEAN         NOT NULL DEFAULT TRUE,
    creacion            TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actualizacion       TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_usuarios_correo    ON usuarios(correo_electronico);
CREATE INDEX IF NOT EXISTS idx_usuarios_nombre    ON usuarios(nombre);
CREATE INDEX IF NOT EXISTS idx_usuarios_apellido  ON usuarios(apellido);
CREATE INDEX IF NOT EXISTS idx_usuarios_rol       ON usuarios(rol);
CREATE INDEX IF NOT EXISTS idx_usuarios_activo    ON usuarios(activo);

-- PRODUCTOS
CREATE TABLE IF NOT EXISTS productos (
    id              BIGSERIAL       PRIMARY KEY,
    nombre          VARCHAR(150)    NOT NULL,
    descripcion     VARCHAR(500),
    precio          DECIMAL(10,2)   NOT NULL,
    imagen_url      VARCHAR(2500),
    activo          BOOLEAN         NOT NULL DEFAULT TRUE,
    creacion        TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actualizacion   TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_productos_nombre  ON productos(nombre);
CREATE INDEX IF NOT EXISTS idx_productos_activo  ON productos(activo);

-- ORDENES
CREATE TABLE IF NOT EXISTS ordenes (
    id              BIGSERIAL       PRIMARY KEY,
    id_usuario      BIGINT          NOT NULL,
    total           DECIMAL(10,2)   NOT NULL,
    envio           DECIMAL(10,2)   NOT NULL DEFAULT 0,
    estado          VARCHAR(20)     NOT NULL DEFAULT 'PENDIENTE',
    creacion        TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actualizacion   TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_ordenes_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
        ON DELETE NO ACTION
);
CREATE INDEX IF NOT EXISTS idx_ordenes_usuario ON ordenes(id_usuario);
CREATE INDEX IF NOT EXISTS idx_ordenes_estado  ON ordenes(estado);

-- ORDEN_PRODUCTO
CREATE TABLE IF NOT EXISTS orden_producto (
    id_orden        BIGINT          NOT NULL,
    id_producto     BIGINT          NOT NULL,
    cantidad        INT             NOT NULL,
    precio_unitario DECIMAL(10,2)   NOT NULL,
    creacion        TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_orden, id_producto),
    CONSTRAINT fk_op_orden
        FOREIGN KEY (id_orden) REFERENCES ordenes(id) ON DELETE CASCADE,
    CONSTRAINT fk_op_producto
        FOREIGN KEY (id_producto) REFERENCES productos(id) ON DELETE NO ACTION
);

-- PASARELA_PAGO
CREATE TABLE IF NOT EXISTS pasarela_pago (
    id              BIGSERIAL       PRIMARY KEY,
    id_orden        BIGINT          NOT NULL UNIQUE,
    monto           DECIMAL(10,2)   NOT NULL,
    estado          VARCHAR(20)     NOT NULL DEFAULT 'PENDIENTE',
    fecha           TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    creacion        TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actualizacion   TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_pago_orden
        FOREIGN KEY (id_orden) REFERENCES ordenes(id) ON DELETE NO ACTION
);
CREATE INDEX IF NOT EXISTS idx_pago_estado ON pasarela_pago(estado);
*/
