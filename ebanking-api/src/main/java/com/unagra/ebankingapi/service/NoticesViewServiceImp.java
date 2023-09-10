package com.unagra.ebankingapi.service;

import com.unagra.ebankingapi.dto.NoticesViewDTO;
import com.unagra.ebankingapi.entities.ebanking.Login;
import com.unagra.ebankingapi.entities.ebanking.NoticesView;
import com.unagra.ebankingapi.exceptions.ResourceNotFoundException;
import com.unagra.ebankingapi.models.GetNoticesByCustomerResponse;
import com.unagra.ebankingapi.repository.ebanking.LoginRepository;
import com.unagra.ebankingapi.repository.ebanking.NoticesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class NoticesViewServiceImp implements NoticesViewService {
    @Autowired
    private NoticesRepository noticeRepository;

    @Autowired
    private LoginRepository loginViewRepository;

    @Override
    public GetNoticesByCustomerResponse getAllNoticesByCustomer(Long customerID) {
        // search customer by ID...
        Login login = loginViewRepository.findById(customerID).orElseThrow(() -> new ResourceNotFoundException(
                "No tenemos información del clienteID [" + customerID + "], valide la información proporcionada."));

        // fin all notices by customoer...
        List<String> vlGeneralList = noticeRepository.findAllNoticesByCustomer(customerID);
        List<NoticesViewDTO> finalArray = new ArrayList<>();
        // System.out.println(vlGeneralList.toString());

        // Fill DTO From Array...
        for (int i = 0; i < vlGeneralList.size(); i++) {
            // get current value...
            String vlMainData = vlGeneralList.get(i);

            // applying split to get individual value of each record...
            String[] vlSplit = vlMainData.split("[,]");

            // Adding to DTO...
            NoticesViewDTO noticesViewDTO = new NoticesViewDTO()
                    .builder()
                    .customerid(Long.parseLong(vlSplit[3]))
                    .id(Long.parseLong(vlSplit[0]))
                    .imgid(Integer.parseInt(vlSplit[1]))
                    .msg(vlSplit[2].replace("|", ","))
                    .build();

            // add to final list...
            finalArray.add(noticesViewDTO);
        }

        // get current dateTime action...
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        // return a response...
        GetNoticesByCustomerResponse vlResponse = new GetNoticesByCustomerResponse()
                .builder()
                .msg("El clienteid [" + customerID + "] tiene un total de ["
                        + finalArray.size()
                        + "] notificaciones que podrán ser mostradas.")
                .dateTimeResponse(dateFormat.format(date))
                .noNotice(finalArray.size())
                .noticesLit(finalArray)
                .build();
        return vlResponse;
    }

    // convert Entity to DTO...
    private NoticesViewDTO mapToDTO(NoticesView noticesView) {
        NoticesViewDTO noticesViewDTO = new NoticesViewDTO();
        noticesViewDTO.setCustomerid(noticesView.getCustomerid());
        noticesViewDTO.setImgid(noticesView.getImgid());
        noticesViewDTO.setMsg(noticesView.getMsg());
        return noticesViewDTO;
    }
}
