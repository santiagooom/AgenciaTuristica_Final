package modelo;

import java.util.ArrayList;

public class PaqueteTuristico {
    private int id;
    private String nombre;
    private String descripcion;
    private int duracionDias;
    private double precioBase;
    private Hotel hotel;
    private ArrayList<LugarTuristico> lugares;
    private String lugarSalida;
    private String lugarRegreso;
    private boolean activo;

    public PaqueteTuristico(int id, String nombre, String descripcion, int duracionDias,
                            double precioBase, Hotel hotel, ArrayList<LugarTuristico> lugares,
                            String lugarSalida, String lugarRegreso) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.duracionDias = duracionDias;
        this.precioBase = precioBase;
        this.hotel = hotel;
        this.lugares = lugares;
        this.lugarSalida = lugarSalida;
        this.lugarRegreso = lugarRegreso;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getDuracionDias() {
        return duracionDias;
    }

    public void setDuracionDias(int duracionDias) {
        this.duracionDias = duracionDias;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(double precioBase) {
        this.precioBase = precioBase;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public ArrayList<LugarTuristico> getLugares() {
        return lugares;
    }

    public void setLugares(ArrayList<LugarTuristico> lugares) {
        this.lugares = lugares;
    }

    public String getLugarSalida() {
        return lugarSalida;
    }

    public void setLugarSalida(String lugarSalida) {
        this.lugarSalida = lugarSalida;
    }

    public String getLugarRegreso() {
        return lugarRegreso;
    }

    public void setLugarRegreso(String lugarRegreso) {
        this.lugarRegreso = lugarRegreso;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getRegion() {
        if (lugares == null || lugares.isEmpty()) {
            return "Sin región";
        }
        return lugares.get(0).getRegion();
    }

    public String getRegiones() {
        ArrayList<String> regiones = new ArrayList<String>();
        for (LugarTuristico lugar : lugares) {
            boolean repetida = false;
            for (String region : regiones) {
                if (region.equalsIgnoreCase(lugar.getRegion())) {
                    repetida = true;
                }
            }
            if (!repetida) {
                regiones.add(lugar.getRegion());
            }
        }
        return String.join(", ", regiones);
    }

    public String getNombresLugares() {
        ArrayList<String> nombres = new ArrayList<String>();
        for (LugarTuristico lugar : lugares) {
            nombres.add(lugar.getNombre());
        }
        return String.join(", ", nombres);
    }

    @Override
    public String toString() {
        return nombre;
    }
}
