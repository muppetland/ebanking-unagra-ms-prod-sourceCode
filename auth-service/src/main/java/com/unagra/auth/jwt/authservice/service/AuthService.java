package com.unagra.auth.jwt.authservice.service;

import com.unagra.auth.jwt.authservice.dto.AddUserDTO;
import com.unagra.auth.jwt.authservice.dto.TokenDTO;
import com.unagra.auth.jwt.authservice.entities.AuthUser;
import com.unagra.auth.jwt.authservice.entities.RefreshToken;
import com.unagra.auth.jwt.authservice.repository.AuthUserRepository;
import com.unagra.auth.jwt.authservice.repository.RefreshTokenRepository;
import com.unagra.auth.jwt.authservice.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private AuthUserRepository authUserRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    public AuthUser newUser(AddUserDTO addUserDTO) {
        //AuthUser authUserF = authUserRepository.findByCustomerID(addUserDTO.getCustomerid());
        Optional<AuthUser> user = authUserRepository.findByCustomerid(addUserDTO.getCustomerid());

        //validate if this user exists...
        if (user.isPresent()) {
            //if (authUserF != null) {
            return null;
        }

        // get current dateTime action...
        DateFormat dateFormatFull = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        DateFormat dateFormatYear = new SimpleDateFormat("yyyy");
        Date date = new Date();

        //get values from DTO...
        String password = passwordEncoder.encode(addUserDTO.getPassword());
        AuthUser authUser = new AuthUser()
                .builder()
                .year(Integer.parseInt(dateFormatYear.format(date)))
                .eventDate(date)
                .eventdatetime(date)
                .password(password)
                .customerid(addUserDTO.getCustomerid())
                .build();
        return authUserRepository.save(authUser);
    }

    public TokenDTO login(AddUserDTO addUserDTO) {
        Optional<AuthUser> user = authUserRepository.findByCustomerid(addUserDTO.getCustomerid());
        //AuthUser authUser = authUserRepository.findByCustomerID(addUserDTO.getCustomerid());

        //validate if this user exists...
        if (!user.isPresent()) {
            //if (authUser == null) {
            return null;
        }

        //if password sent match with preview data...
        if (passwordEncoder.matches(addUserDTO.getPassword(), user.get().getPassword())) {
            //if (passwordEncoder.matches(addUserDTO.getPassword(), authUser.getPassword())) {
            //we need to delete a previous refreshToke generated...
            //refreshTokenRepository.deleteUserTokenID(user.get().getId());

            //save current token to user...
            String vlToken = jwtProvider.createToken(user.get());
            //String vlToken = jwtProvider.createToken(authUser);

            //Current dateTime...
            DateFormat dateFormatFull = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            Date expirate = new Date(System.currentTimeMillis() + (1000 * 60 * 15)); //last part indicates minutes...

            //update update token to customer...
            authUserRepository.updateTokenCustomer(addUserDTO.getCustomerid(), vlToken, expirate);
            return new TokenDTO(vlToken);
        }
        return null;
    }

    public TokenDTO validateToken(String token) {
        if (!jwtProvider.validateToken(token)) {
            return null;
        }

        //get username...
        String customerid = jwtProvider.getUserNameFromToken(token);
        Optional<AuthUser> authUser = authUserRepository.findByCustomerid(Long.parseLong(customerid));
        //AuthUser authUser = authUserRepository.findByCustomerid(Long.parseLong(customerid));
        if (!authUserRepository.findByCustomerid(Long.parseLong(customerid)).isPresent()) {
            //if (authUser == null) {
            return null;
        }

        //access grant...
        return new TokenDTO(token);
    }
}
