# LampreasViolet

## Descripción del proyecto
LampreasViolet es un proyecto desarrollado en Java orientado a la gestión de clientes y pedidos. 
A lo largo del desarrollo se ampliaron las funcionalidades del sistema, se mejoró el modelo de datos 
y se reforzó la validación de entradas, priorizando la integridad de la información y la mantenibilidad del código.

Durante el proyecto se decidió **cambiar el motor de base de datos**, optando por **MySQL** en lugar de PostgreSQL.

---

## Cambios y funcionalidades implementadas

### 1. Nuevos modelos creados
Se incorporaron dos nuevos modelos al sistema:

#### Repartidor
Entidad encargada de la entrega de pedidos, con los siguientes atributos:
- `id` (Integer)
- `nombre` (String)
- `apellidos` (String)
- `telefono` (String)
- `vehiculo` (String)

#### Comercial
Entidad responsable de la gestión comercial de los clientes, con los atributos:
- `id` (Integer)
- `nombre` (String)
- `apellidos` (String)
- `telefono` (String)
- `zona` (String)

Para ambos modelos se desarrollaron sus respectivos **DAO**, permitiendo realizar operaciones de acceso a datos 
de forma estructurada y desacoplada.

---

### 2. Modificaciones en la base de datos
Con el objetivo de adaptar toda la aplicación a las nuevas funcionalidades creadas, 
se realizaron modificaciones en la estructura de la base de datos, permitiendo establecer 
relaciones entre las nuevas entidades y las ya existentes.

Se modificó la **tabla `cliente`** para poder referenciarla con la tabla **`comercial`**, 
de manera que cada cliente pueda estar asociado a un comercial específico.  
Asimismo, se modificó la **tabla `pedido`** para poder referenciarla con la tabla **`repartidor`**, 
permitiendo asignar un repartidor responsable a cada pedido.

Estas modificaciones aseguran que toda la aplicación pueda adaptarse correctamente a los nuevos modelos
y funcionalidades incorporadas.

### 3. Nuevas funcionalidades
Se añadieron nuevas funcionalidades al sistema con el objetivo de mejorar la gestión y mantenimiento de la información. Estas funcionalidades permiten:

- Eliminar registros de la tabla `detalle_pedido`
- Eliminar registros de la tabla `detalle_cliente`

---

### 4. Validación de entradas del usuario
Se creó un nuevo paquete llamado `utils`, dentro del cual se desarrolló la clase `InputUtils`. 
Esta clase centraliza la validación de los datos introducidos por el usuario, evitando errores comunes
y mejorando la experiencia de uso de la aplicación.

La clase `InputUtils` permite validar:
- Cadenas de texto no vacías
- Números enteros
- Números decimales
- Fechas en formato `YYYY-MM-DD`
- Correos electrónicos mediante expresiones regulares



