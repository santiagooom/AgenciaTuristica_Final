package modelo;

import java.time.LocalDate;

public abstract class Usuario {
    private int id;
    private String nombre;
    private String correo;
    private String clave;
    private boolean activo;
    private LocalDate fechaRegistro;
    private String preguntaSeguridad;
    private String respuestaSeguridad;

    public Usuario(int id, String nombre, String correo, String clave, LocalDate fechaRegistro) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.clave = clave;
        this.activo = true;
        this.fechaRegistro = fechaRegistro;
        this.preguntaSeguridad = "";
        this.respuestaSeguridad = "";
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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public String getPreguntaSeguridad() {
        return preguntaSeguridad;
    }

    public void setPreguntaSeguridad(String preguntaSeguridad) {
        this.preguntaSeguridad = preguntaSeguridad;
    }

    public String getRespuestaSeguridad() {
        return respuestaSeguridad;
    }

    public void setRespuestaSeguridad(String respuestaSeguridad) {
        this.respuestaSeguridad = respuestaSeguridad;
    }

    public abstract String getRol();

    public abstract String getDetalle();

    @Override
    public String toString() {
        return nombre + " - " + getRol();
    }
}
