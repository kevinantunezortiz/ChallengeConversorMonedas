package com.aluracursos.conversormonedas.principal;

import com.aluracursos.conversormonedas.modelos.ApiDivisas;
import com.aluracursos.conversormonedas.modelos.Resultado;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FormConversorMonedas {
    private JPanel miPanel;
    private JComboBox comboDivisa2;
    private JButton convertirButton;
    private JComboBox comboDivisa1;
    private JTextField txtCantidad;
    private JLabel lblValor;
    private JLabel lblTotal;

    public FormConversorMonedas() {
        var monedas = new HashMap<String,String>();
        monedas.put("Peso Mexicano","MXN");
        monedas.put("Dólar Estadounidense","USD");
        monedas.put("Euro","EUR");
        monedas.put("Peso Argentino","ARS");
        monedas.put("Real Brasileño","BRL");
        monedas.put("Peso Colombiano","COP");
        for(String moneda:monedas.keySet()){
            comboDivisa1.addItem(moneda);
            comboDivisa2.addItem(moneda);
        }
        convertirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String divisa1 = comboDivisa1.getSelectedItem().toString();
                String divisa2 = comboDivisa2.getSelectedItem().toString();
                double cantidad = Double.parseDouble(txtCantidad.getText());
                var apiDivisas = new ApiDivisas();
                Resultado resultado = apiDivisas.obtenerResultado(monedas.get(divisa1), monedas.get(divisa2),cantidad);
                lblValor.setText("Valor: "+resultado.valor());
                lblTotal.setText("Total: "+resultado.total());
            }
        });
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
        JFrame frame = new JFrame("Conversor de Monedas");
        frame.setContentPane(new FormConversorMonedas().miPanel);
        frame.getContentPane().setPreferredSize(new Dimension(600,300));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
