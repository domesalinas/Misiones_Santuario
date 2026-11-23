import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;

public class VentanaSantuario extends JFrame {

    private final GestorCaballeros gestor;
    private final Runnable onDatosActualizados;

    private final Border bordeNormal = UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border");
    private final Border bordeError = BorderFactory.createLineBorder(new Color(204, 0, 0), 2);

    // Componentes enlazados con VentanaSantuario.form (IntelliJ Swing UI Designer)
    private JPanel panelPrincipal;
    private JTabbedPane tabbedPane1;

    // Pestaña 1: Registro
    private JPanel panelRegistro;
    private JTextField txtRegId;
    private JTextField txtRegNombre;
    private JComboBox<String> cmbRegRango;
    private JTextField txtRegConstelacion;
    private JTextField txtRegNivelPoder;
    private JTextField txtRegMision;
    private JTextField txtRegDificultad;
    private JTextField txtRegRecompensa;
    private JButton btnLimpiar;
    private JButton btnRegistrar;

    // Pestaña 2: Modificación por ID
    private JPanel panelModificar;
    private JTextField txtBuscarId;
    private JButton btnBuscar;
    private JTextField txtModId;
    private JTextField txtModNombre;
    private JComboBox<String> cmbModRango;
    private JTextField txtModConstelacion;
    private JTextField txtModNivelPoder;
    private JTextField txtModMision;
    private JTextField txtModDificultad;
    private JTextField txtModRecompensa;
    private JButton btnCancelar;
    private JButton btnGuardarCambios;

    public VentanaSantuario() {
        this(new GestorCaballeros(), null);
    }

    public VentanaSantuario(GestorCaballeros gestor, Runnable onDatosActualizados) {
        this.gestor = gestor;
        this.onDatosActualizados = onDatosActualizados;

        setTitle("Santuario de Atena - Gestión de Misiones");
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);

        habilitarCamposEdicion(false);
        configurarEventos();
    }

    private void configurarEventos() {
        btnRegistrar.addActionListener(e -> registrarCaballero());
        btnLimpiar.addActionListener(e -> limpiarCamposRegistro());
        btnBuscar.addActionListener(e -> buscarCaballeroPorId());
        txtBuscarId.addActionListener(e -> buscarCaballeroPorId());
        btnGuardarCambios.addActionListener(e -> guardarCambiosCaballero());
        btnCancelar.addActionListener(e -> limpiarCamposModificacion());
    }

    private void habilitarCamposEdicion(boolean habilitar) {
        txtModNombre.setEnabled(habilitar);
        cmbModRango.setEnabled(habilitar);
        txtModConstelacion.setEnabled(habilitar);
        txtModNivelPoder.setEnabled(habilitar);
        txtModMision.setEnabled(habilitar);
        txtModDificultad.setEnabled(habilitar);
        txtModRecompensa.setEnabled(habilitar);
        btnGuardarCambios.setEnabled(habilitar);
    }

    private void limpiarBordesRegistro() {
        txtRegId.setBorder(bordeNormal);
        txtRegNombre.setBorder(bordeNormal);
        txtRegConstelacion.setBorder(bordeNormal);
        txtRegNivelPoder.setBorder(bordeNormal);
        txtRegMision.setBorder(bordeNormal);
        txtRegDificultad.setBorder(bordeNormal);
        txtRegRecompensa.setBorder(bordeNormal);
    }

    private void limpiarCamposRegistro() {
        limpiarBordesRegistro();
        txtRegId.setText("");
        txtRegNombre.setText("");
        cmbRegRango.setSelectedIndex(0);
        txtRegConstelacion.setText("");
        txtRegNivelPoder.setText("");
        txtRegMision.setText("");
        txtRegDificultad.setText("");
        txtRegRecompensa.setText("");
        txtRegId.requestFocus();
    }

    private void limpiarBordesModificacion() {
        txtBuscarId.setBorder(bordeNormal);
        txtModNombre.setBorder(bordeNormal);
        txtModConstelacion.setBorder(bordeNormal);
        txtModNivelPoder.setBorder(bordeNormal);
        txtModMision.setBorder(bordeNormal);
        txtModDificultad.setBorder(bordeNormal);
        txtModRecompensa.setBorder(bordeNormal);
    }

    private void limpiarCamposModificacion() {
        limpiarBordesModificacion();
        txtBuscarId.setText("");
        txtModId.setText("");
        txtModNombre.setText("");
        cmbModRango.setSelectedIndex(0);
        txtModConstelacion.setText("");
        txtModNivelPoder.setText("");
        txtModMision.setText("");
        txtModDificultad.setText("");
        txtModRecompensa.setText("");
        habilitarCamposEdicion(false);
        txtBuscarId.requestFocus();
    }

    private void registrarCaballero() {
        limpiarBordesRegistro();
        StringBuilder errores = new StringBuilder();

        String id = txtRegId.getText().trim();
        String nombre = txtRegNombre.getText().trim();
        String rango = String.valueOf(cmbRegRango.getSelectedItem());
        String constelacion = txtRegConstelacion.getText().trim();
        String mision = txtRegMision.getText().trim();

        if (id.isEmpty()) {
            txtRegId.setBorder(bordeError);
            errores.append("- El ID es obligatorio.\n");
        }
        if (nombre.isEmpty()) {
            txtRegNombre.setBorder(bordeError);
            errores.append("- El nombre es obligatorio.\n");
        }
        if (constelacion.isEmpty()) {
            txtRegConstelacion.setBorder(bordeError);
            errores.append("- La constelación es obligatoria.\n");
        }
        if (mision.isEmpty()) {
            txtRegMision.setBorder(bordeError);
            errores.append("- La misión es obligatoria.\n");
        }

        int nivelPoder = 0;
        try {
            nivelPoder = Integer.parseInt(txtRegNivelPoder.getText().trim());
            if (nivelPoder < 1 || nivelPoder > 10) {
                txtRegNivelPoder.setBorder(bordeError);
                errores.append("- El nivel de poder debe estar entre 1 y 10.\n");
            }
        } catch (NumberFormatException ex) {
            txtRegNivelPoder.setBorder(bordeError);
            errores.append("- El nivel de poder debe ser un número entero.\n");
        }

        int dificultad = 0;
        try {
            dificultad = Integer.parseInt(txtRegDificultad.getText().trim());
            if (dificultad < 1 || dificultad > 5) {
                txtRegDificultad.setBorder(bordeError);
                errores.append("- La dificultad debe estar entre 1 y 5.\n");
            }
        } catch (NumberFormatException ex) {
            txtRegDificultad.setBorder(bordeError);
            errores.append("- La dificultad debe ser un número entero.\n");
        }

        double recompensa = 0.0;
        try {
            recompensa = Double.parseDouble(txtRegRecompensa.getText().trim());
            if (!Double.isFinite(recompensa) || recompensa < 0) {
                txtRegRecompensa.setBorder(bordeError);
                errores.append("- La recompensa debe ser un número finito mayor o igual a 0.\n");
            }
        } catch (NumberFormatException ex) {
            txtRegRecompensa.setBorder(bordeError);
            errores.append("- La recompensa debe ser un valor numérico válido.\n");
        }

        if (errores.length() > 0) {
            JOptionPane.showMessageDialog(this,
                    "Corrija los siguientes errores:\n\n" + errores,
                    "Error de Validación",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Caballero nuevo = new Caballero(id, nombre, rango, constelacion, nivelPoder, mision, dificultad, recompensa);
            boolean agregado = gestor.registrarCaballero(nuevo);

            if (!agregado) {
                txtRegId.setBorder(bordeError);
                JOptionPane.showMessageDialog(this,
                        "Ya existe un caballero registrado con el ID '" + id + "'.",
                        "ID Duplicado",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(this,
                    "Caballero registrado correctamente.",
                    "Registro Exitoso",
                    JOptionPane.INFORMATION_MESSAGE);
            limpiarCamposRegistro();
            if (onDatosActualizados != null) {
                onDatosActualizados.run();
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error en los datos del modelo: " + ex.getMessage(),
                    "Error de Dominio",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarCaballeroPorId() {
        limpiarBordesModificacion();
        String idBuscado = txtBuscarId.getText().trim();

        if (idBuscado.isEmpty()) {
            txtBuscarId.setBorder(bordeError);
            JOptionPane.showMessageDialog(this,
                    "Ingrese un ID para buscar.",
                    "Campo Vacío",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Caballero c = gestor.buscarPorId(idBuscado);
        if (c == null) {
            limpiarCamposModificacion();
            txtBuscarId.setText(idBuscado);
            txtBuscarId.setBorder(bordeError);
            JOptionPane.showMessageDialog(this,
                    "No se encontró ningún caballero con el ID '" + idBuscado + "'.",
                    "Sin Resultados",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        txtModId.setText(c.getId());
        txtModNombre.setText(c.getNombre());
        for (int i = 0; i < cmbModRango.getItemCount(); i++) {
            if (cmbModRango.getItemAt(i).equalsIgnoreCase(c.getRango())) {
                cmbModRango.setSelectedIndex(i);
                break;
            }
        }
        txtModConstelacion.setText(c.getConstelacion());
        txtModNivelPoder.setText(String.valueOf(c.getNivelPoder()));
        txtModMision.setText(c.getMision());
        txtModDificultad.setText(String.valueOf(c.getDificultad()));
        txtModRecompensa.setText(String.valueOf(c.getRecompensa()));

        habilitarCamposEdicion(true);
    }

    private void guardarCambiosCaballero() {
        limpiarBordesModificacion();
        StringBuilder errores = new StringBuilder();

        String id = txtModId.getText().trim();
        String nombre = txtModNombre.getText().trim();
        String rango = String.valueOf(cmbModRango.getSelectedItem());
        String constelacion = txtModConstelacion.getText().trim();
        String mision = txtModMision.getText().trim();

        if (nombre.isEmpty()) {
            txtModNombre.setBorder(bordeError);
            errores.append("- El nombre es obligatorio.\n");
        }
        if (constelacion.isEmpty()) {
            txtModConstelacion.setBorder(bordeError);
            errores.append("- La constelación es obligatoria.\n");
        }
        if (mision.isEmpty()) {
            txtModMision.setBorder(bordeError);
            errores.append("- La misión es obligatoria.\n");
        }

        int nivelPoder = 0;
        try {
            nivelPoder = Integer.parseInt(txtModNivelPoder.getText().trim());
            if (nivelPoder < 1 || nivelPoder > 10) {
                txtModNivelPoder.setBorder(bordeError);
                errores.append("- El nivel de poder debe estar entre 1 y 10.\n");
            }
        } catch (NumberFormatException ex) {
            txtModNivelPoder.setBorder(bordeError);
            errores.append("- El nivel de poder debe ser un número entero.\n");
        }

        int dificultad = 0;
        try {
            dificultad = Integer.parseInt(txtModDificultad.getText().trim());
            if (dificultad < 1 || dificultad > 5) {
                txtModDificultad.setBorder(bordeError);
                errores.append("- La dificultad debe estar entre 1 y 5.\n");
            }
        } catch (NumberFormatException ex) {
            txtModDificultad.setBorder(bordeError);
            errores.append("- La dificultad debe ser un número entero.\n");
        }

        double recompensa = 0.0;
        try {
            recompensa = Double.parseDouble(txtModRecompensa.getText().trim());
            if (!Double.isFinite(recompensa) || recompensa < 0) {
                txtModRecompensa.setBorder(bordeError);
                errores.append("- La recompensa debe ser un número finito mayor o igual a 0.\n");
            }
        } catch (NumberFormatException ex) {
            txtModRecompensa.setBorder(bordeError);
            errores.append("- La recompensa debe ser un valor numérico válido.\n");
        }

        if (errores.length() > 0) {
            JOptionPane.showMessageDialog(this,
                    "Corrija los siguientes errores:\n\n" + errores,
                    "Error de Validación",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            boolean actualizado = gestor.modificarCaballero(
                    id, nombre, rango, constelacion, nivelPoder, mision, dificultad, recompensa
            );

            if (actualizado) {
                JOptionPane.showMessageDialog(this,
                        "Caballero actualizado exitosamente.",
                        "Modificación Exitosa",
                        JOptionPane.INFORMATION_MESSAGE);
                limpiarCamposModificacion();
                if (onDatosActualizados != null) {
                    onDatosActualizados.run();
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo actualizar el registro.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error en los datos del modelo: " + ex.getMessage(),
                    "Error de Dominio",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VentanaSantuario().setVisible(true);
        });
    }
}
