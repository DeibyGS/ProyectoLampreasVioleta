package dao;

import db.Db;
import model.Comercial;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComercialDAO {

    // ----------------------------------------------------------
    // SENTENCIAS SQL PREPARADAS COMO CONSTANTES
    // ----------------------------------------------------------

    private static final String INSERT_SQL =
            "INSERT INTO comercial (nombre, apellidos, telefono, zona) VALUES (?, ?, ?, ?)";

    private static final String SELECT_BY_ID_SQL =
            "SELECT id, nombre, apellidos, telefono, zona FROM comercial WHERE id = ?";

    private static final String SELECT_ALL_SQL =
            "SELECT id, nombre, apellidos, telefono, zona FROM comercial ORDER BY id";

    private static final String SEARCH_SQL =
            "SELECT id, nombre, apellidos, telefono, zona FROM comercial " +
                    "WHERE CAST(id AS CHAR) LIKE ? OR nombre LIKE ? OR apellidos LIKE ? OR zona LIKE ? " +
                    "ORDER BY id";

    // ----------------------------------------------------------
    // MÉTODO: INSERTAR UN COMERCIAL
    // ----------------------------------------------------------

    public void insert(Comercial c) throws SQLException {
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, c.getNombre());
            ps.setString(2, c.getApellidos());
            ps.setString(3, c.getTelefono());
            ps.setString(4, c.getZona());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    c.setId(rs.getInt(1));
                }
            }
        }
    }

    // ----------------------------------------------------------
    // MÉTODO: BUSCAR COMERCIAL POR ID
    // ----------------------------------------------------------

    public Comercial findById(int id) throws SQLException {
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_BY_ID_SQL)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Comercial c = new Comercial();
                    c.setId(rs.getInt("id"));
                    c.setNombre(rs.getString("nombre"));
                    c.setApellidos(rs.getString("apellidos"));
                    c.setTelefono(rs.getString("telefono"));
                    c.setZona(rs.getString("zona"));
                    return c;
                }
            }
        }
        return null;
    }

    // ----------------------------------------------------------
    // MÉTODO: LISTAR TODOS LOS COMERCIALES
    // ----------------------------------------------------------

    public List<Comercial> findAll() throws SQLException {
        List<Comercial> out = new ArrayList<>();

        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Comercial c = new Comercial();
                c.setId(rs.getInt("id"));
                c.setNombre(rs.getString("nombre"));
                c.setApellidos(rs.getString("apellidos"));
                c.setTelefono(rs.getString("telefono"));
                c.setZona(rs.getString("zona"));
                out.add(c);
            }
        }
        return out;
    }

    // ----------------------------------------------------------
    // MÉTODO: BUSCAR (OPCIONAL)
    // ----------------------------------------------------------

    public List<Comercial> search(String texto) throws SQLException {
        List<Comercial> out = new ArrayList<>();
        String like = "%" + texto + "%";

        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(SEARCH_SQL)) {

            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            ps.setString(4, like);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Comercial c = new Comercial();
                    c.setId(rs.getInt("id"));
                    c.setNombre(rs.getString("nombre"));
                    c.setApellidos(rs.getString("apellidos"));
                    c.setTelefono(rs.getString("telefono"));
                    c.setZona(rs.getString("zona"));
                    out.add(c);
                }
            }
        }
        return out;
    }
}
