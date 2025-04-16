package com.aluracursos.conversormonedas.principal;

import com.aluracursos.conversormonedas.modelos.ApiMonedas;
import com.aluracursos.conversormonedas.modelos.HistorialMonedas;
import com.aluracursos.conversormonedas.modelos.Resultado;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;

public class FormConversorMonedas {
    private JPanel miPanel;
    private JComboBox<String> comboDivisa2;
    private JButton convertirButton;
    private JComboBox<String> comboDivisa1;
    private JTextField txtCantidad;
    private JLabel lblValor;
    private JLabel lblTotal;
    private JTable tablaResultados;
    private final ApiMonedas apiMonedas = new ApiMonedas();
    private HashMap<String,String> monedas;
    private final HistorialMonedas historialMonedas = new HistorialMonedas();
    private final DefaultTableModel model = new DefaultTableModel(new Object[]{"Moneda 1", "Moneda 2","Cantidad","Valor","Total","Fecha"},0);

    private void initComponents(){
        monedas = apiMonedas.obtenerMonedas();
        tablaResultados.setModel(model);
        convertirButton.setEnabled(false);
        tablaResultados.getColumnModel().getColumn(0).setPreferredWidth(120);
        tablaResultados.getColumnModel().getColumn(1).setPreferredWidth(120);
        for (String moneda : monedas.keySet()) {
            comboDivisa1.addItem(moneda);
            comboDivisa2.addItem(moneda);
        }
        comboDivisa1.setSelectedIndex(3);
        convertirButton.addActionListener(e -> {
            convertirDivisas();
        });
        txtCantidad.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '.') {
                    e.consume(); // Evita que se ingrese caracteres no permitidos
                }
                if(txtCantidad.getText().isEmpty()){
                    convertirButton.setEnabled(false);
                }else{
                    convertirButton.setEnabled(true);
                }
            }
        });

        mostrarHistorial();
    }
    private void mostrarHistorial(){
       var resultados = historialMonedas.obtenerResultados();
       for (Resultado resultado: resultados){
           model.addRow(resultado.getDatos());
       }
    }
    private void convertirDivisas(){
        String moneda1 = comboDivisa1.getSelectedItem() + "";
        String moneda2 = comboDivisa2.getSelectedItem() + "";
        String cantidad = txtCantidad.getText();
        String moneda1Codigo = monedas.get(moneda1);
        String moneda2Codigo = monedas.get(moneda2);
        Resultado resultado = apiMonedas.obtenerResultado(moneda1Codigo, moneda2Codigo, cantidad);
        lblValor.setText("1 "+moneda1Codigo+" = "+ resultado.getValor());
        lblTotal.setText("Total: " + resultado.getTotal()+" "+moneda2Codigo);
        resultado.setCantidad(cantidad);
        resultado.setMoneda1(moneda1Codigo+"-"+moneda1);
        resultado.setMoneda2(moneda2Codigo+"-"+moneda2);
        model.addRow(resultado.getDatos());
        historialMonedas.guardarResultado(resultado);
    }
    public FormConversorMonedas() {
        initComponents();
    }


    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }

        JFrame frame = new JFrame("Conversor de Monedas");
        frame.setContentPane(new FormConversorMonedas().miPanel);
        frame.getContentPane().setPreferredSize(new Dimension(800, 300));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}