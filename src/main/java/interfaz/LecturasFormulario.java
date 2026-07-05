package interfaz;

import negocio.NegocioException;
import negocio.Validaciones;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LecturasFormulario {

    public static String leerTexto(JTextField campo, String nombre) throws NegocioException {
        try {
            return Validaciones.textoObligatorio(campo.getText(), nombre);
        } catch (NegocioException e) {
            throw e;
        } catch (Exception e) {
            throw new NegocioException("No se pudo leer el campo " + nombre + ".");
        }
    }

    public static String leerNombre(JTextField campo) throws NegocioException {
        try {
            return Validaciones.nombre(campo.getText());
        } catch (NegocioException e) {
            throw e;
        } catch (Exception e) {
            throw new NegocioException("No se pudo leer el nombre.");
        }
    }

    public static String leerCorreo(JTextField campo) throws NegocioException {
        try {
            return Validaciones.correo(campo.getText());
        } catch (NegocioException e) {
            throw e;
        } catch (Exception e) {
            throw new NegocioException("No se pudo leer el correo.");
        }
    }

    public static String leerClave(JPasswordField campo) throws NegocioException {
        try {
            return Validaciones.clave(new String(campo.getPassword()));
        } catch (NegocioException e) {
            throw e;
        } catch (Exception e) {
            throw new NegocioException("No se pudo leer la contraseña.");
        }
    }

    public static String leerArea(JTextArea campo, String nombre) throws NegocioException {
        try {
            return Validaciones.textoObligatorio(campo.getText(), nombre);
        } catch (NegocioException e) {
            throw e;
        } catch (Exception e) {
            throw new NegocioException("No se pudo leer el campo " + nombre + ".");
        }
    }

    public static int leerEntero(JTextField campo, String nombre) throws NegocioException {
        try {
            int valor = Integer.parseInt(campo.getText().trim());
            return Validaciones.enteroPositivo(valor, nombre);
        } catch (NumberFormatException e) {
            throw new NegocioException("El campo " + nombre + " debe ser un número entero.");
        } catch (NegocioException e) {
            throw e;
        } catch (Exception e) {
            throw new NegocioException("No se pudo leer el campo " + nombre + ".");
        }
    }

    public static double leerDecimal(JTextField campo, String nombre) throws NegocioException {
        try {
            double valor = Double.parseDouble(campo.getText().trim());
            return Validaciones.decimalPositivo(valor, nombre);
        } catch (NumberFormatException e) {
            throw new NegocioException("El campo " + nombre + " debe ser un número válido.");
        } catch (NegocioException e) {
            throw e;
        } catch (Exception e) {
            throw new NegocioException("No se pudo leer el campo " + nombre + ".");
        }
    }

    public static LocalDate leerFecha(JTextField campo, String nombre) throws NegocioException {
        try {
            LocalDate fecha = LocalDate.parse(campo.getText().trim());
            return Validaciones.fechaNoNula(fecha, nombre);
        } catch (java.time.format.DateTimeParseException e) {
            throw new NegocioException("La fecha de " + nombre + " debe tener formato AAAA-MM-DD.");
        } catch (NegocioException e) {
            throw e;
        } catch (Exception e) {
            throw new NegocioException("No se pudo leer la fecha de " + nombre + ".");
        }
    }

    public static Object leerCombo(JComboBox<?> combo, String nombre) throws NegocioException {
        try {
            Object valor = combo.getSelectedItem();
            if (valor == null) {
                throw new NegocioException("Debe seleccionar " + nombre + ".");
            }
            return valor;
        } catch (NegocioException e) {
            throw e;
        } catch (Exception e) {
            throw new NegocioException("No se pudo leer la selección de " + nombre + ".");
        }
    }

    public static ArrayList<Object> leerLista(JList<?> lista, String nombre) throws NegocioException {
        try {
            List<?> seleccionados = lista.getSelectedValuesList();
            if (seleccionados == null || seleccionados.isEmpty()) {
                throw new NegocioException("Debe seleccionar al menos un elemento en " + nombre + ".");
            }
            ArrayList<Object> resultado = new ArrayList<Object>();
            resultado.addAll(seleccionados);
            return resultado;
        } catch (NegocioException e) {
            throw e;
        } catch (Exception e) {
            throw new NegocioException("No se pudo leer la selección de " + nombre + ".");
        }
    }
}
