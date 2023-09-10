package com.unagra.ebankingapi.service;

import com.unagra.ebankingapi.dto.CatchmentMovementsDTO;
import com.unagra.ebankingapi.entities.bancaUNAGRA.CatchmentMovements;
import com.unagra.ebankingapi.exceptions.ResourceNotFoundException;
import com.unagra.ebankingapi.repository.bancaUNAGRA.CatchmentMovementsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CatchmentMovementsServiceImp implements CatchmentMovementsService {
    @Autowired
    private CatchmentMovementsRepository catchmentMovementsRepository;

    @Override
    public CatchmentMovementsDTO getDataFromMovementID(Long movementID) {
        CatchmentMovements vlGetData = catchmentMovementsRepository.findById(movementID)
                .orElseThrow(() -> new ResourceNotFoundException("¡Ups!, no tenemos información del clienteID [" + movementID
                        + "], valide la información proporcionada."));

        CatchmentMovementsDTO catchmentMovementsDTOO = new CatchmentMovementsDTO();
        catchmentMovementsDTOO.setDescripcion(vlGetData.getDescripcion());
        catchmentMovementsDTOO.setMovimientoid(vlGetData.getMovimientoid());
        return catchmentMovementsDTOO;
    }

}
