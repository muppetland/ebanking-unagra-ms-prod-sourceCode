package com.unagra.api.gateway.config;

import com.unagra.api.gateway.dto.TokenDTO;
import jakarta.ws.rs.BadRequestException;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.Charset;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {
    private WebClient.Builder webCliente;

    private AuthFilter(WebClient.Builder webClient) {
        super(Config.class);
        this.webCliente = webClient;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            //header not contains token...
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                throw new BadRequestException("¡Ups!, lo sentimos, es necesario la información de la autorización.");
            }

            //getting information about the header to get data from token...
            String vlHeaderData = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

            //extract header|payload|signature...
            String[] vlTokenArray = vlHeaderData.split(" ");
            if (vlTokenArray.length != 2 || !vlTokenArray[0].equalsIgnoreCase("Bearer")) {
                //token with bad request...
                throw new BadRequestException("¡Ups!, lo sentimos, el token no tiene la estructura correcta.");
            }

            //return...
            return webCliente
                    .build()
                    .post()
                    .uri("http://auth-api/auth/validate?token=" + vlTokenArray[1])
                    .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN)
                    .acceptCharset(Charset.forName("UTF-8"))
                    //.retrieve().bodyToMono(TokenDTO.class)
                    .retrieve().bodyToMono(String.class)
                    .map(tokenDTO -> {
                        exchange.getRequest()
                                .mutate();
                        //.header("auth-user-id", String.valueOf(tokenDTO.getToken()));
                        return exchange;
                    }).flatMap(chain::filter);
        };
    }

    public static class Config {

    }
    /*
    private final WebClient.Builder webClientBuilder;

    public AuthFilter(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            //header not contains token...
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                throw new BadRequestException("¡Ups!, lo sentimos, es necesario la información de la autorización.");
            }

            //getting information about the header to get data from token...
            String vlHeaderData = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

            //extract header|payload|signature...
            String[] vlTokenArray = vlHeaderData.split(" ");
            if (vlTokenArray.length != 2 || !vlTokenArray[0].equalsIgnoreCase("Bearer")) {
                //token with bad request...
                throw new BadRequestException("¡Ups!, lo sentimos, el token no tiene la estructura correcta.");
            }

            //return...
            return webClientBuilder
                    .build()
                    .post()
                    .uri("http://auth-api/auth/validate?token" + vlTokenArray[1])
                    .retrieve().bodyToMono(TokenDTO.class)
                    .map(tokenDTO -> {
                        exchange.getRequest()
                                .mutate()
                                .header("auth-user-id", String.valueOf(tokenDTO.getToken()));
                        return exchange;
                    }).flatMap(chain::filter);
        };
    }

    public static class Config {

    }
     */
}
