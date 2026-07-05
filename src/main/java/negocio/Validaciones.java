package negocio;

import java.time.LocalDate;
import java.util.ArrayList;

public class Validaciones {

    public static String textoObligatorio(String valor, String campo) throws NegocioException {
        if (valor == null || valor.trim().isEmpty()) {
            throw new NegocioException("El campo " + campo + " es obligatorio.");
        }
        return valor.trim();
    }

    public static String nombre(String valor) throws NegocioException {
        String nombre = textoObligatorio(valor, "nombre");
        if (!nombre.matches("[A-Za-zÁÉÍÓÚáéíóúÑñÜü ]+")) {
            throw new NegocioException("El nombre solo puede contener letras y espacios.");
        }
        return nombre;
    }

    public static String correo(String valor) throws NegocioException {
        String correo = textoObligatorio(valor, "correo").toLowerCase();
        if (!correo.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new NegocioException("Ingrese un correo electrónico válido.");
        }
        return correo;
    }

    public static String clave(String valor) throws NegocioException {
        String clave = textoObligatorio(valor, "contraseña");
        if (clave.length() < 8) {
            throw new NegocioException(
                    "La contraseña debe tener al menos 8 caracteres, un número y un carácter especial.");
        }
        if (!clave.matches(".*[0-9].*")) {
            throw new NegocioException(
                    "La contraseña debe tener al menos un número.");
        }
        if (!clave.matches(".*[^A-Za-z0-9ÁÉÍÓÚáéíóúÑñÜü].*")) {
            throw new NegocioException(
                    "La contraseña debe tener al menos un carácter especial.");
        }
        return clave;
    }

    public static int enteroPositivo(int valor, String campo) throws NegocioException {
        if (valor <= 0) {
            throw new NegocioException("El campo " + campo + " debe ser mayor que cero.");
        }
        return valor;
    }

    public static double decimalPositivo(double valor, String campo) throws NegocioException {
        if (valor <= 0) {
            throw new NegocioException("El campo " + campo + " debe ser mayor que cero.");
        }
        return valor;
    }

    public static LocalDate fechaNoNula(LocalDate fecha, String campo) throws NegocioException {
        if (fecha == null) {
            throw new NegocioException("La fecha de " + campo + " es obligatoria.");
        }
        return fecha;
    }

    public static void rangoFechas(LocalDate salida, LocalDate retorno) throws NegocioException {
        fechaNoNula(salida, "salida");
        fechaNoNula(retorno, "retorno");
        if (!retorno.isAfter(salida)) {
            throw new NegocioException("La fecha de retorno debe ser posterior a la fecha de salida.");
        }
    }

    public static int calificacion(int valor, String campo) throws NegocioException {
        if (valor < 1 || valor > 5) {
            throw new NegocioException("La " + campo + " debe estar entre 1 y 5.");
        }
        return valor;
    }

    public static ArrayList<String> intereses(String texto) throws NegocioException {
        String valor = textoObligatorio(texto, "intereses");
        String[] partes = valor.split(",");
        ArrayList<String> resultado = new ArrayList<String>();
        for (String parte : partes) {
            String region = parte.trim();
            if (!region.isEmpty()) {
                if (!esRegionValida(region)) {
                    throw new NegocioException("Región no válida: " + region
                            + ". Use Costa, Sierra, Oriente o Galápagos.");
                }
                String normalizada = normalizarRegion(region);
                boolean repetida = false;
                for (String existente : resultado) {
                    if (existente.equalsIgnoreCase(normalizada)) {
                        repetida = true;
                    }
                }
                if (!repetida) {
                    resultado.add(normalizada);
                }
            }
        }
        if (resultado.isEmpty()) {
            throw new NegocioException("Debe ingresar al menos una región de interés.");
        }
        return resultado;
    }

    public static boolean esRegionValida(String region) {
        String normalizada = quitarTildes(region.toLowerCase().trim());
        return normalizada.equals("costa")
                || normalizada.equals("sierra")
                || normalizada.equals("oriente")
                || normalizada.equals("galapagos");
    }

    public static String normalizarRegion(String region) {
        String normalizada = quitarTildes(region.toLowerCase().trim());
        if (normalizada.equals("costa")) {
            return "Costa";
        }
        if (normalizada.equals("sierra")) {
            return "Sierra";
        }
        if (normalizada.equals("oriente")) {
            return "Oriente";
        }
        return "Galápagos";
    }

    private static String quitarTildes(String texto) {
        return texto.replace("á", "a")
                .replace("é", "e")
                .replace("í", "i")
                .replace("ó", "o")
                .replace("ú", "u");
    }
}
