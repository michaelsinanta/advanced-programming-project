package id.ac.ui.cs.advprog.bayarservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HealthCheckController {

    @GetMapping("/health")
    public @ResponseBody String healthCheck() {
        return "OK";
    }
}
