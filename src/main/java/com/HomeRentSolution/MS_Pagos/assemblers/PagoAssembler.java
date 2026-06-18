package com.HomeRentSolution.MS_Pagos.assemblers;

import com.HomeRentSolution.MS_Pagos.controller.PagoV2Controller;
import com.HomeRentSolution.MS_Pagos.dto.PagoResponseDTO;
import com.HomeRentSolution.MS_Pagos.model.Pago;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class PagoAssembler extends RepresentationModelAssemblerSupport <Pago, PagoResponseDTO> {

    public PagoAssembler(){

        super(PagoV2Controller.class, PagoResponseDTO.class);
    }

    @Override
    public PagoResponseDTO toModel(Pago entidad) {
        PagoResponseDTO dto = new PagoResponseDTO();

        dto.setIdPago(entidad.getIdPago());
        dto.setIdReserva(entidad.getIdReserva());
        dto.setIdPropiedad(entidad.getIdPropiedad());
        dto.setIdInquilino(entidad.getIdInquilino());
        dto.setMontoTotal(entidad.getMontoTotal());
        dto.setMontoPagado(entidad.getMontoPagado());
        dto.setFechaPago(entidad.getFechaPago());
        dto.setFechaVencimiento(entidad.getFechaVencimiento());
        dto.setEstadoPago(entidad.getEstadoPago());

        dto.add(linkTo(methodOn(PagoV2Controller.class).obtenerPorId(entidad.getIdPago())).withSelfRel());

        return dto;
    }

}
