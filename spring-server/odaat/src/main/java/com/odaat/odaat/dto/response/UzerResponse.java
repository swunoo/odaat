package com.odaat.odaat.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UzerResponse {
    private String name;
    private String email;
    private LocalDateTime createdAt;
}