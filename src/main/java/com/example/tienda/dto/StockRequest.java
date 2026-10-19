package com.example.tienda.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StockRequest(

    @NotBlank 
    String action,
    @NotNull 
    Integer cantidad

) {

}
