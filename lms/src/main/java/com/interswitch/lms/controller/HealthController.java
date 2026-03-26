package com.interswitch.lms.controller;

import com.interswitch.lms.dto.response.HealthResponseDTO;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/v1")
public class HealthController {

    @GetMapping("/hello")
    public String sayHello(){
        return "Hello World";
    }

    @GetMapping("/health")
    public HealthResponseDTO healthCheck(){

        return HealthResponseDTO.builder()
                .status("UP")
                .service("lms")
                .timestamp(new Date())
                .build();
    }
}