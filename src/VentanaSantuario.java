import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.StyleContext;

public class VentanaSantuario extends JFrame {

    private final GestorCaballeros gestor;
    private final Runnable onDatosActualizados;

    private final Border bordeNormal = UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border");
    private final Border bordeError = BorderFactory.createLineBorder(new Color(204, 0, 0), 2);

    private DefaultTableModel modeloListado;
    private DefaultTableModel modeloInforme;

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

    // Pestañas 3 y 4: Listado e Informe
    private JPanel panelListado;
    private JPanel panelInforme;
    private JButton btnRefrescarListado;
    private JTable tablaCaballeros;
    private JLabel lblImagenCaballero;
    private JButton btnRefrescarInforme;
    private JTable tablaInforme;

    public VentanaSantuario() {
        this(new GestorCaballeros(), null);
    }

    public VentanaSantuario(GestorCaballeros gestor, Runnable onDatosActualizados) {
        this.gestor = gestor;
        this.onDatosActualizados = onDatosActualizados;

        inicializarComponentesSwing();

        setTitle("Santuario de Atena - Gestión de Misiones");
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);

        habilitarCamposEdicion(false);
        configurarTablasYPaneles();
        configurarEventos();
        refrescarTodo();
    }

    private void inicializarComponentesSwing() {
        panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        tabbedPane1 = new JTabbedPane();
        tabbedPane1.setPreferredSize(new Dimension(560, 460));

        construirPanelRegistro();
        construirPanelModificar();
        construirPanelListado();
        construirPanelInforme();

        tabbedPane1.addTab("Registrar Caballero", panelRegistro);
        tabbedPane1.addTab("Modificar por ID", panelModificar);
        tabbedPane1.addTab("Listado", panelListado);
        tabbedPane1.addTab("Informe", panelInforme);

        panelPrincipal.add(tabbedPane1, BorderLayout.CENTER);
    }

    private void construirPanelRegistro() {
        panelRegistro = new JPanel(new GridBagLayout());
        panelRegistro.setBorder(BorderFactory.createEmptyBorder(15, 18, 15, 18));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("Registro de Caballero y Misión", SwingConstants.CENTER);
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 16f));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelRegistro.add(lblTitulo, gbc);

        gbc.gridwidth = 1;
        txtRegId = new JTextField(18);
        txtRegNombre = new JTextField(18);
        cmbRegRango = new JComboBox<>(new DefaultComboBoxModel<>(new String[]{"Bronce", "Plata", "Oro"}));
        txtRegConstelacion = new JTextField(18);
        txtRegNivelPoder = new JTextField(18);
        txtRegMision = new JTextField(18);
        txtRegDificultad = new JTextField(18);
        txtRegRecompensa = new JTextField(18);

        agregarFilaFormulario(panelRegistro, gbc, 1, "ID Único:", txtRegId);
        agregarFilaFormulario(panelRegistro, gbc, 2, "Nombre:", txtRegNombre);
        agregarFilaFormulario(panelRegistro, gbc, 3, "Rango:", cmbRegRango);
        agregarFilaFormulario(panelRegistro, gbc, 4, "Constelación:", txtRegConstelacion);
        agregarFilaFormulario(panelRegistro, gbc, 5, "Nivel de Poder (1 - 10):", txtRegNivelPoder);
        agregarFilaFormulario(panelRegistro, gbc, 6, "Misión Asignada:", txtRegMision);
        agregarFilaFormulario(panelRegistro, gbc, 7, "Dificultad (1 - 5):", txtRegDificultad);
        agregarFilaFormulario(panelRegistro, gbc, 8, "Recompensa (Monedas):", txtRegRecompensa);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnLimpiar = new JButton("Limpiar");
        btnRegistrar = new JButton("Registrar Caballero");
        btnRegistrar.setFont(btnRegistrar.getFont().deriveFont(Font.BOLD));
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnRegistrar);

        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        panelRegistro.add(panelBotones, gbc);
    }

    private void construirPanelModificar() {
        panelModificar = new JPanel(new GridBagLayout());
        panelModificar.setBorder(BorderFactory.createEmptyBorder(15, 18, 15, 18));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 6, 5, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("Modificación de Caballero por ID", SwingConstants.CENTER);
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 16f));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelModificar.add(lblTitulo, gbc);

        JPanel panelBusqueda = new JPanel(new BorderLayout(8, 0));
        JLabel lblBuscar = new JLabel("Buscar ID:");
        txtBuscarId = new JTextField(12);
        btnBuscar = new JButton("Buscar");
        panelBusqueda.add(lblBuscar, BorderLayout.WEST);
        panelBusqueda.add(txtBuscarId, BorderLayout.CENTER);
        panelBusqueda.add(btnBuscar, BorderLayout.EAST);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panelModificar.add(panelBusqueda, gbc);

        gbc.gridy = 2;
        panelModificar.add(new JSeparator(), gbc);

        gbc.gridwidth = 1;
        txtModId = new JTextField(18);
        txtModId.setEditable(false);
        txtModId.setBackground(new Color(236, 236, 236));

        txtModNombre = new JTextField(18);
        cmbModRango = new JComboBox<>(new DefaultComboBoxModel<>(new String[]{"Bronce", "Plata", "Oro"}));
        txtModConstelacion = new JTextField(18);
        txtModNivelPoder = new JTextField(18);
        txtModMision = new JTextField(18);
        txtModDificultad = new JTextField(18);
        txtModRecompensa = new JTextField(18);

        agregarFilaFormulario(panelModificar, gbc, 3, "ID (No editable):", txtModId);
        agregarFilaFormulario(panelModificar, gbc, 4, "Nombre:", txtModNombre);
        agregarFilaFormulario(panelModificar, gbc, 5, "Rango:", cmbModRango);
        agregarFilaFormulario(panelModificar, gbc, 6, "Constelación:", txtModConstelacion);
        agregarFilaFormulario(panelModificar, gbc, 7, "Nivel de Poder (1 - 10):", txtModNivelPoder);
        agregarFilaFormulario(panelModificar, gbc, 8, "Misión Asignada:", txtModMision);
        agregarFilaFormulario(panelModificar, gbc, 9, "Dificultad (1 - 5):", txtModDificultad);
        agregarFilaFormulario(panelModificar, gbc, 10, "Recompensa (Monedas):", txtModRecompensa);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnCancelar = new JButton("Cancelar");
        btnGuardarCambios = new JButton("Guardar Cambios");
        btnGuardarCambios.setFont(btnGuardarCambios.getFont().deriveFont(Font.BOLD));
        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardarCambios);

        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 2;
        panelModificar.add(panelBotones, gbc);
    }

    private void construirPanelListado() {
        panelListado = new JPanel(new BorderLayout(8, 8));
        panelListado.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        btnRefrescarListado = new JButton("Actualizar Listado");
        tablaCaballeros = new JTable();
        JScrollPane scrollListado = new JScrollPane(tablaCaballeros);

        lblImagenCaballero = new JLabel("Seleccione un caballero", SwingConstants.CENTER);
        lblImagenCaballero.setPreferredSize(new Dimension(160, 160));
        lblImagenCaballero.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        panelListado.add(btnRefrescarListado, BorderLayout.NORTH);
        panelListado.add(scrollListado, BorderLayout.CENTER);
        panelListado.add(lblImagenCaballero, BorderLayout.SOUTH);
    }

    private void construirPanelInforme() {
        panelInforme = new JPanel(new BorderLayout(8, 8));
        panelInforme.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        btnRefrescarInforme = new JButton("Calcular Informe");
        tablaInforme = new JTable();
        JScrollPane scrollInforme = new JScrollPane(tablaInforme);

        panelInforme.add(btnRefrescarInforme, BorderLayout.NORTH);
        panelInforme.add(scrollInforme, BorderLayout.CENTER);
    }

    private void agregarFilaFormulario(JPanel panel, GridBagConstraints gbc, int fila, String etiqueta, Component campo) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0.0;
        panel.add(new JLabel(etiqueta), gbc);

        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.weightx = 1.0;
        panel.add(campo, gbc);
    }

    private void configurarTablasYPaneles() {
        modeloListado = new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Rango", "Constelación", "Poder", "Misión", "Dificultad", "Recompensa"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaCaballeros.setModel(modeloListado);
        tablaCaballeros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        modeloInforme = new DefaultTableModel(
                new Object[]{
                        "ID", "Nombre", "Rango", "Misión", "Dificultad",
                        "Recompensa", "Aporte Santuario", "Impuesto Atena", "Recompensa Neta"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaInforme.setModel(modeloInforme);
    }

    private void configurarEventos() {
        btnRegistrar.addActionListener(e -> registrarCaballero());
        btnLimpiar.addActionListener(e -> limpiarCamposRegistro());
        btnBuscar.addActionListener(e -> buscarCaballeroPorId());
        txtBuscarId.addActionListener(e -> buscarCaballeroPorId());
        btnGuardarCambios.addActionListener(e -> guardarCambiosCaballero());
        btnCancelar.addActionListener(e -> limpiarCamposModificacion());

        btnRefrescarListado.addActionListener(e -> cargarTablaListado());
        btnRefrescarInforme.addActionListener(e -> cargarTablaInforme());

        tablaCaballeros.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = tablaCaballeros.getSelectedRow();
                if (fila >= 0) {
                    String nombre = String.valueOf(modeloListado.getValueAt(fila, 1));
                    mostrarImagenCaballero(nombre);
                }
            }
        });
    }

    private void refrescarTodo() {
        cargarTablaListado();
        cargarTablaInforme();
    }

    private void cargarTablaListado() {
        modeloListado.setRowCount(0);
        List<Caballero> lista = gestor.listarCaballeros();
        if (lista != null) {
            for (Caballero c : lista) {
                modeloListado.addRow(new Object[]{
                        c.getId(),
                        c.getNombre(),
                        c.getRango(),
                        c.getConstelacion(),
                        c.getNivelPoder(),
                        c.getMision(),
                        c.getDificultad(),
                        c.getRecompensa()
                });
            }
        }
        if (modeloListado.getRowCount() == 0) {
            lblImagenCaballero.setIcon(null);
            lblImagenCaballero.setText("Sin registros");
        }
    }

    private void cargarTablaInforme() {
        modeloInforme.setRowCount(0);
        List<Caballero> lista = gestor.listarCaballeros();
        if (lista != null) {
            for (Caballero c : lista) {
                modeloInforme.addRow(new Object[]{
                        c.getId(),
                        c.getNombre(),
                        c.getRango(),
                        c.getMision(),
                        c.getDificultad(),
                        c.getRecompensa(),
                        c.calcularAporteSantuario(),
                        c.calcularImpuestoAtena(),
                        c.calcularRecompensaNeta()
                });
            }
        }
    }

    private void mostrarImagenCaballero(String nombreCaballero) {
        String nombreArchivo = nombreCaballero == null
                ? "default.png"
                : nombreCaballero.trim().toLowerCase().replace(" ", "_") + ".png";

        URL recurso = getClass().getResource("/img/" + nombreArchivo);
        if (recurso == null) {
            recurso = getClass().getResource("/img/default.png");
        }

        if (recurso != null) {
            ImageIcon original = new ImageIcon(recurso);
            int ancho = lblImagenCaballero.getWidth() > 0 ? lblImagenCaballero.getWidth() : 160;
            int alto = lblImagenCaballero.getHeight() > 0 ? lblImagenCaballero.getHeight() : 160;
            Image escalada = original.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            lblImagenCaballero.setText("");
            lblImagenCaballero.setIcon(new ImageIcon(escalada));
        } else {
            lblImagenCaballero.setIcon(null);
            lblImagenCaballero.setText("Sin imagen: " + nombreArchivo);
        }
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
            refrescarTodo();
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
                refrescarTodo();
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
