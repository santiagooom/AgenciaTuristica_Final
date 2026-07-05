package modelo;

import java.time.LocalDate;

public class Reserva {
    private int id;
    private Turista turista;
    private SalidaProgramada salida;
    private LocalDate fechaContratacion;
    private String estado;

    public Reserva(int id, Turista turista, SalidaProgramada salida,
                   LocalDate fechaContratacion, String estado) {
        this.id = id;
        this.turista = turista;
        this.salida = salida;
        this.fechaContratacion = fechaContratacion;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public Turista getTurista() {
        return turista;
    }

    public SalidaProgramada getSalida() {
        return salida;
    }

    public LocalDate getFechaContratacion() {
        return fechaContratacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return salida.getPaquete().getNombre() + " - "
                + CalendarioTuristico.formatearFecha(salida.getFechaSalida()) + " - " + estado;
    }
}
