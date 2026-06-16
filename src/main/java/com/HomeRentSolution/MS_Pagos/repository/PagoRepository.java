package com.HomeRentSolution.MS_Pagos.repository;

import com.HomeRentSolution.MS_Pagos.dto.PagoCancelacionEvento;
import com.HomeRentSolution.MS_Pagos.dto.ReservaDTO;
import com.HomeRentSolution.MS_Pagos.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PagoRepository extends JpaRepository<Pago, Long> {

    Optional<Pago> findByIdPago(Long idPago);
    List<Pago> findByIdInquilino(Long idInquilino);
    Optional<Pago> findByIdReserva(Long idReserva);
}
