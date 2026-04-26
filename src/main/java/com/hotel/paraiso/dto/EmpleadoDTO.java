package com.hotel.paraiso.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class EmpleadoDTO {

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Request {

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100)
        private String nombre;

        @NotBlank(message = "El apellido es obligatorio")
        @Size(max = 100)
        private String apellido;

        @NotBlank(message = "El número de documento es obligatorio")
        @Size(max = 30)
        private String numeroDocumento;

        @NotBlank(message = "El cargo es obligatorio")
        @Size(max = 80)
        private String cargo;

        @Email(message = "Formato de email inválido")
        @Size(max = 150)
        private String emailCorporativo;

        @Size(max = 10)
        private String telefonoExtension;

        @NotNull(message = "La fecha de contratación es obligatoria")
        private LocalDate fechaContratacion;

        private Boolean activo;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Response {
        private Long id;
        private String nombre;
        private String apellido;
        private String nombreCompleto;
        private String numeroDocumento;
        private String cargo;
        private String emailCorporativo;
        private String telefonoExtension;
        private LocalDate fechaContratacion;
        private Boolean activo;
        private LocalDateTime fechaRegistro;
        private Integer totalReservasGestionadas;
    }
}
