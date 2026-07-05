package modelo;

import java.time.LocalDate;

public class Administrador extends Usuario {

    public Administrador(int id, String nombre, String correo, String clave, LocalDate fechaRegistro) {
        super(id, nombre, correo, clave, fechaRegistro);
    }

    @Override
    public String getRol() {
        return "ADMINISTRADOR";
    }

    @Override
    public String getDetalle() {
        return "Administrador general del sistema";
    }
}
