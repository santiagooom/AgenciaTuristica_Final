package modelo;

import java.time.LocalDate;
import java.util.ArrayList;

public class SalidaProgramada {
    private int id;
    private PaqueteTuristico paquete;
    private GuiaTuristico guia;
    private LocalDate fechaSalida;
    private LocalDate fechaRetorno;
    private int cupoMaximo;
    private double precioFinal;
    private String temporada;
    private boolean activa;
    private ArrayList<Reserva> reservas;

    public SalidaProgramada(int id, PaqueteTuristico paquete, GuiaTuristico guia,
                            LocalDate fechaSalida, LocalDate fechaRetorno,
                            int cupoMaximo, double precioFinal, String temporada) {
        this.id = id;
        this.paquete = paquete;
        this.guia = guia;
        this.fechaSalida = fechaSalida;
        this.fechaRetorno = fechaRetorno;
        this.cupoMaximo = cupoMaximo;
        this.precioFinal = precioFinal;
        this.temporada = temporada;
        this.activa = true;
        this.reservas = new ArrayList<Reserva>();
    }

    public int getId() {
        return id;
    }

    public PaqueteTuristico getPaquete() {
        return paquete;
    }

    public void setPaquete(PaqueteTuristico paquete) {
        this.paquete = paquete;
    }

    public GuiaTuristico getGuia() {
        return guia;
    }

    public void setGuia(GuiaTuristico guia) {
        this.guia = guia;
    }

    public LocalDate getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDate fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public LocalDate getFechaRetorno() {
        return fechaRetorno;
    }

    public void setFechaRetorno(LocalDate fechaRetorno) {
        this.fechaRetorno = fechaRetorno;
    }

    public int getCupoMaximo() {
        return cupoMaximo;
    }

    public void setCupoMaximo(int cupoMaximo) {
        this.cupoMaximo = cupoMaximo;
    }

    public double getPrecioFinal() {
        return precioFinal;
    }

    public void setPrecioFinal(double precioFinal) {
        this.precioFinal = precioFinal;
    }

    public String getTemporada() {
        return temporada;
    }

    public void setTemporada(String temporada) {
        this.temporada = temporada;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public ArrayList<Reserva> getReservas() {
        return reservas;
    }

    public void agregarReserva(Reserva reserva) {
        reservas.add(reserva);
    }

    public int getCuposDisponibles() {
        int ocupados = 0;
        for (Reserva reserva : reservas) {
            if (!reserva.getEstado().equals("CANCELADA")) {
                ocupados++;
            }
        }
        return cupoMaximo - ocupados;
    }

    @Override
    public String toString() {
        return paquete.getNombre() + " | " + CalendarioTuristico.formatearFecha(fechaSalida)
                + " | Cupos: " + getCuposDisponibles();
    }
}
