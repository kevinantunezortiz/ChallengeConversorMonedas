package com.aluracursos.conversormonedas.modelos;

import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;

public class HistorialMonedas {
    private Gson gson = new Gson();
    public void guardarResultado(Resultado resultado)  {
        try {
            FileWriter escribirArchivo = new FileWriter("historial.txt",true);
            String json = gson.toJson(resultado);
            System.out.println(json);
            escribirArchivo.write(json+"\n");
            escribirArchivo.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public ArrayList<Resultado> obtenerResultados() {
        var resultados = new ArrayList<Resultado>();
        try {
            File file = new File("historial.txt");
            if(file.exists()){
                FileReader lector = new FileReader("historial.txt");
                BufferedReader bufferLector = new BufferedReader(lector);
                String json;
                while ((json = bufferLector.readLine()) != null) {
                    Resultado resultado = gson.fromJson(json, Resultado.class);
                    resultados.add(resultado);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }
        return resultados;
    }
}
