package com.hotel.paraiso.dto;

import com.hotel.paraiso.model.Servicio.CategoriaServicio;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

public class ServicioDTO {

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Request {

        @NotBlank(message = "El nombre del servicio es obligatorio")
        @Size(max = 100)
        private String nombre;

        @Size(max = 500)
        private String descripcion;

        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.00", message = "El precio no puede ser negativo")
        private BigDecimal precio;

        @NotNull(message = "La categoría es obligatoria")
        private CategoriaServicio categoria;

        private Boolean activo;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Response {
        private Long id;
        private String nombre;
        private String descripcion;
        private BigDecimal precio;
        private CategoriaServicio categoria;
        private Boolean activo;
    }
}
