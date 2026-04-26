package com.hotel.paraiso.dto;

import com.hotel.paraiso.model.Factura.EstadoFactura;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FacturaDTO {

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Request {

        @NotNull(message = "El ID de la reserva es obligatorio")
        private Long reservaId;

        @DecimalMin(value = "0.00")
        private BigDecimal descuento;

        @DecimalMin(value = "0.00") @DecimalMax(value = "100.00")
        private BigDecimal impuestoPorcentaje;

        @Size(max = 500)
        private String notas;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Response {
        private Long id;
        private String numeroFactura;
        private BigDecimal subtotal;
        private BigDecimal impuestoPorcentaje;
        private BigDecimal impuestoValor;
        private BigDecimal descuento;
        private BigDecimal total;
        private String notas;
        private EstadoFactura estadoFactura;
        private LocalDateTime fechaEmision;
        private Long reservaId;
        private String codigoReserva;
    }
}
