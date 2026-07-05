package interfaz;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

public class UIUtils {
    public static final Color AMARILLO = new Color(252, 209, 22);
    public static final Color AZUL = new Color(0, 56, 147);
    public static final Color ROJO = new Color(206, 17, 38);
    public static final Color FONDO = new Color(245, 247, 250);
    public static final Color BLANCO = Color.WHITE;
    public static final Color TEXTO = new Color(31, 41, 55);
    public static final Color BORDE = new Color(218, 223, 230);

    public static void configurarTema() {
        try {
            for (UIManager.LookAndFeelInfo informacion : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(informacion.getName())) {
                    UIManager.setLookAndFeel(informacion.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Se conserva el estilo estándar de Swing si Nimbus no está disponible.
        }

        Font fuente = new Font("SansSerif", Font.PLAIN, 14);
        UIManager.put("defaultFont", fuente);
        UIManager.put("Panel.background", FONDO);
        UIManager.put("OptionPane.background", BLANCO);
        UIManager.put("OptionPane.messageForeground", TEXTO);
        UIManager.put("Label.foreground", TEXTO);
        UIManager.put("TextField.background", BLANCO);
        UIManager.put("PasswordField.background", BLANCO);
        UIManager.put("TextArea.background", BLANCO);
        UIManager.put("ComboBox.background", BLANCO);
        UIManager.put("Table.background", BLANCO);
        UIManager.put("Table.foreground", TEXTO);
        UIManager.put("Table.selectionBackground", AMARILLO);
        UIManager.put("Table.selectionForeground", TEXTO);
        UIManager.put("TabbedPane.selected", AMARILLO);
        UIManager.put("TabbedPane.background", FONDO);
        UIManager.put("Button.background", AZUL);
        UIManager.put("Button.foreground", BLANCO);
    }

    public static JPanel crearFranjaBandera() {
        JPanel franja = new JPanel(new GridLayout(3, 1));
        franja.setPreferredSize(new Dimension(10, 12));
        JPanel amarillo = new JPanel();
        JPanel azul = new JPanel();
        JPanel rojo = new JPanel();
        amarillo.setBackground(AMARILLO);
        azul.setBackground(AZUL);
        rojo.setBackground(ROJO);
        franja.add(amarillo);
        franja.add(azul);
        franja.add(rojo);
        return franja;
    }

    public static void prepararEncabezado(JPanel encabezado, JLabel titulo) {
        encabezado.setBackground(BLANCO);
        encabezado.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 3, 0, AMARILLO),
                new EmptyBorder(12, 14, 12, 14)));
        titulo.setForeground(AZUL);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 21));
    }

    public static void mostrarError(Component componente, String mensaje) {
        JOptionPane.showMessageDialog(componente, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void mostrarInformacion(Component componente, String mensaje) {
        JOptionPane.showMessageDialog(componente, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean confirmar(Component componente, String mensaje) {
        int respuesta = JOptionPane.showConfirmDialog(componente, mensaje,
                "Confirmación", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return respuesta == JOptionPane.YES_OPTION;
    }

    public static JTable crearTabla(DefaultTableModel modelo) {
        JTable tabla = new JTable(modelo);
        tabla.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabla.setAutoCreateRowSorter(true);
        tabla.setRowHeight(30);
        tabla.setShowVerticalLines(false);
        tabla.setGridColor(BORDE);
        tabla.setSelectionBackground(AMARILLO);
        tabla.setSelectionForeground(TEXTO);
        tabla.getTableHeader().setBackground(AZUL);
        tabla.getTableHeader().setForeground(BLANCO);
        tabla.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        return tabla;
    }

    public static DefaultTableModel crearModelo(String[] columnas) {
        return new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    public static void configurarCampo(JComponent componente) {
        componente.setPreferredSize(new Dimension(220, 30));
        componente.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE),
                BorderFactory.createEmptyBorder(4, 7, 4, 7)));
    }

    public static void aplicarEstilo(Component componente) {
        if (componente instanceof JButton) {
            JButton boton = (JButton) componente;
            boton.setBackground(AZUL);
            boton.setForeground(BLANCO);
            boton.setFocusPainted(false);
            boton.setFont(new Font("SansSerif", Font.BOLD, 13));
            boton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(AZUL),
                    BorderFactory.createEmptyBorder(7, 13, 7, 13)));
        } else if (componente instanceof JTabbedPane) {
            JTabbedPane pestanas = (JTabbedPane) componente;
            pestanas.setFont(new Font("SansSerif", Font.BOLD, 13));
            pestanas.setBackground(FONDO);
        } else if (componente instanceof JScrollPane) {
            JScrollPane scroll = (JScrollPane) componente;
            scroll.setBorder(BorderFactory.createLineBorder(BORDE));
            scroll.getViewport().setBackground(BLANCO);
        }

        if (componente instanceof Container) {
            Component[] hijos = ((Container) componente).getComponents();
            for (Component hijo : hijos) {
                aplicarEstilo(hijo);
            }
        }
    }
}
