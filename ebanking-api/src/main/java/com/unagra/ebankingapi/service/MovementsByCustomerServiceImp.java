package com.unagra.ebankingapi.service;

import com.unagra.ebankingapi.dto.MovementsByCustomerIndexDTO;
import com.unagra.ebankingapi.entities.bancaUNAGRA.MovementsByCustomer;
import com.unagra.ebankingapi.entities.ebanking.AccountExtraInfo;
import com.unagra.ebankingapi.exceptions.ResourceNotFoundException;
import com.unagra.ebankingapi.models.MovementsByCustomerHistoryResponse;
import com.unagra.ebankingapi.models.MovementsByCustomerIndexResponse;
import com.unagra.ebankingapi.repository.bancaUNAGRA.CatchmentMovementsRepository;
import com.unagra.ebankingapi.repository.bancaUNAGRA.MovementsByCustomerRepository;
import com.unagra.ebankingapi.repository.ebanking.AccountExtraInfoRepository;
import org.hibernate.query.IllegalQueryOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class MovementsByCustomerServiceImp implements MovementsByCustomerService {
    @Autowired
    private CatchmentMovementsRepository catchmentMovementsRepository;

    @Autowired
    private MovementsByCustomerRepository movementsByCustomerRepository;

    @Autowired
    private AccountExtraInfoRepository accountExtraInfoRepository;

    @Override
    public MovementsByCustomerIndexResponse findMovementsByCustomerIndex(Long vpAccountID) {
        AccountExtraInfo accountExtraInfo = accountExtraInfoRepository.findById(vpAccountID)
                .orElseThrow(() -> new ResourceNotFoundException("No podemos devolver el detalle de la cuenta ["
                        + vpAccountID + "], derivado a que esta no existe."));

        //get all movements by accountId for index...
        List<String> vlMovementsArray = movementsByCustomerRepository.findMovementsByCustomerIndex(vpAccountID);
        List<MovementsByCustomerIndexDTO> vlFinalArray = new ArrayList<>();

        //if we have records...
        if (!vlMovementsArray.isEmpty()) {
            // Fill DTO From Array...
            for (int i = 0; i < vlMovementsArray.size(); i++) {
                // get current value...
                String vlMainData = vlMovementsArray.get(i);

                // applying split to get individual value of each record...
                String[] vlSplit = vlMainData.split("[,]");

                // currency format...
                Double vlAmount = Double.parseDouble(vlSplit[5]);

                // Adding to DTO...
                MovementsByCustomerIndexDTO vlData = new MovementsByCustomerIndexDTO()
                        .builder()
                        .movimientoid(Long.parseLong(vlSplit[0]))
                        .cuentaid(Long.parseLong(vlSplit[1]))
                        .tipomovimiento(vlSplit[2])
                        .origenmovimiento(vlSplit[3])
                        .fechahoramovimiento(vlSplit[4])
                        .monto(NumberFormat
                                .getCurrencyInstance(new Locale.Builder().setLanguage("en").setRegion("US").build())
                                .format(vlAmount))
                        .concepto(vlSplit[6])
                        .tipooperacion(vlSplit[7])
                        .tipomovimientoid(Integer.parseInt(vlSplit[8]))
                        .fechaaplicacion(vlSplit[9])
                        .build();

                //add to final array....
                vlFinalArray.add(vlData);
            }

        } else {
            throw new ResourceNotFoundException("No tenemos movimientos de ahorro relacionados a la cuentaID [" + vpAccountID + "].");
        }

        // get current dateTime action...
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        //build response...
        MovementsByCustomerIndexResponse MovementsByCustomerResponse = new MovementsByCustomerIndexResponse()
                .builder()
                .movementsDetail(vlFinalArray)
                .dateTimeResponse(dateFormat.format(date))
                .msg("A continuación se muestran los últimos 10 movimientos entre abonos y retiros de la cuentaid [" + vpAccountID + "]")
                .build();
        return MovementsByCustomerResponse;
    }

    @Override
    public MovementsByCustomerHistoryResponse findMovementsByCustomerHistory(Long vpAccountID, Integer pageNo, Integer pageSize, Integer filterOption, String startDate, String endDate) {
        //account must exists...
        AccountExtraInfo accountExtraInfo = accountExtraInfoRepository.findById(vpAccountID)
                .orElseThrow(() -> new ResourceNotFoundException("No podemos devolver el detalle de la cuenta ["
                        + vpAccountID + "], derivado a que esta no existe."));


        // incluiding page properties...
        //Sort sort = Sort.by("fechahoramovimiento").descending();
        Sort sort = Sort.by("fechahoramovimiento").descending();
        Pageable paginador = PageRequest.of(pageNo, pageSize, sort);
        Page<MovementsByCustomer> movementsCustomerList = null;
        try {
            //declare date variables...
            Date vlStartDate = null;
            Date vlEndDate = null;

            //setting date format...
            if (startDate.trim().equals("") && endDate.trim().equals("")) {
                //Filter by current day when we don't have a date setted...
                DateFormat vlDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                vlStartDate = new SimpleDateFormat("yyyy-MM-dd").parse("1994-01-01"); //UNAGRA's begin operations...
                vlEndDate = new Date();
            } else {
                //if front send a date...
                vlStartDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
                vlEndDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
            }

            //Execute query...
            movementsCustomerList =
                    movementsByCustomerRepository.findMovementsByCustomerHistory(vpAccountID, filterOption, vlStartDate, vlEndDate, paginador);
        } catch (InvalidDataAccessResourceUsageException ex) {
            throw new IllegalQueryOperationException("¡Ups!, lo sentimos, hay un error en nuestra petición hacía nuestro servidor, paquito esta trabajando en ello para resolver el incoveniente. \n" + ex.getCause());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // get current dateTime action...
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        //build response...
        return new MovementsByCustomerHistoryResponse()
                .builder()
                .movementsDetail(movementsCustomerList.getContent())
                .dateTimeResponse(dateFormat.format(date))
                .pageNo(paginador.getPageNumber())
                .pageSize(paginador.getPageSize())
                .recordsDisplayed(movementsCustomerList.getNumberOfElements())
                .recodsTotal(movementsCustomerList.getTotalElements())
                .recodsPages(movementsCustomerList.getTotalPages())
                .msg("Esta consulta ha arrojado un total de " + movementsCustomerList.getTotalElements() + " registros, los cuales están repartidos en " + movementsCustomerList.getTotalPages() + " páginas, dichos registros están asociados a la cuentaid [" + vpAccountID + "]")
                .build();

        /*
        List<MovementsByCustomerDTO> vlFinalArray = new ArrayList<>();

        //if we have records...
        if (!vlMovementsArray.isEmpty()) {
            // Fill DTO From Array...
            for (int i = 0; i < vlMovementsArray.size(); i++) {
                // get current value...
                String vlMainData = vlMovementsArray.get(i);

                // applying split to get individual value of each record...
                String[] vlSplit = vlMainData.split("[,]");

                // currency format...
                Double vlAmount = Double.parseDouble(vlSplit[5]);

                // Adding to DTO...
                MovementsByCustomerDTO vlData = new MovementsByCustomerDTO()
                        .builder()
                        .movimientoid(Long.parseLong(vlSplit[0]))
                        .cuentaid(Long.parseLong(vlSplit[1]))
                        .tipomovimiento(vlSplit[2])
                        .origenmovimiento(vlSplit[3])
                        .fechahoramovimiento(vlSplit[4])
                        .monto(NumberFormat
                                .getCurrencyInstance(new Locale.Builder().setLanguage("en").setRegion("US").build())
                                .format(vlAmount))
                        .concepto(vlSplit[6])
                        .tipooperacion(vlSplit[7])
                        .tipomovimientoid(Integer.parseInt(vlSplit[8]))
                        .fechaaplicacion(vlSplit[9])
                        .build();

                //add to final array....
                vlFinalArray.add(vlData);
            }

        } else {
            throw new ResourceNotFoundException("No tenemos movimientos de ahorro relacionados a la cuentaID [" + vpAccountID + "].");
        }
        */


    }
}
