package id.ac.ui.cs.advprog.bayarservice.util;

import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {
//    create private constructor to prevent instantiation
    private ResponseHandler() {
    }
    public static ResponseEntity<Object> generateResponse(Response response) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", response.getMessage());
        map.put("code", response.getCode().value());
        map.put("status", response.getStatus());
        map.put("content", response.getResponseObject());

        return new ResponseEntity<>(map, response.getCode());
    }
}