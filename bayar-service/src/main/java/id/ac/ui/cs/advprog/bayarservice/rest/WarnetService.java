package id.ac.ui.cs.advprog.bayarservice.rest;

import id.ac.ui.cs.advprog.bayarservice.dto.warnet.SessionResponse;
import id.ac.ui.cs.advprog.bayarservice.exception.warnet.SessionDoesNotExistException;
import id.ac.ui.cs.advprog.bayarservice.exception.warnet.WarnetServiceServerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class WarnetService {
    private final WebClient webClient;

    public WarnetService(@Value("${warnet.service.ip}") String warnetServiceIP) {
        this.webClient = WebClient.builder().baseUrl(warnetServiceIP).build();
    }

    public SessionResponse getSessionViaAPI(UUID sessionId) {
        return webClient.get().uri("/warnet/sewa_pc/get_session/" + sessionId.toString())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new SessionDoesNotExistException(sessionId)))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new WarnetServiceServerException(body))))
                .bodyToMono(SessionResponse.class)
                .block();
    }
}
