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
import java.util.Objects;

public class FormConversorMonedas extends  JFrame{
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

    public FormConversorMonedas()  {
        ImageIcon img = new ImageIcon(Objects.requireNonNull(FormConversorMonedas.class.getResource("/icon.png"))); // Reemplaza "icono.png" con el nombre de tu archivo
        Image icon = img.getImage();
        setIconImage(icon);
        setContentPane(miPanel);
        setTitle("Conversor de Monedas");
        getContentPane().setPreferredSize(new Dimension(800, 300));
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        monedas = apiMonedas.obtenerMonedas();
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
            public void keyReleased(KeyEvent e) {
                convertirButton.setEnabled(!txtCantidad.getText().isEmpty());
            }
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                String text = txtCantidad.getText();
                convertirButton.setEnabled(!text.isEmpty());
                if (!Character.isDigit(c) && c != '.') {
                    e.consume(); // Evita caracteres no permitidos
                }
                // Permite solo un punto decimal
                if (c == '.' && text.contains(".")) {
                    e.consume();
                }
            }
        });
        mostrarHistorial();
        convertirButton.setEnabled(false);
    }
    private void mostrarHistorial(){
        tablaResultados.setModel(model);
        tablaResultados.getColumnModel().getColumn(0).setPreferredWidth(120);
        tablaResultados.getColumnModel().getColumn(1).setPreferredWidth(120);
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

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
        FormConversorMonedas conversorMonedas = new FormConversorMonedas();
    }
}