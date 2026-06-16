package com.HomeRentSolution.MS_Pagos.assemblers;

import com.HomeRentSolution.MS_Pagos.controller.PagoController;
import com.HomeRentSolution.MS_Pagos.dto.PagoRequestDTO;
import com.HomeRentSolution.MS_Pagos.model.Pago;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class PagoAssembler extends RepresentationModelAssembler<Pago, PagoRequestDTO> {

    public PagoAssembler(){
        super(PagoController.class, PagoRequestDTO.class);
    }

    @Override
    public PagoRequestDTO toModel(Pago entidad) {
        PagoRequestDTO dto = new PagoRequestDTO();
        dto.setIdInqulino(entidad.getIdInquilino());

    }

}
