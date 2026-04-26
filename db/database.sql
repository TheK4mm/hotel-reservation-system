CREATE TABLE tipos_habitacion (
    id                  BIGSERIAL       PRIMARY KEY,
    nombre              VARCHAR(80)     NOT NULL UNIQUE,
    descripcion         VARCHAR(500),
    capacidad_maxima    INTEGER NOT     NULL CHECK (capacidad_maxima > 0),
    precio_base_noche   NUMERIC(10,2)   NOT NULL CHECK (precio_base_noche > 0),
    activo              BOOLEAN NOT     NULL DEFAULT TRUE
);

CREATE TABLE habitaciones (
    id BIGSERIAL PRIMARY KEY,
    numero              VARCHAR(10)     NOT NULL UNIQUE,
    piso                INTEGER         NOT NULL CHECK (piso > 0),
    descripcion         VARCHAR(500),
    estado              VARCHAR(20)     NOT NULL DEFAULT 'DISPONIBLE'
    CHECK (estado IN ('DISPONIBLE','OCUPADA','MANTENIMIENTO','BLOQUEADA')),
    activo              BOOLEAN         NOT NULL DEFAULT TRUE,
    tipo_habitacion_id  BIGINT          NOT NULL
                        REFERENCES tipos_habitacion(id) ON DELETE RESTRICT
);

CREATE INDEX idx_habitacion_estado      ON habitaciones(estado);
CREATE INDEX idx_habitacion_tipo        ON habitaciones(tipo_habitacion_id);

CREATE TABLE clientes (
    id                  BIGSERIAL       PRIMARY KEY,
    nombre              VARCHAR(100)    NOT NULL,
    apellido            VARCHAR(100)    NOT NULL,
    tipo_documento      VARCHAR(20)     NOT NULL,
    numero_documento    VARCHAR(30)     NOT NULL UNIQUE,
    email               VARCHAR(150)    NOT NULL UNIQUE,
    telefono            VARCHAR(20),
    direccion           VARCHAR(300),
    pais                VARCHAR(80),
    activo              BOOLEAN         NOT NULL DEFAULT TRUE,
    fecha_registro      TIMESTAMP       NOT NULL DEFAULT NOW(),
    fecha_actualizacion TIMESTAMP
);

CREATE INDEX idx_cliente_documento  ON clientes(numero_documento);
CREATE INDEX idx_cliente_email      ON clientes(email);

CREATE TABLE empleados (
    id                  BIGSERIAL       PRIMARY KEY,
    nombre              VARCHAR(100)    NOT NULL,
    apellido            VARCHAR(100)    NOT NULL,
    numero_documento    VARCHAR(30)     NOT NULL UNIQUE,
    cargo               VARCHAR(80)     NOT NULL,
    email_corporativo   VARCHAR(150),
    telefono_extension  VARCHAR(10),
    fecha_contratacion  DATE            NOT NULL,
    activo              BOOLEAN         NOT NULL DEFAULT TRUE,
    fecha_registro      TIMESTAMP       NOT NULL DEFAULT NOW()
);

CREATE TABLE servicios (
    id          BIGSERIAL       PRIMARY KEY,
    nombre      VARCHAR(100)    NOT NULL UNIQUE,
    descripcion VARCHAR(500),
    precio      NUMERIC(10,2)   NOT NULL CHECK (precio >= 0),
    categoria   VARCHAR(30)     NOT NULL
                    CHECK (categoria IN ('ALIMENTACION','SPA_BIENESTAR','TRANSPORTE',
                                        'ENTRETENIMIENTO','LAVANDERIA','NEGOCIOS','OTROS')),
    activo      BOOLEAN         NOT NULL DEFAULT TRUE
);

CREATE TABLE reservas (
    id                  BIGSERIAL       PRIMARY KEY,
    codigo_reserva      VARCHAR(20)     NOT NULL UNIQUE,
    fecha_entrada       DATE            NOT NULL,
    fecha_salida        DATE            NOT NULL,
    numero_huespedes    INTEGER         NOT NULL CHECK (numero_huespedes > 0),
    total_noches        INTEGER         NOT NULL CHECK (total_noches > 0),
    precio_total        NUMERIC(12,2)   NOT NULL CHECK (precio_total >= 0),
    observaciones       VARCHAR(500),
    estado              VARCHAR(20)     NOT NULL DEFAULT 'PENDIENTE'
                            CHECK (estado IN ('PENDIENTE','CONFIRMADA','CHECKIN',
                                              'CHECKOUT','CANCELADA','NO_SHOW')),
    fecha_creacion      TIMESTAMP       NOT NULL DEFAULT NOW(),
    fecha_actualizacion TIMESTAMP,
    cliente_id          BIGINT          NOT NULL
                            REFERENCES clientes(id) ON DELETE RESTRICT,
    empleado_id         BIGINT
                            REFERENCES empleados(id) ON DELETE SET NULL,
    CONSTRAINT chk_fechas CHECK (fecha_salida > fecha_entrada)
);

CREATE INDEX idx_reserva_cliente  ON reservas(cliente_id);
CREATE INDEX idx_reserva_estado   ON reservas(estado);
CREATE INDEX idx_reserva_entrada  ON reservas(fecha_entrada);

CREATE TABLE reserva_habitacion (
    reserva_id      BIGINT  NOT NULL REFERENCES reservas(id)     ON DELETE CASCADE,
    habitacion_id   BIGINT  NOT NULL REFERENCES habitaciones(id) ON DELETE RESTRICT,
    PRIMARY KEY (reserva_id, habitacion_id)
);

CREATE TABLE reserva_servicio (
    reserva_id  BIGINT  NOT NULL REFERENCES reservas(id)  ON DELETE CASCADE,
    servicio_id BIGINT  NOT NULL REFERENCES servicios(id) ON DELETE RESTRICT,
    PRIMARY KEY (reserva_id, servicio_id)
);

CREATE TABLE facturas (
    id                  BIGSERIAL       PRIMARY KEY,
    numero_factura      VARCHAR(30)     NOT NULL UNIQUE,
    subtotal            NUMERIC(12,2)   NOT NULL,
    impuesto_porcentaje NUMERIC(5,2)    NOT NULL DEFAULT 19.00,
    impuesto_valor      NUMERIC(12,2)   NOT NULL,
    descuento           NUMERIC(12,2)   NOT NULL DEFAULT 0.00,
    total               NUMERIC(12,2)   NOT NULL,
    notas               VARCHAR(500),
    estado_factura      VARCHAR(20)     NOT NULL DEFAULT 'PENDIENTE'
                            CHECK (estado_factura IN ('PENDIENTE','PAGADA_PARCIALMENTE','PAGADA','ANULADA')),
    fecha_emision       TIMESTAMP       NOT NULL DEFAULT NOW(),
    reserva_id          BIGINT          NOT NULL UNIQUE
                            REFERENCES reservas(id) ON DELETE RESTRICT
);

CREATE TABLE pagos (
    id                      BIGSERIAL       PRIMARY KEY,
    monto                   NUMERIC(12,2)   NOT NULL CHECK (monto > 0),
    metodo_pago             VARCHAR(20)     NOT NULL
                                CHECK (metodo_pago IN ('EFECTIVO','TARJETA_CREDITO','TARJETA_DEBITO',
                                                       'TRANSFERENCIA','PSE','NEQUI','DAVIPLATA')),
    referencia_transaccion  VARCHAR(100),
    estado_pago             VARCHAR(15)     NOT NULL DEFAULT 'PENDIENTE'
                                CHECK (estado_pago IN ('PENDIENTE','APROBADO','RECHAZADO','REEMBOLSADO','CANCELADO')),
    descripcion             VARCHAR(300),
    fecha_pago              TIMESTAMP       NOT NULL DEFAULT NOW(),
    reserva_id              BIGINT          NOT NULL REFERENCES reservas(id)  ON DELETE RESTRICT,
    factura_id              BIGINT              REFERENCES facturas(id) ON DELETE SET NULL
);

CREATE INDEX idx_pago_reserva  ON pagos(reserva_id);
CREATE INDEX idx_pago_factura  ON pagos(factura_id);


-- DATOS DE EJEMPLO

INSERT INTO tipos_habitacion (nombre, descripcion, capacidad_maxima, precio_base_noche) VALUES
  ('Individual Estándar',   'Habitación individual con cama sencilla, baño privado y vista al jardín', 1, 150000.00),
  ('Doble Estándar',        'Habitación doble con cama queen, baño privado y Smart TV 42"',            2, 220000.00),
  ('Junior Suite',          'Suite junior con sala de estar, jacuzzi y vista a la piscina',             3, 380000.00),
  ('Suite Presidencial',    'Suite de lujo con dos habitaciones, sala, comedor y terraza panorámica',   4, 750000.00),
  ('Familiar',              'Habitación familiar con dos camas dobles y sofá cama para niños',          5, 320000.00);

INSERT INTO servicios (nombre, descripcion, precio, categoria) VALUES
  ('Desayuno Buffet',       'Desayuno buffet completo con productos frescos de la región', 35000.00,  'ALIMENTACION'),
  ('Cena Romántica',        'Cena de 3 tiempos en el restaurante con decoración especial',  120000.00, 'ALIMENTACION'),
  ('Servicio de Spa',       'Masaje relajante de 60 minutos con aromaterapia',              85000.00,  'SPA_BIENESTAR'),
  ('Transfer Aeropuerto',   'Transporte privado aeropuerto-hotel-aeropuerto',               90000.00,  'TRANSPORTE'),
  ('Lavandería Express',    'Servicio de lavandería con entrega en 4 horas',                45000.00,  'LAVANDERIA'),
  ('Tour Ciudad',           'Tour guiado por los principales atractivos de la ciudad',      65000.00,  'ENTRETENIMIENTO'),
  ('Sala de Juntas',        'Alquiler de sala de juntas por 4 horas con equipos AV',       180000.00, 'NEGOCIOS');

INSERT INTO empleados (nombre, apellido, numero_documento, cargo, email_corporativo, fecha_contratacion) VALUES
  ('María',  'González', '52445123', 'Recepcionista',    'mgonzalez@hotelparaiso.com',  '2022-03-15'),
  ('Carlos', 'Ramírez',  '80123456', 'Gerente Reservas', 'cramirez@hotelparaiso.com',   '2020-01-10'),
  ('Luisa',  'Martínez', '43789012', 'Recepcionista',    'lmartinez@hotelparaiso.com',  '2023-06-01');

INSERT INTO habitaciones (numero, piso, descripcion, tipo_habitacion_id) VALUES
  ('101', 1, 'Vista al jardín oriental',              1),
  ('102', 1, 'Vista a la piscina',                    1),
  ('201', 2, 'Cama queen con escritorio ejecutivo',   2),
  ('202', 2, 'Cama king, vista a la montaña',         2),
  ('301', 3, 'Jacuzzi y balcón privado',              3),
  ('401', 4, 'Suite completa, terraza panorámica',    4),
  ('203', 2, 'Habitación familiar, área de juegos',   5);
