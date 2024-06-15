package com.odaat.odaat.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {
    @Size(max = 32)
    private String name;
}
