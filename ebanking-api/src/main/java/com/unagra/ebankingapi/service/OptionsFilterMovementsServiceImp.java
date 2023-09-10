package com.unagra.ebankingapi.service;

import com.unagra.ebankingapi.dto.OptionsFilterMovementsDTO;
import com.unagra.ebankingapi.entities.ebanking.OptionsFilterMovements;
import com.unagra.ebankingapi.repository.ebanking.OptionsFilterMovementsRepository;
import org.hibernate.query.IllegalQueryOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OptionsFilterMovementsServiceImp implements OptionsFilterMovementsService{

    @Autowired
    private OptionsFilterMovementsRepository optionsFilterMovementsRepository;

    @Override
    public List<OptionsFilterMovements> getAllOptions() {
        //set DTO to return this data...
        List<OptionsFilterMovements> optionsFilterMovements = null;

        try{
            //find information about the optoins availables...
            optionsFilterMovements = optionsFilterMovementsRepository.findAll(Sort.by(Sort.Direction.ASC,"description"));
        }catch (InvalidDataAccessResourceUsageException ex){
            //notice...
            throw new IllegalQueryOperationException("¡Ups!, lo sentimos, al parecer no contamos con el listado de filtro a utilizar en las consultas de movimientos.");
        }

        return optionsFilterMovements;
    }
}
