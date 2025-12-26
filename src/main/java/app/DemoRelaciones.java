package app;

import dao.*;
import model.*;
import services.JsonIO;
import utils.InputUtils;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * Demo por consola:
 * - Permite probar rápidamente los DAO (listar, insertar, buscar por id)
 * - Exporta/importa JSON con una instantánea de todas las entidades
 *
 * Ideal para comprobar que:
 * - conexión JDBC funciona
 * - DAOs funcionan
 * - BD tiene integridad (FK)
 */
public class DemoRelaciones {

    // Ruta del JSON de exportación/importación
    private static final File JSON_FILE = new File("data", "lampreasvioleta_export.json");

    // DAOs
    private static final ClienteDAO clienteDAO = new ClienteDAO();
    private static final DetalleClienteDAO detalleClienteDAO = new DetalleClienteDAO();

    private static final ProductoDAO productoDAO = new ProductoDAO();
    private static final PedidoDAO pedidoDAO = new PedidoDAO();
    private static final DetallePedidoDAO detallePedidoDAO = new DetallePedidoDAO();
    private static final RepartidorDAO repartidorDAO = new RepartidorDAO();
    private static final ComercialDAO comercialDAO = new ComercialDAO();

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {

            while (true) {
                mostrarMenu();
                System.out.print("Opción: ");
                String op = sc.nextLine().trim();

                try {
                    switch (op) {
                        // -------------------- CLIENTE --------------------
                        case "1"  -> listarClientes();
                        case "2"  -> insertarCliente(sc);
                        case "3"  -> buscarClientePorId(sc);

                        // ----------------- DETALLE_CLIENTE ----------------
                        case "4"  -> listarDetallesCliente();
                        case "5"  -> insertarDetalleCliente(sc);
                        case "6"  -> buscarDetalleClientePorId(sc);
                        case "7"  -> eliminarDetallesCliente(sc);

                        // -------------------- PRODUCTO -------------------
                        case "8"  -> listarProductos();
                        case "9"  -> insertarProducto(sc);
                        case "10" -> buscarProductoPorId(sc);

                        // --------------------- PEDIDO --------------------
                        case "11" -> listarPedidos();
                        case "12" -> insertarPedido(sc);
                        case "13" -> buscarPedidoPorId(sc);

                        // ----------------- DETALLE_PEDIDO ----------------
                        case "14" -> listarDetallesPedido();
                        case "15" -> insertarDetallePedido(sc);
                        case "16" -> eliminarDetallePedido(sc);

                        // ----------------- REPARTIDOR ----------------
                        case "17" -> listarRepartidores();
                        case "18" -> insertarRepartidor(sc);
                        case "19" -> buscarRepartidorPorId(sc);

                        // ----------------- COMERCIAL ----------------
                        case "20" -> listarComerciales();
                        case "21" -> insertarComercial(sc);
                        case "22" -> buscarComercialPorId(sc);

                        // ---------------- JSON EXPORT / IMPORT ------------
                        case "23" -> exportarJson();
                        case "24" -> importarJson();

                        case "0"  -> {
                            System.out.println("FIN.");
                            return;
                        }
                        default -> System.out.println("Opción no válida.");
                    }

                } catch (SQLException e) {
                    System.err.println("[SQL ERROR] " + e.getMessage());
                } catch (IOException e) {
                    System.err.println("[IO ERROR] " + e.getMessage());
                } catch (Exception e) {
                    System.err.println("[ERROR] " + e.getMessage());
                    e.printStackTrace();
                }

                System.out.println();
            }
        }
    }

    private static void mostrarMenu() {
        System.out.println("=========================================");
        System.out.println(" DEMO RELACIONES (DAO + JSON)");
        System.out.println("=========================================");
        System.out.println("CLIENTE");
        System.out.println("  1  - Listar clientes");
        System.out.println("  2  - Insertar cliente");
        System.out.println("  3  - Buscar cliente por id");
        System.out.println();
        System.out.println("DETALLE_CLIENTE (1:1)");
        System.out.println("  4  - Listar detalles cliente");
        System.out.println("  5  - Insertar detalle cliente");
        System.out.println("  6  - Buscar detalle cliente por id");
        System.out.println("  7  - Eliminar detalle cliente");
        System.out.println();
        System.out.println("PRODUCTO");
        System.out.println("  8  - Listar productos");
        System.out.println("  9  - Insertar producto");
        System.out.println(" 10  - Buscar producto por id");
        System.out.println();
        System.out.println("PEDIDO (1:N con cliente)");
        System.out.println(" 11  - Listar pedidos");
        System.out.println(" 12  - Insertar pedido");
        System.out.println(" 13  - Buscar pedido por id");
        System.out.println();
        System.out.println("DETALLE_PEDIDO (N:M pedido-producto)");
        System.out.println(" 14  - Listar detalles pedido");
        System.out.println(" 15  - Insertar detalle pedido");
        System.out.println(" 16  - Eliminar detalle pedido");
        System.out.println();
        System.out.println("REPARTIDOR");
        System.out.println(" 17  - Listar repartidores");
        System.out.println(" 18  - Insertar repartidor");
        System.out.println(" 19  - Buscar repartidor por id");
        System.out.println();
        System.out.println("COMERCIAL");
        System.out.println(" 20  - Listar comerciales");
        System.out.println(" 21  - Insertar comercial");
        System.out.println(" 22  - Buscar comercial por id");
        System.out.println();
        System.out.println("JSON");
        System.out.println(" 23  - Exportar BD a JSON");
        System.out.println(" 24  - Importar JSON a BD (INSERT en orden FK)");
        System.out.println();
        System.out.println("  0  - Salir");
        System.out.println("=========================================");
    }


    // =========================================================
    // CLIENTE
    // =========================================================

    private static void listarClientes() throws SQLException {
        List<Cliente> list = clienteDAO.findAll();
        System.out.println("CLIENTES: " + list.size());
        list.forEach(System.out::println);
    }

    private static void insertarCliente(Scanner sc) throws SQLException {
        int id = InputUtils.readIntegers(sc,"ID Cliente: ");
        String nombre = InputUtils.readStrings(sc,"Nombre cliente: ");
        String email = InputUtils.readEmail(sc,"Email cliente: ");
        int idComercial = InputUtils.readIntegers(sc,"IdComercial: ");
        clienteDAO.insert(new Cliente(id, nombre, email, idComercial));
        System.out.println("Cliente insertado.");
    }

    private static void buscarClientePorId(Scanner sc) throws SQLException {
        int id = InputUtils.readIntegers(sc,"ID Cliente: ");
        Cliente c = clienteDAO.findById(id);
        System.out.println(c == null ? "No encontrado." : c);
    }

    // =========================================================
    // DETALLE_CLIENTE
    // =========================================================

    private static void listarDetallesCliente() throws SQLException {
        List<DetalleCliente> list = detalleClienteDAO.findAll();
        System.out.println("DETALLES_CLIENTE: " + list.size());
        list.forEach(System.out::println);
    }

    private static void insertarDetalleCliente(Scanner sc) throws SQLException {
        int id = InputUtils.readIntegers(sc,"ID Cliente: ");
        String direccion = InputUtils.readStrings(sc,"Direccion cliente: ");
        String telefono = InputUtils.readStrings(sc,"Telefono cliente: ");
        String notas = InputUtils.readStrings(sc,"Notas cliente: ");

        detalleClienteDAO.insert(new DetalleCliente(id, direccion, telefono, notas));
        System.out.println("DetalleCliente insertado.");
    }

    private static void buscarDetalleClientePorId(Scanner sc) throws SQLException {
        int  id = InputUtils.readIntegers(sc,"ID Cliente: ");
        DetalleCliente d = detalleClienteDAO.findById(id);
        System.out.println(d == null ? "No encontrado." : d);
    }

    private static void eliminarDetallesCliente(Scanner sc) throws SQLException{
        int  id = InputUtils.readIntegers(sc,"ID Cliente: ");
        detalleClienteDAO.deleteById(id);
    }

    // =========================================================
    // PRODUCTO
    // =========================================================

    private static void listarProductos() throws SQLException {
        List<Producto> list = productoDAO.findAll();
        System.out.println("PRODUCTOS: " + list.size());
        list.forEach(System.out::println);
    }

    private static void insertarProducto(Scanner sc) throws SQLException {
        int  id = InputUtils.readIntegers(sc,"ID Producto: ");
        String nombre = InputUtils.readStrings(sc,"Nombre cliente: ");
        Double precio = InputUtils.readDoubles(sc,"Precio cliente: ");

        productoDAO.insert(new Producto(id, nombre, precio));
        System.out.println("Producto insertado.");
    }

    private static void buscarProductoPorId(Scanner sc) throws SQLException {
        int  id = InputUtils.readIntegers(sc,"ID Producto: ");
        Producto p = productoDAO.findById(id);
        System.out.println(p == null ? "No encontrado." : p);
    }

    // =========================================================
    // PEDIDO
    // =========================================================

    private static void listarPedidos() throws SQLException {
        List<Pedido> list = pedidoDAO.findAll();
        System.out.println("PEDIDOS: " + list.size());

        // Mostrar cada pedido y a continuación sus líneas (si tienes findByPedidoId)
        for (Pedido p : list) {
            System.out.println(p);

            // Si tu DetallePedidoDAO tiene método findByPedidoId:
            List<DetallePedido> lineas = detallePedidoDAO.findByPedidoId(p.getId());
            for (DetallePedido dp : lineas) {
                System.out.println("   -> " + dp);
            }
        }
    }

    private static void insertarPedido(Scanner sc) throws SQLException {
        int  id = InputUtils.readIntegers(sc,"ID Pedido: ");
        int clienteId = InputUtils.readIntegers(sc,"ID Cliente: ");
        LocalDate fecha = InputUtils.readLocalDate(sc,"Fecha de Pedido: ");
        int repartidorId = InputUtils.readIntegers(sc,"ID Repartidor: ");

        pedidoDAO.insert(new Pedido(id, clienteId, fecha, repartidorId));
        System.out.println("Pedido insertado.");
    }

    private static void buscarPedidoPorId(Scanner sc) throws SQLException {
        int   id = InputUtils.readIntegers(sc,"ID Pedido: ");
        Pedido p = pedidoDAO.findById(id);

        if (p == null) {
            System.out.println("No encontrado.");
            return;
        }

        System.out.println(p);
        List<DetallePedido> lineas = detallePedidoDAO.findByPedidoId(p.getId());
        for (DetallePedido dp : lineas) {
            System.out.println("   -> " + dp);
        }
    }

    // =========================================================
    // DETALLE_PEDIDO
    // =========================================================

    private static void listarDetallesPedido() throws SQLException {
        List<DetallePedido> list = detallePedidoDAO.findAll();
        System.out.println("DETALLES_PEDIDO: " + list.size());
        list.forEach(System.out::println);
    }

    private static void insertarDetallePedido(Scanner sc) throws SQLException {
        int  pedidoId = InputUtils.readIntegers(sc,"ID Pedido: ");
        int productoId = InputUtils.readIntegers(sc,"ID Producto: ");
        int cantidad = InputUtils.readIntegers(sc,"Cantidad: ");
        Double precioUnit = InputUtils.readDoubles(sc,"Precio unit: ");

        detallePedidoDAO.insert(new DetallePedido(pedidoId, productoId, cantidad, precioUnit));
        System.out.println("DetallePedido insertado.");
    }

    private static void eliminarDetallePedido(Scanner sc) throws SQLException {
        int  pedidoId = InputUtils.readIntegers(sc,"ID Pedido: ");
        int  productoId = InputUtils.readIntegers(sc,"ID Producto: ");
        detallePedidoDAO.deleteById(pedidoId, productoId);
    }

    // =========================================================
    // REPARTIDOR
    // =========================================================

    private static void listarRepartidores() throws SQLException {
        List<Repartidor> list = repartidorDAO.findAll();
        System.out.println("REPARTIDORES: " + list.size());
        list.forEach(System.out::println);
    }

    private static void insertarRepartidor(Scanner sc) throws SQLException {
        String  nombre = InputUtils.readStrings(sc,"Nombre cliente: ");
        String apellidos = InputUtils.readStrings(sc,"Apellido cliente: ");
        String telefono = InputUtils.readStrings(sc,"Telefono cliente: ");
        String vehiculo = InputUtils.readStrings(sc,"Valor cliente: ");

        repartidorDAO.insert(new Repartidor(nombre,apellidos,telefono,vehiculo));
        System.out.println("Repartidor insertado.");

    }

    private static void buscarRepartidorPorId(Scanner sc) throws SQLException {
        int idRepartidor = InputUtils.readIntegers(sc,"ID Repartidor: ");
        Repartidor r = repartidorDAO.findById(idRepartidor);
        if (r == null) {
            System.out.println("No encontrado.");
        }
        System.out.println(r);
    }

    // =========================================================
    // COMERCIAL
    // =========================================================


    private static void listarComerciales() throws SQLException {
        List<Comercial> list = comercialDAO.findAll();
        System.out.println("COMERCIALES: " + list.size());
        list.forEach(System.out::println);
    }

    private static void insertarComercial(Scanner sc) throws SQLException {
        String  nombre = InputUtils.readStrings(sc,"Nombre cliente: ");
        String apellidos = InputUtils.readStrings(sc,"Apellido cliente: ");
        String telefono = InputUtils.readStrings(sc,"Telefono cliente: ");
        String zona = InputUtils.readStrings(sc,"Zona cliente: ");

        comercialDAO.insert(new Comercial(null, nombre, apellidos, telefono, zona));
        System.out.println("Comercial insertado.");
    }

    private static void buscarComercialPorId(Scanner sc) throws SQLException {
        int idComercial = InputUtils.readIntegers(sc,"ID Comercial: ");
        Comercial c = comercialDAO.findById(idComercial);
        if (c == null) {
            System.out.println("No encontrado.");
        }
        System.out.println(c);
    }


    // =========================================================
    // JSON EXPORT / IMPORT
    // =========================================================

    /**
     * Exporta una "foto" de la BD a JSON.
     * Lee todas las tablas y las serializa.
     */
    private static void exportarJson() throws SQLException, IOException {
        AppData data = new AppData();

        data.setClientes(clienteDAO.findAll());
        data.setDetallesCliente(detalleClienteDAO.findAll());
        data.setProductos(productoDAO.findAll());
        data.setPedidos(pedidoDAO.findAll());
        data.setDetallesPedido(detallePedidoDAO.findAll());
        data.setRepartidores(repartidorDAO.findAll());
        data.setComerciales(comercialDAO.findAll());

        JsonIO.write(JSON_FILE, data);

        System.out.println("Exportado JSON en: " + JSON_FILE.getAbsolutePath());
    }

    /**
     * Importa JSON a la BD haciendo INSERT en orden correcto por FKs:
     *  1) cliente
     *  2) detalle_cliente
     *  3) producto
     *  4) pedido
     *  5) detalle_pedido
     *  6)repartidores
     *  7)comerciales
     *
     * IMPORTANTE:
     * - No borra lo existente (si ya hay IDs repetidos, fallará por PK).
     * - En clase podéis añadir luego una opción "vaciar tablas" o "upsert".
     */
    private static void importarJson() throws IOException, SQLException {
        if (!JSON_FILE.exists()) {
            System.out.println("No existe el JSON: " + JSON_FILE.getAbsolutePath());
            return;
        }

        AppData data = JsonIO.read(JSON_FILE, AppData.class);

        // 1) Clientes
        for (Cliente c : data.getClientes()) {
            // Podrías comprobar si existe para evitar error, pero lo dejamos simple:
            // si existe, fallará por PK/unique -> perfecto para explicar integridad.
            clienteDAO.insert(c);
        }

        // 2) Detalles cliente (requieren cliente previo)
        for (DetalleCliente d : data.getDetallesCliente()) {
            detalleClienteDAO.insert(d);
        }

        // 3) Productos
        for (Producto p : data.getProductos()) {
            productoDAO.insert(p);
        }

        // 4) Pedidos (requieren cliente previo)
        for (Pedido pe : data.getPedidos()) {
            pedidoDAO.insert(pe);
        }

        // 5) Detalles pedido (requieren pedido y producto previos)
        for (DetallePedido dp : data.getDetallesPedido()) {
            detallePedidoDAO.insert(dp);
        }

        // 6) Repartidores
        for (Repartidor r : data.getRepartidores()) {
            repartidorDAO.insert(r);
        }

        //7) Comerciales
        for (Comercial c : data.getComerciales()) {
            comercialDAO.insert(c);
        }

        System.out.println("Importación finalizada.");
    }
}
