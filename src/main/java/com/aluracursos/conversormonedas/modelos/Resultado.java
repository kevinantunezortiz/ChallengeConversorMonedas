package com.aluracursos.conversormonedas.modelos;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;

public record Resultado(
        @SerializedName("conversion_rate") double valor,
        @SerializedName("conversion_result") double total
        ){
}
