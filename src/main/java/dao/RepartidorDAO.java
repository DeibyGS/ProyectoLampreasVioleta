package dao;

import db.Db;
import model.Repartidor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepartidorDAO {


    // ----------------------------------------------------------
    // SENTENCIAS SQL PREPARADAS COMO CONSTANTES
    // ----------------------------------------------------------

    private static final String INSERT_SQL =
            "INSERT INTO repartidor (nombre, apellidos,telefono,vehiculo) VALUES (?, ?, ?, ?)";
    private static final String SELECT_BY_ID_SQL =
            "SELECT id, nombre, apellidos,telefono,vehiculo FROM repartidor WHERE id = ?";
    private static final String SELECT_ALL_SQL =
            "SELECT id, nombre, apellidos,telefono,vehiculo FROM repartidor ORDER BY id";
    private static final String SEARCH_SQL =
            "SELECT id, nombre, apellidos,telefono,vehiculo FROM repartidor WHERE CAST(id AS CHAR) LIKE ? OR nombre LIKE ? OR email LIKE ? ORDER BY id;";

    // ----------------------------------------------------------
    // MÉTODO: INSERTAR UN REPARTIDOR
    // ----------------------------------------------------------


    public void insert(Repartidor r) throws SQLException {
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, r.getNombre());
            ps.setString(2, r.getApellidos());
            ps.setString(3, r.getTelefono());
            ps.setString(4, r.getVehiculo());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    r.setId(rs.getInt(1));
                }
            }
        }
    }


    // ----------------------------------------------------------
    // MÉTODO: BUSCAR REPARTIDOR POR ID
    // ----------------------------------------------------------

    public Repartidor findById(int id) throws SQLException {
        try (Connection con = Db.getConnection();
        PreparedStatement ps = con.prepareStatement(SELECT_BY_ID_SQL)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Repartidor r = new Repartidor();
                    r.setId(rs.getInt("id"));
                    r.setNombre(rs.getString("nombre"));
                    r.setApellidos(rs.getString("apellidos"));
                    r.setTelefono(rs.getString("telefono"));
                    r.setVehiculo(rs.getString("vehiculo"));
                    return r;
                }
            }
            return  null;
        }
    }

    // ----------------------------------------------------------
    // MÉTODO: LISTAR TODOS LOS CLIENTES
    // ----------------------------------------------------------

    public List<Repartidor> findAll() throws SQLException {
        List<Repartidor> out = new ArrayList<>();
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Repartidor r = new Repartidor();
                r.setId(rs.getInt("id"));
                r.setNombre(rs.getString("nombre"));
                r.setApellidos(rs.getString("apellidos"));
                r.setTelefono(rs.getString("telefono"));
                r.setVehiculo(rs.getString("vehiculo"));
                out.add(r);
            }
        }
        return out;
    }

    // ----------------------------------------------------------
// MÉTODO: BUSCAR (OPCIONAL)
// ----------------------------------------------------------

    public List<Repartidor> search(String texto) throws SQLException {
        List<Repartidor> out = new ArrayList<>();
        String like = "%" + texto + "%";

        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(SEARCH_SQL)) {

            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Repartidor r = new Repartidor();
                    r.setId(rs.getInt("id"));
                    r.setNombre(rs.getString("nombre"));
                    r.setApellidos(rs.getString("apellidos"));
                    r.setTelefono(rs.getString("telefono"));
                    r.setVehiculo(rs.getString("vehiculo"));
                    out.add(r);
                }
            }
        }
        return out;
    }





//final de la clase
}
