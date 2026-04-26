package com.hotel.paraiso.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

public class ClienteDTO {

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Request {

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100)
        private String nombre;

        @NotBlank(message = "El apellido es obligatorio")
        @Size(max = 100)
        private String apellido;

        @NotBlank(message = "El tipo de documento es obligatorio")
        private String tipoDocumento;

        @NotBlank(message = "El número de documento es obligatorio")
        @Size(max = 30)
        private String numeroDocumento;

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El formato del email no es válido")
        @Size(max = 150)
        private String email;

        @Size(max = 20, message = "El teléfono no puede superar 20 caracteres")
        private String telefono;

        @Size(max = 300)
        private String direccion;

        @Size(max = 80)
        private String pais;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Response {
        private Long id;
        private String nombre;
        private String apellido;
        private String nombreCompleto;
        private String tipoDocumento;
        private String numeroDocumento;
        private String email;
        private String telefono;
        private String direccion;
        private String pais;
        private Boolean activo;
        private LocalDateTime fechaRegistro;
        private Integer totalReservas;
    }
}
