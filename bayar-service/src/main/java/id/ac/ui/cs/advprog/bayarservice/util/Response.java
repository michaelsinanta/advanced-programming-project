package id.ac.ui.cs.advprog.bayarservice.util;

import lombok.*;
import org.springframework.http.HttpStatusCode;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Response {
    private String message;
    private HttpStatusCode code;
    private String status;
    private Object responseObject;
}
