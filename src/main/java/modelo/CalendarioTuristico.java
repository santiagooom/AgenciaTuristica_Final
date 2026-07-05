package modelo;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarioTuristico {
    private static final String[] MESES = {
            "enero", "febrero", "marzo", "abril", "mayo", "junio",
            "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"
    };

    public static String formatearFecha(LocalDate fecha) {
        if (fecha == null) {
            return "Sin fecha";
        }
        return fecha.getDayOfMonth() + " de " + MESES[fecha.getMonthValue() - 1]
                + " de " + fecha.getYear();
    }

    public static ArrayList<LocalDate> getFechasTrimestrales2027() {
        ArrayList<LocalDate> fechas = new ArrayList<LocalDate>();
        fechas.add(LocalDate.of(2027, 1, 1));
        fechas.add(LocalDate.of(2027, 4, 1));
        fechas.add(LocalDate.of(2027, 7, 1));
        fechas.add(LocalDate.of(2027, 10, 1));
        return fechas;
    }

    public static LocalDate getFechaTrimestral(int indice) {
        ArrayList<LocalDate> fechas = getFechasTrimestrales2027();
        if (indice < 0 || indice >= fechas.size()) {
            return null;
        }
        return fechas.get(indice);
    }

    public static int getIndiceTrimestre(LocalDate fecha) {
        ArrayList<LocalDate> fechas = getFechasTrimestrales2027();
        for (int i = 0; i < fechas.size(); i++) {
            if (fechas.get(i).equals(fecha)) {
                return i;
            }
        }
        return -1;
    }

    public static boolean esFechaTrimestral2027(LocalDate fecha) {
        return getIndiceTrimestre(fecha) >= 0;
    }

    public static String getNombreTrimestre(LocalDate fecha) {
        int indice = getIndiceTrimestre(fecha);
        if (indice == 0) {
            return "Primer trimestre";
        }
        if (indice == 1) {
            return "Segundo trimestre";
        }
        if (indice == 2) {
            return "Tercer trimestre";
        }
        if (indice == 3) {
            return "Cuarto trimestre";
        }
        return "Trimestre no definido";
    }

    public static LocalDate calcularSiguienteTrimestre(LocalDate referencia) {
        ArrayList<LocalDate> fechas = getFechasTrimestrales2027();
        for (LocalDate fecha : fechas) {
            if (fecha.isAfter(referencia)) {
                return fecha;
            }
        }
        return LocalDate.of(2028, 1, 1);
    }
}
