package modelo;

public class LugarTuristico {
    private int id;
    private String nombre;
    private String region;
    private String ubicacion;
    private boolean activo;

    public LugarTuristico(int id, String nombre, String region, String ubicacion) {
        this.id = id;
        this.nombre = nombre;
        this.region = region;
        this.ubicacion = ubicacion;
        this.activo = true;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return nombre + " (" + region + ")";
    }
}
