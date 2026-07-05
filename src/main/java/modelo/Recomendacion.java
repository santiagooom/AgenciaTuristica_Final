package modelo;

import java.time.LocalDate;

public class Recomendacion {
    private int id;
    private Turista turista;
    private Reserva reserva;
    private int calificacionGuia;
    private int calificacionExperiencia;
    private String comentario;
    private LocalDate fechaComentario;

    public Recomendacion(int id, Turista turista, Reserva reserva,
                         int calificacionGuia, int calificacionExperiencia,
                         String comentario, LocalDate fechaComentario) {
        this.id = id;
        this.turista = turista;
        this.reserva = reserva;
        this.calificacionGuia = calificacionGuia;
        this.calificacionExperiencia = calificacionExperiencia;
        this.comentario = comentario;
        this.fechaComentario = fechaComentario;
    }

    public int getId() {
        return id;
    }

    public Turista getTurista() {
        return turista;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public int getCalificacionGuia() {
        return calificacionGuia;
    }

    public int getCalificacionExperiencia() {
        return calificacionExperiencia;
    }

    public String getComentario() {
        return comentario;
    }

    public LocalDate getFechaComentario() {
        return fechaComentario;
    }
}
