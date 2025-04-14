package com.aluracursos.conversormonedas.modelos;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiDivisas {


    private String obtenerRespuesta(String divisa1, String divisa2, double cantidad) {
        String apikey = "f04170f78a1f513606b22a0b";
        String direccion = "https://v6.exchangerate-api.com/v6/"+apikey+"/pair/"+divisa1+"/"+divisa2+"/"+cantidad;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(direccion))
                .build();
        HttpResponse<String> response;
        try {
            response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return response.body();
    }
    public Resultado obtenerResultado(String divisa1, String divisa2, double cantidad){
        Gson gson = new Gson();
        String json = obtenerRespuesta(divisa1,divisa2,cantidad);
        return gson.fromJson(json, Resultado.class);
    }
}
