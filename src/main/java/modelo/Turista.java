package modelo;

import java.time.LocalDate;
import java.util.ArrayList;

public class Turista extends Usuario {
    private String idioma;
    private ArrayList<String> intereses;

    public Turista(int id, String nombre, String correo, String clave,
                   LocalDate fechaRegistro, String idioma, ArrayList<String> intereses) {
        super(id, nombre, correo, clave, fechaRegistro);
        this.idioma = idioma;
        this.intereses = intereses;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public ArrayList<String> getIntereses() {
        return intereses;
    }

    public void setIntereses(ArrayList<String> intereses) {
        this.intereses = intereses;
    }

    public boolean tieneInteres(String region) {
        for (String interes : intereses) {
            if (interes.equalsIgnoreCase(region)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getRol() {
        return "TURISTA";
    }

    @Override
    public String getDetalle() {
        return "Intereses: " + String.join(", ", intereses);
    }
}
