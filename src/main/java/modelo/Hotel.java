package modelo;

public class Hotel {
    private int id;
    private String nombre;
    private LugarTuristico lugar;
    private int categoria;
    private boolean activo;

    public Hotel(int id, String nombre, LugarTuristico lugar, int categoria) {
        this.id = id;
        this.nombre = nombre;
        this.lugar = lugar;
        this.categoria = categoria;
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

    public LugarTuristico getLugar() {
        return lugar;
    }

    public void setLugar(LugarTuristico lugar) {
        this.lugar = lugar;
    }

    public String getCiudad() {
        return lugar == null ? "Sin lugar" : lugar.getNombre();
    }

    public String getRegion() {
        return lugar == null ? "Sin región" : lugar.getRegion();
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return nombre + " - " + getCiudad() + " (" + categoria + " estrellas)";
    }
}
