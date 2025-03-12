/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import ModelsDAO.SensorDAO;
import ModelsDAO.VolcanDAO;
import ModelsDTO.Sensor;
import ModelsDTO.Volcan;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author seth
 */
public class frmGestionSensores extends javax.swing.JFrame {

    private SensorDAO sensorDAO = new SensorDAO();
    private VolcanDAO volcanDAO = new VolcanDAO();
    private DefaultTableModel modeloTabla;
    private int volcanSeleccionadoId;
    private int sensorSeleccionadoId;
    
    /** Creates new form frmGestionSensores */
    public frmGestionSensores() {
        initComponents();
        this.setTitle("Gestión de Sensores");
        this.setLocationRelativeTo(null);
        cargarVolcanes();
        inicializarTabla();
        inicializarInterfaz();
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
                new String [] {"ID", "Ubicación", "Estado"}
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
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
        
        tblSensores.setModel(modeloTabla);
    }
    
    private void inicializarInterfaz() {
        // Inicializar estado de los botones
        btnReparar.setEnabled(false);
        btnEliminar.setEnabled(false);
        btnGuardar.setEnabled(false);
        btnNuevo.setEnabled(true);
        
        // Inicializar campos
        txtUbicacion.setText("");
        cmbEstado.setSelectedIndex(0);
        sensorSeleccionadoId = 0;
    }
    
    private void actualizarTablaSensores() {
        if (volcanSeleccionadoId <= 0) {
            return;
        }
        
        // Limpiar la tabla
        modeloTabla.setRowCount(0);
        
        // Obtener los sensores del volcán seleccionado
        List<Sensor> sensores = sensorDAO.obtenerSensoresVolcan(volcanSeleccionadoId);
        
        // Agregar los sensores a la tabla
        for (Sensor sensor : sensores) {
            modeloTabla.addRow(new Object[]{
                sensor.getID(),
                sensor.getUbicacion(),
                sensor.getEstado()
            });
        }
    }
    
    private void registrarSensor() {
        try {
            // Validar que se haya seleccionado un volcán
            if (cmbVolcanes.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un volcán", 
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Obtener y validar la ubicación
            String ubicacion = txtUbicacion.getText().trim();
            if (ubicacion.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar la ubicación del sensor", 
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Crear el objeto Sensor
            Sensor sensor = new Sensor(volcanSeleccionadoId, ubicacion);
            
            // Registrar el sensor
            int resultado = sensorDAO.registrarSensor(sensor);
            
            if (resultado > 0) {
                JOptionPane.showMessageDialog(this, "Sensor registrado correctamente", 
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                
                // Limpiar los campos
                txtUbicacion.setText("");
                
                // Actualizar la tabla
                actualizarTablaSensores();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo registrar el sensor", 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al registrar el sensor: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void actualizarSensor() {
        try {
            // Validar que se haya seleccionado un sensor
            if (sensorSeleccionadoId <= 0) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un sensor para actualizar", 
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Obtener y validar la ubicación
            String ubicacion = txtUbicacion.getText().trim();
            if (ubicacion.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar la ubicación del sensor", 
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Obtener el estado
            String estado = cmbEstado.getSelectedItem().toString();
            
            // Crear el objeto Sensor
            Sensor sensor = new Sensor(sensorSeleccionadoId, volcanSeleccionadoId, ubicacion, estado);
            
            // Actualizar el sensor
            int resultado = sensorDAO.actualizarSensor(sensor);
            
            if (resultado > 0) {
                JOptionPane.showMessageDialog(this, "Sensor actualizado correctamente", 
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                
                // Inicializar interfaz
                inicializarInterfaz();
                
                // Actualizar la tabla
                actualizarTablaSensores();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar el sensor", 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar el sensor: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void eliminarSensor() {
        try {
            // Validar que se haya seleccionado un sensor
            if (sensorSeleccionadoId <= 0) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un sensor para eliminar", 
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Confirmar eliminación
            int opcion = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea eliminar este sensor?", 
                    "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            
            if (opcion == JOptionPane.YES_OPTION) {
                // Eliminar el sensor
                int resultado = sensorDAO.eliminarSensor(sensorSeleccionadoId);
                
                if (resultado > 0) {
                    JOptionPane.showMessageDialog(this, "Sensor eliminado correctamente", 
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Inicializar interfaz
                    inicializarInterfaz();
                    
                    // Actualizar la tabla
                    actualizarTablaSensores();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar el sensor", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar el sensor: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void repararSensor() {
        try {
            // Validar que se haya seleccionado un sensor
            if (sensorSeleccionadoId <= 0) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un sensor para reparar", 
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Reparar el sensor
            int resultado = sensorDAO.repararSensor(sensorSeleccionadoId);
            
            if (resultado > 0) {
                JOptionPane.showMessageDialog(this, "Sensor reparado correctamente", 
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                
                // Inicializar interfaz
                inicializarInterfaz();
                
                // Actualizar la tabla
                actualizarTablaSensores();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo reparar el sensor", 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al reparar el sensor: " + ex.getMessage(), 
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
        txtUbicacion = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cmbEstado = new javax.swing.JComboBox<>();
        btnGuardar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSensores = new javax.swing.JTable();
        btnCerrar = new javax.swing.JButton();
        btnReparar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Gestión de Sensores");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del Sensor"));

        jLabel2.setText("Volcán:");

        cmbVolcanes.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione un volcán" }));
        cmbVolcanes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbVolcanesActionPerformed(evt);
            }
        });

        jLabel3.setText("Ubicación:");

        jLabel4.setText("Estado:");

        cmbEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Operativo", "Falla", "Reparación" }));

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
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
                    .addComponent(txtUbicacion)
                    .addComponent(cmbEstado, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
                    .addComponent(btnNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cmbVolcanes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNuevo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtUbicacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGuardar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cmbEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblSensores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "Ubicación", "Estado"
            }
        ));
        tblSensores.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSensoresMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblSensores);

        btnCerrar.setText("Cerrar");
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });

        btnReparar.setText("Reparar");
        btnReparar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRepararActionPerformed(evt);
            }
        });

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
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
                        .addComponent(btnReparar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReparar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
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
            actualizarTablaSensores();
        }
    }                                           

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {                                         
        registrarSensor();
    }                                        

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {                                           
        actualizarSensor();
    }                                          

    private void tblSensoresMouseClicked(java.awt.event.MouseEvent evt) {                                         
        if (tblSensores.getSelectedRow() != -1) {
            sensorSeleccionadoId = Integer.parseInt(tblSensores.getValueAt(tblSensores.getSelectedRow(), 0).toString());
            String ubicacion = tblSensores.getValueAt(tblSensores.getSelectedRow(), 1).toString();
            String estado = tblSensores.getValueAt(tblSensores.getSelectedRow(), 2).toString();
            
            txtUbicacion.setText(ubicacion);
            cmbEstado.setSelectedItem(estado);
            
            btnGuardar.setEnabled(true);
            btnReparar.setEnabled(true);
            btnEliminar.setEnabled(true);
        }
    }                                        

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {                                          
        this.dispose();
    }                                         

    private void btnRepararActionPerformed(java.awt.event.ActionEvent evt) {                                           
        repararSensor();
    }                                          

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {                                            
        eliminarSensor();
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
            java.util.logging.Logger.getLogger(frmGestionSensores.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmGestionSensores.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmGestionSensores.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmGestionSensores.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmGestionSensores().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnReparar;
    private javax.swing.JComboBox<String> cmbEstado;
    private javax.swing.JComboBox<String> cmbVolcanes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblSensores;
    private javax.swing.JTextField txtUbicacion;
    // End of variables declaration                   
}