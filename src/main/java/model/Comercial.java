package model;

public class Comercial {

    private Integer id;
    private String nombre;
    private String apellidos;
    private String telefono;
    private String zona;


    public Comercial() {};

    public Comercial(String nombre, String apellidos, String telefono, String zona) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.zona = zona;

    }

    public Comercial(Integer id,String nombre, String apellidos, String telefono, String zona) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.zona = zona;


    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getApellidos() {
        return apellidos;
    }
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    public String getZona() {
        return zona;
    }
    public void setZona(String zona) {
        this.zona = zona;
    }

    @Override
    public String toString() {
        return "Comercial{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", telefono='" + telefono + '\'' +
                ", zona='" + zona + '\'' +
                '}';
    }
}
