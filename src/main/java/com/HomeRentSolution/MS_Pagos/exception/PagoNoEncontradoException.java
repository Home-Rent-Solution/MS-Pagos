package com.HomeRentSolution.MS_Pagos.exception;

public class PagoNoEncontradoException extends RuntimeException{

    public PagoNoEncontradoException(Long id) {
        super("Pago no encontrado con ID: " + id);
    }

    public PagoNoEncontradoException(String mensaje) {
        super(mensaje);
    }

}
