package modelo;

import java.time.LocalDate;

public class GuiaTuristico extends Usuario {
    private String idioma;
    private String regionAsignada;
    private LocalDate fechaDisponibleDesde;

    public GuiaTuristico(int id, String nombre, String correo, String clave,
                         LocalDate fechaRegistro, String idioma, String regionAsignada,
                         LocalDate fechaDisponibleDesde) {
        super(id, nombre, correo, clave, fechaRegistro);
        this.idioma = idioma;
        this.regionAsignada = regionAsignada;
        this.fechaDisponibleDesde = fechaDisponibleDesde;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public String getRegionAsignada() {
        return regionAsignada;
    }

    public void setRegionAsignada(String regionAsignada) {
        this.regionAsignada = regionAsignada;
    }

    public LocalDate getFechaDisponibleDesde() {
        return fechaDisponibleDesde;
    }

    public void setFechaDisponibleDesde(LocalDate fechaDisponibleDesde) {
        this.fechaDisponibleDesde = fechaDisponibleDesde;
    }

    public boolean puedeCubrir(String region, LocalDate fechaSalida) {
        return isActivo()
                && regionAsignada.equalsIgnoreCase(region)
                && !fechaSalida.isBefore(fechaDisponibleDesde);
    }

    @Override
    public String getRol() {
        return "GUIA";
    }

    @Override
    public String getDetalle() {
        return "Región: " + regionAsignada
                + " | Disponible desde: " + CalendarioTuristico.formatearFecha(fechaDisponibleDesde);
    }

    @Override
    public String toString() {
        return getNombre() + " - " + regionAsignada;
    }
}
