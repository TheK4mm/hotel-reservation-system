package com.hotel.paraiso.dto;

import com.hotel.paraiso.model.Pago.EstadoPago;
import com.hotel.paraiso.model.Pago.MetodoPago;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PagoDTO {

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Request {

        @NotNull(message = "El monto es obligatorio")
        @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
        private BigDecimal monto;

        @NotNull(message = "El método de pago es obligatorio")
        private MetodoPago metodoPago;

        @Size(max = 100)
        private String referenciaTransaccion;

        @Size(max = 300)
        private String descripcion;

        @NotNull(message = "El ID de reserva es obligatorio")
        private Long reservaId;

        private Long facturaId;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Response {
        private Long id;
        private BigDecimal monto;
        private MetodoPago metodoPago;
        private String referenciaTransaccion;
        private EstadoPago estadoPago;
        private String descripcion;
        private LocalDateTime fechaPago;
        private Long reservaId;
        private String codigoReserva;
        private Long facturaId;
    }
}
