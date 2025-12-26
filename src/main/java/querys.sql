
-- CLIENTE (1)
CREATE TABLE IF NOT EXISTS cliente (
  id        INT PRIMARY KEY,
  nombre    VARCHAR(120) NOT NULL,
  email     VARCHAR(200) NOT NULL UNIQUE
);

-- DETALLE_CLIENTE (1:1)  → FK única a cliente
CREATE TABLE IF NOT EXISTS detalle_cliente (
  id          INT PRIMARY KEY,                  -- misma PK que cliente.id (opción clásica 1:1)
  direccion   VARCHAR(200),
  telefono    VARCHAR(40),
  notas       VARCHAR(200),
  CONSTRAINT fk_det_cliente FOREIGN KEY (id) REFERENCES cliente(id) ON DELETE CASCADE
);

-- PRODUCTO
CREATE TABLE IF NOT EXISTS producto (
  id      INT PRIMARY KEY,
  nombre  VARCHAR(120) NOT NULL,
  precio  NUMERIC(12,2) NOT NULL CHECK (precio >= 0)
);

-- PEDIDO (N de 1:N con Cliente)
CREATE TABLE IF NOT EXISTS pedido (
  id          INT PRIMARY KEY,
  cliente_id  INT NOT NULL,
  fecha       DATE NOT NULL,
  CONSTRAINT fk_pedido_cliente FOREIGN KEY (cliente_id)
    REFERENCES cliente(id) ON DELETE RESTRICT
);

-- DETALLE_PEDIDO (tabla intermedia N:M)
CREATE TABLE IF NOT EXISTS detalle_pedido (
  pedido_id    INT NOT NULL,
  producto_id  INT NOT NULL,
  cantidad     INT NOT NULL CHECK (cantidad > 0),
  precio_unit  NUMERIC(12,2) NOT NULL CHECK (precio_unit >= 0),
  PRIMARY KEY (pedido_id, producto_id),
  CONSTRAINT fk_dp_pedido   FOREIGN KEY (pedido_id)   REFERENCES pedido(id)   ON DELETE CASCADE,
  CONSTRAINT fk_dp_producto FOREIGN KEY (producto_id) REFERENCES producto(id) ON DELETE RESTRICT
);


--Nueva tabla comercial
CREATE TABLE comercial (
    id_comercial INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50),
    apellidos VARCHAR(80),
    telefono VARCHAR(15),
    zona VARCHAR(50)
);


--Aqui la referencio con clientes
--Un comercial puede tener muchos clientes
ALTER TABLE cliente
ADD id_comercial INT,
ADD FOREIGN KEY (id_comercial) REFERENCES comercial(id);


--Nueva tabla repartidor
CREATE TABLE repartidor (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50),
    apellidos VARCHAR(80),
    telefono VARCHAR(15),
    vehiculo VARCHAR(50),

);
--Aqui la referencia con pedidos
--Un repartidor reparte michos pedidos

ALTER TABLE pedido
ADD id_repartidor INT,
ADD FOREIGN KEY (id_repartidor) REFERENCES repartidor(id);



