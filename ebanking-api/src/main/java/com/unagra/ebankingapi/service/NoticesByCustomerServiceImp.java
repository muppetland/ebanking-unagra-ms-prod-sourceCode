package com.unagra.ebankingapi.service;

import com.unagra.ebankingapi.dto.NoticesByCustomerDTO;
import com.unagra.ebankingapi.entities.ebanking.NoticesByCustomer;
import com.unagra.ebankingapi.models.NoticesByCustomerResponse;
import com.unagra.ebankingapi.repository.ebanking.NoticesByCustomerRepository;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class NoticesByCustomerServiceImp implements NoticesByCustomerService {
    @Autowired
    private NoticesByCustomerRepository noticesByCustomerRepository;

    @Override
    public NoticesByCustomerResponse hideNoticeByCustomer(NoticesByCustomerDTO noticesByCustomerDTO) {
        // we need to find the id sent to hide this notice...
        NoticesByCustomer noticesByCustomer = noticesByCustomerRepository.findById(noticesByCustomerDTO.getId())
                .orElseThrow(() -> new NotFoundException("No tenemos información de la notificación con ID ["
                        + noticesByCustomerDTO.getId() + "], valide la información proporcionada."));

        // get current dateTime action...
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        // process to hide this notice...
        noticesByCustomer.setStatusid("C");
        noticesByCustomer.setNoticedatetimeattended(date);

        // now we edit the information...
        noticesByCustomerRepository.save(noticesByCustomer);

        // response...
        NoticesByCustomerResponse vlResponse = new NoticesByCustomerResponse()
                .builder()
                .dateTimeResponse(dateFormat.format(date))
                .noNotice(Integer.parseInt(noticesByCustomerDTO.getId().toString()))
                .msg("La notificación con ID[" + noticesByCustomerDTO.getId()
                        + "] ha sido ocultada para el clienteID ["
                        + noticesByCustomerDTO.getCustomerid() + "]")
                .build();
        return vlResponse;
    }

}
