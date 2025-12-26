package app;

import dao.ClienteDAO;
import model.Cliente;
import model.DetalleCliente;
import services.ClienteDetalle;
import dao.DetalleClienteDAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClientesView {

    private final BorderPane root = new BorderPane();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final ClienteDetalle clienteService = new ClienteDetalle();
    private final DetalleClienteDAO detalleClienteDAO = new DetalleClienteDAO();

    // Caché en memoria: idCliente -> detalle
    private final Map<Integer, DetalleCliente> cacheDetalles = new HashMap<>();

    // Tabla y datos
    private final TableView<Cliente> tabla = new TableView<>();
    private final ObservableList<Cliente> datos = FXCollections.observableArrayList();

    // Campos de formulario (Cliente)
    private final TextField txtId = new TextField();
    private final TextField txtNombre = new TextField();
    private final TextField txtEmail = new TextField();
    private final TextField txtIdComercial = new TextField(); // NUEVO

    // Campos de formulario (DetalleCliente) – visuales por ahora
    private final TextField txtDireccion = new TextField();
    private final TextField txtTelefono  = new TextField();
    private final TextField txtNotas     = new TextField();

    // Botones CRUD
    private final Button btnNuevo    = new Button("Nuevo");
    private final Button btnGuardar  = new Button("Guardar");
    private final Button btnBorrar   = new Button("Borrar");
    private final Button btnRecargar = new Button("Recargar");

    // Búsqueda
    private final TextField txtBuscar          = new TextField();
    private final Button    btnBuscar          = new Button("Buscar");
    private final Button    btnLimpiarBusqueda = new Button("Limpiar");

    public ClientesView() {
        configurarTabla();
        configurarFormulario();
        configurarEventos();
        recargarDatos(); // cargar al iniciar la vista
    }

    public Parent getRoot() {
        return root;
    }

    /* =========================================================
       CONFIGURACIÓN INTERFAZ
       ========================================================= */
    private void configurarTabla() {
        TableColumn<Cliente, Number> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getId()));

        TableColumn<Cliente, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNombre()));

        TableColumn<Cliente, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getEmail()));

        TableColumn<Cliente, Number> colIdComercial = new TableColumn<>("ID Comercial"); // NUEVO
        colIdComercial.setCellValueFactory(c ->
                new javafx.beans.property.SimpleIntegerProperty(c.getValue().getIdComercial()));

        // Columnas “placeholder” para DetalleCliente
        TableColumn<Cliente, String> colDireccion = new TableColumn<>("Dirección");
        colDireccion.setCellValueFactory(c -> {
            DetalleCliente d = cacheDetalles.get(c.getValue().getId());
            String valor = (d != null) ? d.getDireccion() : "";
            return new javafx.beans.property.SimpleStringProperty(valor);
        });

        TableColumn<Cliente, String> colTelefono = new TableColumn<>("Teléfono");
        colTelefono.setCellValueFactory(c -> {
            DetalleCliente d = cacheDetalles.get(c.getValue().getId());
            String valor = (d != null) ? d.getTelefono() : "";
            return new javafx.beans.property.SimpleStringProperty(valor);
        });

        TableColumn<Cliente, String> colNotas = new TableColumn<>("Notas");
        colNotas.setCellValueFactory(c -> {
            DetalleCliente d = cacheDetalles.get(c.getValue().getId());
            String valor = (d != null) ? d.getNotas() : "";
            return new javafx.beans.property.SimpleStringProperty(valor);
        });

        tabla.getColumns().addAll(colId, colNombre, colEmail, colIdComercial,
                colDireccion, colTelefono, colNotas);
        tabla.setItems(datos);

        root.setCenter(tabla);
    }

    private void configurarFormulario() {
        GridPane form = new GridPane();
        form.setPadding(new Insets(10));
        form.setHgap(10);
        form.setVgap(10);

        // ----- Cliente -----
        txtId.setPromptText("ID (entero)");
        txtNombre.setPromptText("Nombre");
        txtEmail.setPromptText("Email");
        txtIdComercial.setPromptText("ID Comercial"); // NUEVO

        form.add(new Label("ID:"), 0, 0);
        form.add(txtId, 1, 0);
        form.add(new Label("Nombre:"), 0, 1);
        form.add(txtNombre, 1, 1);
        form.add(new Label("Email:"), 0, 2);
        form.add(txtEmail, 1, 2);
        form.add(new Label("ID Comercial:"), 0, 3);
        form.add(txtIdComercial, 1, 3);

        // ----- DetalleCliente -----
        txtDireccion.setPromptText("Dirección");
        txtTelefono.setPromptText("Teléfono");
        txtNotas.setPromptText("Notas");

        form.add(new Label("Dirección:"), 0, 4);
        form.add(txtDireccion, 1, 4);
        form.add(new Label("Teléfono:"), 0, 5);
        form.add(txtTelefono, 1, 5);
        form.add(new Label("Notas:"), 0, 6);
        form.add(txtNotas, 1, 6);

        // Zona botones CRUD
        HBox botonesCrud = new HBox(10, btnNuevo, btnGuardar, btnBorrar, btnRecargar);
        botonesCrud.setPadding(new Insets(10, 0, 0, 0));

        // Zona de búsqueda
        HBox zonaBusqueda = new HBox(10,
                new Label("Buscar:"), txtBuscar, btnBuscar, btnLimpiarBusqueda);
        zonaBusqueda.setPadding(new Insets(10, 0, 10, 0));

        BorderPane bottom = new BorderPane();
        bottom.setTop(zonaBusqueda);
        bottom.setCenter(form);
        bottom.setBottom(botonesCrud);

        root.setBottom(bottom);
    }

    private void configurarEventos() {
        // Cuando seleccionamos una fila en la tabla
        tabla.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                txtId.setText(String.valueOf(newSel.getId()));
                txtNombre.setText(newSel.getNombre());
                txtEmail.setText(newSel.getEmail());
                txtIdComercial.setText(String.valueOf(newSel.getIdComercial())); // NUEVO
                txtId.setDisable(true);
                txtIdComercial.setDisable(true);

                // DetalleCliente
                txtDireccion.clear();
                txtTelefono.clear();
                txtNotas.clear();
            }
        });

        btnNuevo.setOnAction(e -> limpiarFormulario());
        btnGuardar.setOnAction(e -> guardarCliente());
        btnBorrar.setOnAction(e -> borrarClienteSeleccionado());
        btnRecargar.setOnAction(e -> {
            txtBuscar.clear();
            recargarDatos();
        });
        btnBuscar.setOnAction(e -> buscarClientesEnBBDD());
        btnLimpiarBusqueda.setOnAction(e -> {
            txtBuscar.clear();
            recargarDatos();
        });
    }

    /* =========================================================
       LÓGICA DE NEGOCIO
       ========================================================= */
    private void recargarDatos() {
        try {
            List<Cliente> clientes = clienteDAO.findAll();
            List<DetalleCliente> detalles = detalleClienteDAO.findAll();

            cacheDetalles.clear();
            for (DetalleCliente d : detalles) {
                cacheDetalles.put(d.getId(), d);
            }

            datos.setAll(clientes);
        } catch (SQLException e) {
            mostrarError("Error al recargar datos", e);
        }
    }

    private void buscarClientesEnBBDD(){
        String filtro = txtBuscar.getText().trim();
        if (filtro.isEmpty()){
            recargarDatos();
            return;
        }

        try {
            List<Cliente> lista = clienteDAO.search(filtro);
            datos.setAll(lista);
        } catch (SQLException e){
            mostrarError("Error al buscar", e);
        }
    }

    private void limpiarFormulario() {
        txtId.clear();
        txtNombre.clear();
        txtEmail.clear();
        txtIdComercial.clear(); // NUEVO
        txtDireccion.clear();
        txtTelefono.clear();
        txtNotas.clear();
        txtId.setDisable(false);
        txtIdComercial.setDisable(false);
        tabla.getSelectionModel().clearSelection();
    }

    private void guardarCliente() {
        if (txtId.getText().isBlank() || txtNombre.getText().isBlank() || txtEmail.getText().isBlank() || txtIdComercial.getText().isBlank()) {
            mostrarAlerta("Campos obligatorios","Debes rellenar ID, nombre, email y ID Comercial.");
            return;
        }

        int id, idComercial;
        try {
            id = Integer.parseInt(txtId.getText().trim());
            idComercial = Integer.parseInt(txtIdComercial.getText().trim());
        } catch (NumberFormatException ex) {
            mostrarAlerta("ID inválido", "El ID y ID Comercial deben ser números enteros.");
            return;
        }

        Cliente c = new Cliente(
                id,
                txtNombre.getText().trim(),
                txtEmail.getText().trim(),
                idComercial
        );

        DetalleCliente d = new DetalleCliente(
                id,
                txtDireccion.getText().trim(),
                txtTelefono.getText().trim(),
                txtNotas.getText().trim()
        );

        try {
            Cliente existente = clienteDAO.findById(id);
            if (existente == null) {
                clienteService.guardarClienteCompleto(c, d);
                mostrarInfo("Insertado","Cliente y detalle creados correctamente.");
            } else {
                mostrarAlerta("Actualizar pendiente","El cliente ya existe.\nMás adelante aquí haremos UPDATE.");
            }

            recargarDatos();
            limpiarFormulario();
        } catch (SQLException e) {
            mostrarError("Error al guardar cliente y detalle", e);
        }
    }

    private void borrarClienteSeleccionado() {
        Cliente sel = tabla.getSelectionModel().getSelectedItem();
        if (sel == null) {
            mostrarAlerta("Sin selección", "Selecciona un cliente en la tabla.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar borrado");
        confirm.setHeaderText("¿Eliminar cliente?");
        confirm.setContentText("Se borrará el cliente con ID " + sel.getId());
        if (confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }

        mostrarAlerta("Borrado pendiente","Aún no existe deleteById en ClienteDAO.");
    }

    /* =========================================================
       DIÁLOGOS AUXILIARES
       ========================================================= */
    private void mostrarError(String titulo, Exception e) {
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(titulo);
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarInfo(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
