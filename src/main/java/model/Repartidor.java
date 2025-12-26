package model;

public class Repartidor {

    private Integer id;
    private String nombre;
    private String apellidos;
    private String telefono;
    private String vehiculo;



    public Repartidor(){};

    public Repartidor(String nombre, String apellidos,String telefono,String vehiculo) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.vehiculo = vehiculo;

    }

    public Repartidor(Integer id, String nombre, String apellidos,String telefono,String vehiculo) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.vehiculo = vehiculo;

    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
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
    public String getVehiculo() {
        return vehiculo;
    }
    public void setVehiculo(String vehiculo) {
        this.vehiculo = vehiculo;
    }

    @Override
    public String toString() {
        return "Repartidor{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", telefono='" + telefono + '\'' +
                ", vehiculo='" + vehiculo + '\'' +
                '}';
    }
}
