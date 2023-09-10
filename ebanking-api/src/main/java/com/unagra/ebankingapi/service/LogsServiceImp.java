package com.unagra.ebankingapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unagra.ebankingapi.dto.LogsDTO;
import com.unagra.ebankingapi.entities.ebanking.Logs;
import com.unagra.ebankingapi.repository.ebanking.LogsRepository;

@Service
public class LogsServiceImp implements LogsService {

    @Autowired
    private LogsRepository logsRepository;

    @Override
    public LogsDTO saveValues(LogsDTO logsDTO) {
        // convert DTO to Entity...
        Logs logs = mapToEntity(logsDTO);

        // Save Data...
        Logs newRecord = logsRepository.save(logs);

        // convert entity to DTO...
        LogsDTO logsResponse = mapToDTO(newRecord);

        // return dto to json...
        return logsResponse;
    }

    // convert Entity to DTO...
    private LogsDTO mapToDTO(Logs logs) {
        LogsDTO logsDTO = new LogsDTO();
        logsDTO.setAction(logs.getAction());
        logsDTO.setCustomerid(logs.getCustomerid());
        logsDTO.setDevice(logs.getDevice());
        logsDTO.setEnviormentid(logs.getEnviormentid());
        logsDTO.setEventdate(logs.getEventdate());
        logsDTO.setEventdatetime(logs.getEventdatetime());
        logsDTO.setGeocodingid(logs.getGeocodingid());
        logsDTO.setId(logs.getId());
        logsDTO.setLocalip(logs.getLocalip());
        logsDTO.setModuleid(logs.getModuleid());
        logsDTO.setPublicip(logs.getPublicip());
        logsDTO.setYear(logs.getYear());
        return logsDTO;
    }

    // convert DTO to Entity...
    private Logs mapToEntity(LogsDTO logsDTO) {
        Logs logs = new Logs();
        logs.setAction(logsDTO.getAction());
        logs.setCustomerid(logsDTO.getCustomerid());
        logs.setDevice(logsDTO.getDevice());
        logs.setEnviormentid(logsDTO.getEnviormentid());
        logs.setEventdate(logsDTO.getEventdate());
        logs.setEventdatetime(logsDTO.getEventdatetime());
        logs.setGeocodingid(logsDTO.getGeocodingid());
        logs.setId(logsDTO.getId());
        logs.setLocalip(logsDTO.getLocalip());
        logs.setModuleid(logsDTO.getModuleid());
        logs.setPublicip(logsDTO.getPublicip());
        logs.setYear(logsDTO.getYear());
        return logs;
    }
}
