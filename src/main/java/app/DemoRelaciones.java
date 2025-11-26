package app;
// Paquete donde vive la clase de arranque de la aplicación.
// Suele contener clases "main", demos, lanzadores, etc.

import dao.ClienteDAO;
// Importamos el DAO de Cliente, responsable de hablar con la base de datos
// (INSERT, SELECT, UPDATE, DELETE de la tabla 'cliente').

import model.*;
// Importamos las clases de modelo (entidades): Cliente, Pedido, Producto, etc.
// Con el asterisco importamos todas las clases del paquete model.

import java.sql.SQLException;
// Excepción que lanza JDBC cuando ocurre un problema con la base de datos
// (sentencia mal escrita, conexión caída, constraint violada, etc.).

import java.time.LocalDate;
// Clase para manejar fechas (se usará si creamos pedidos con fecha, por ejemplo).

import java.util.List;
// Interfaz List para manejar colecciones de clientes, pedidos, etc.

/**
 * Clase de demostración para explicar relaciones entre entidades.
 *
 * El comentario indica la intención del ejemplo:
 *  - 1:1  Cliente ↔ DetalleCliente
 *  - 1:N  Cliente ↔ Pedido
 *  - N:M  Pedido ↔ Producto (vía DetallePedido)
 *
 * Aunque en este código solo estás trabajando con Cliente,
 * la idea es extender la demo para incluir el resto de relaciones.
 */
public class DemoRelaciones {

    // Método main: punto de entrada de la aplicación Java.
    public static void main(String[] args) {

        try {
            // Creamos una instancia del DAO de Cliente.
            // A través de este objeto realizaremos las operaciones sobre la tabla 'cliente'.
            ClienteDAO clienteDAO = new ClienteDAO();

            // 1) Cargar datos de ejemplo en la base de datos.
            //    Se insertan algunos clientes usando el DAO.
            cargaDeDatos(clienteDAO);

            // 2) Consultar y mostrar los datos que acabamos de insertar.
            mostrarDatos(clienteDAO);

        } catch (SQLException e) {
            // Cualquier operación de BD puede lanzar SQLException.
            // Al capturarla aquí, evitamos que el programa termine de forma abrupta
            // y al menos vemos la traza del error en consola.
            e.printStackTrace();
        }
    }

    /**
     * Método auxiliar que se encarga de insertar datos de ejemplo en la BD.
     *
     * @param clienteDAO Objeto DAO que sabe cómo hablar con la tabla 'cliente'.
     * @throws SQLException Si ocurre un error al insertar en la BD.
     */
    private static void cargaDeDatos(ClienteDAO clienteDAO) throws SQLException {

        System.out.println("=== Cargando datos ===");

        // Creamos dos instancias de Cliente en memoria, con id, nombre y email.
        // Cada objeto representa una futura fila de la tabla 'cliente'.
        Cliente c1 = new Cliente(1, "Roberto Rodríguez", "robert@rodri.com");
        Cliente c2 = new Cliente(2, "Andrea Valenti", "andrea@valenti.com");

        // Insertamos los clientes en la base de datos usando el DAO.
        // Si alguna de estas operaciones falla (por ejemplo, clave duplicada),
        // se lanzará una SQLException que será propagada al método main.
        clienteDAO.insert(c1);
        clienteDAO.insert(c2);

        System.out.println("=== DATOS CARGADOS CORRECTAMENTE ===");
    }

    /**
     * Método auxiliar que muestra por pantalla los clientes almacenados en la BD.
     *
     * @param clienteDAO DAO para acceder a la tabla de clientes.
     * @throws SQLException Si ocurre un error al consultar la BD.
     */
    private static void mostrarDatos(ClienteDAO clienteDAO) throws SQLException {

        System.out.println("=== CLIENTES ===");

        // Pedimos al DAO todos los clientes.
        // El método findAll() consulta la tabla 'cliente' y devuelve una lista de objetos Cliente.
        List<Cliente> clientes = clienteDAO.findAll();

        // Recorremos la lista y mostramos cada cliente.
        // System.out::println es una referencia a método: por cada cliente de la lista
        // se llamará a System.out.println(cliente).
        // Para que el resultado sea legible, la clase Cliente debería tener un toString() bien definido.
        clientes.forEach(System.out::println);
    }

}
