package id.ac.ui.cs.advprog.warnetservice.rest;

import id.ac.ui.cs.advprog.warnetservice.dto.rest.BillRequest;
import id.ac.ui.cs.advprog.warnetservice.dto.rest.InvoiceRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class BayarService {
    private final WebClient webClient;

    public BayarService(@Value("${bayar.service.ip}") String bayarServiceIp) {
        this.webClient = WebClient.builder().baseUrl(bayarServiceIp + "/api/v1/").build();
    }
    public String createInvoiceFromMicroservice(InvoiceRequest invoiceRequest) {
        return webClient.post().uri("/invoices")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(BodyInserters.fromValue(invoiceRequest))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
    public String addToBillFromMicroservice(BillRequest billRequest) {
        return webClient.post().uri("/bills")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(BodyInserters.fromValue(billRequest))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
