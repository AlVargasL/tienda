package com.example.tienda.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
    @NotBlank 
    String name
) {

}
