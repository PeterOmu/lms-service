package com.interswitch.lms.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Builder
public class HealthResponseDTO {
    private String status;
    private String service;
    private Date timestamp;
}
