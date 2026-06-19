package com.HomeRentSolution.MS_Pagos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

        private int status;
        private String error;
        private String mensaje;
        private LocalDateTime tiempotamp;
}
