package com.unagra.ebankingapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.unagra.ebankingapi.dto.GeoCodingDTO;
import com.unagra.ebankingapi.entities.ebanking.GeoCoding;
import com.unagra.ebankingapi.repository.ebanking.GeoCodingRepository;

@Service
public class GeoCodingServiceImp implements GeoCodingService {
    @Autowired
    private GeoCodingRepository geoCodingRepository;

    @Override
    public GeoCodingDTO saveValues(GeoCodingDTO geoCodingDTO) {
        // convert DTO to Entity...
        GeoCoding geoCoding = mapToEntity(geoCodingDTO);

        // Save Data...
        GeoCoding newRecord = geoCodingRepository.save(geoCoding);

        // convert entity to DTO...
        GeoCodingDTO geoCodingResponse = mapToDTO(newRecord);

        // return dto to json...
        return geoCodingResponse;
    }

    // convert Entity to DTO...
    private GeoCodingDTO mapToDTO(GeoCoding geoCoding) {
        GeoCodingDTO geoCodingDTO = new GeoCodingDTO();
        geoCodingDTO.setAddress(geoCoding.getAddress());
        geoCodingDTO.setCountry(geoCoding.getCountry());
        geoCodingDTO.setCustomerId(geoCoding.getCustomerId());
        geoCodingDTO.setEventDate(geoCoding.getEventDate());
        geoCodingDTO.setEventDateTime(geoCoding.getEventDateTime());
        geoCodingDTO.setGeoCodingId(geoCoding.getGeoCodingId());
        geoCodingDTO.setLat(geoCoding.getLat());
        geoCodingDTO.setLng(geoCoding.getLng());
        geoCodingDTO.setLocality(geoCoding.getLocality());
        geoCodingDTO.setPlaceId(geoCoding.getPlaceId());
        geoCodingDTO.setPostalCode(geoCoding.getPostalCode());
        geoCodingDTO.setTokenRequest(geoCoding.getTokenRequest());
        geoCodingDTO.setYear(geoCoding.getYear());
        return geoCodingDTO;
    }

    // convert DTO to Entity...
    private GeoCoding mapToEntity(GeoCodingDTO geoCodingDTO) {
        GeoCoding geoCoding = new GeoCoding();
        geoCoding.setAddress(geoCodingDTO.getAddress());
        geoCoding.setCountry(geoCodingDTO.getCountry());
        geoCoding.setCustomerId(geoCodingDTO.getCustomerId());
        geoCoding.setEventDate(geoCodingDTO.getEventDate());
        geoCoding.setEventDateTime(geoCodingDTO.getEventDateTime());
        geoCoding.setGeoCodingId(geoCodingDTO.getGeoCodingId());
        geoCoding.setLat(geoCodingDTO.getLat());
        geoCoding.setLng(geoCodingDTO.getLng());
        geoCoding.setLocality(geoCodingDTO.getLocality());
        geoCoding.setPlaceId(geoCodingDTO.getPlaceId());
        geoCoding.setPostalCode(geoCodingDTO.getPostalCode());
        geoCoding.setTokenRequest(geoCodingDTO.getTokenRequest());
        geoCoding.setYear(geoCodingDTO.getYear());
        return geoCoding;
    }

}
