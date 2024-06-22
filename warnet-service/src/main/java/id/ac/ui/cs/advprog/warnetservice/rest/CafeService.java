package id.ac.ui.cs.advprog.warnetservice.rest;

import id.ac.ui.cs.advprog.warnetservice.dto.rest.MenuItemDTO;
import id.ac.ui.cs.advprog.warnetservice.dto.rest.OrderRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CafeService {
    private final WebClient webClient;
    public CafeService(){
        webClient = WebClient.builder().baseUrl("http://adpro.iyoubee.xyz/cafe/")
                .build();
    }
    public String createOrderFromMicroservice(OrderRequest orderRequest) {
        return webClient.post().uri("/order/create?from=warnet")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(BodyInserters.fromValue(orderRequest))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
    public MenuItemDTO getMenuItemByIdFromMicroservice(String id) {
        return webClient.get().uri("/menu/id/" + id)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(MenuItemDTO.class)
                .block();
    }
}
