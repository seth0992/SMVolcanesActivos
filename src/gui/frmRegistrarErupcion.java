/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import ModelsDAO.ErupcionDAO;
import ModelsDAO.VolcanDAO;
import ModelsDTO.Erupcion;
import ModelsDTO.Volcan;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author seth
 */
public class frmRegistrarErupcion extends javax.swing.JFrame {

    private ErupcionDAO erupcionDAO = new ErupcionDAO();
    private VolcanDAO volcanDAO = new VolcanDAO();
    private DefaultTableModel modeloTabla;
    private int volcanSeleccionadoId;
    
    /** Creates new form frmRegistrarErupcion */
    public frmRegistrarErupcion() {
        initComponents();
        this.setTitle("Registrar Erupciones");
        this.setLocationRelativeTo(null);
        cargarVolcanes();
        inicializarTabla();
    }
    
    private void cargarVolcanes() {
        try {
            List<Volcan> volcanes = volcanDAO.obtenerVolcanesConFiltro(null, null, null);
            DefaultComboBoxModel<String> modelo = new DefaultComboBoxModel<>();
            
            for (Volcan volcan : volcanes) {
                modelo.addElement(volcan.getID() + " - " + volcan.getNombre());
            }
            
            cmbVolcanes.setModel(modelo);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar los volcanes: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void inicializarTabla() {
        modeloTabla = new DefaultTableModel(
                new Object [][] {},
                new String [] {"ID", "Fecha", "Tipo", "Duración (horas)"}
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };
        
        tblErupciones.setModel(modeloTabla);
    }
    
    private void actualizarTablaErupciones() {
        if (volcanSeleccionadoId <= 0) {
            return;
        }
        
        // Limpiar la tabla
        modeloTabla.setRowCount(0);
        
        // Obtener las erupciones del volcán seleccionado
        List<Erupcion> erupciones = erupcionDAO.obtenerErupcionesVolcan(volcanSeleccionadoId);
        
        // Agregar las erupciones a la tabla
        for (Erupcion erupcion : erupciones) {
            modeloTabla.addRow(new Object[]{
                erupcion.getID(),
                erupcion.getFechaFormateada(),
                erupcion.getTipo(),
                erupcion.getDuracion_Horas()
            });
        }
    }
    
    private void registrarErupcion() {
        try {
            // Validar que se haya seleccionado un volcán
            if (cmbVolcanes.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un volcán", 
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Obtener y validar el tipo de erupción
            String tipo = cmbTipoErupcion.getSelectedItem().toString();
            
            // Obtener y validar la duración
            String duracionStr = txtDuracion.getText().trim();
            if (duracionStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar la duración de la erupción", 
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int duracion;
            try {
                duracion = Integer.parseInt(duracionStr);
                if (duracion <= 0) {
                    JOptionPane.showMessageDialog(this, "La duración debe ser mayor a 0", 
                            "Validación", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "La duración debe ser un número entero válido", 
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Crear el objeto Erupcion
            Erupcion erupcion = new Erupcion(volcanSeleccionadoId, tipo, duracion);
            
            // Registrar la erupción
            int resultado = erupcionDAO.registrarErupcion(erupcion);
            
            if (resultado > 0) {
                JOptionPane.showMessageDialog(this, "Erupción registrada correctamente", 
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                
                // Limpiar los campos
                txtDuracion.setText("");
                cmbTipoErupcion.setSelectedIndex(0);
                
                // Actualizar la tabla
                actualizarTablaErupciones();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo registrar la erupción", 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al registrar la erupción: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cmbVolcanes = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        cmbTipoErupcion = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        txtDuracion = new javax.swing.JTextField();
        btnRegistrar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblErupciones = new javax.swing.JTable();
        btnCerrar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Registro de Erupciones");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos de la Erupción"));

        jLabel2.setText("Volcán:");

        cmbVolcanes.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione un volcán" }));
        cmbVolcanes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbVolcanesActionPerformed(evt);
            }
        });

        jLabel3.setText("Tipo:");

        cmbTipoErupcion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Explosiva", "Efusiva", "Freatomagmática" }));

        jLabel4.setText("Duración (horas):");

        btnRegistrar.setText("Registrar Erupción");
        btnRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cmbVolcanes, 0, 307, Short.MAX_VALUE)
                    .addComponent(cmbTipoErupcion, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtDuracion))
                .addGap(18, 18, 18)
                .addComponent(btnRegistrar, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(cmbVolcanes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(cmbTipoErupcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtDuracion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblErupciones.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Fecha", "Tipo", "Duración (horas)"
            }
        ));
        jScrollPane1.setViewportView(tblErupciones);

        btnCerrar.setText("Cerrar");
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>                        

    private void cmbVolcanesActionPerformed(java.awt.event.ActionEvent evt) {                                            
        if (cmbVolcanes.getSelectedIndex() != -1) {
            String seleccionado = cmbVolcanes.getSelectedItem().toString();
            String[] partes = seleccionado.split(" - ");
            volcanSeleccionadoId = Integer.parseInt(partes[0]);
            actualizarTablaErupciones();
        }
    }                                           

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {                                             
        registrarErupcion();
    }                                            

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {                                          
        this.dispose();
    }                                         

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmRegistrarErupcion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmRegistrarErupcion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmRegistrarErupcion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmRegistrarErupcion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmRegistrarErupcion().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JComboBox<String> cmbTipoErupcion;
    private javax.swing.JComboBox<String> cmbVolcanes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblErupciones;
    private javax.swing.JTextField txtDuracion;
    // End of variables declaration                   
}