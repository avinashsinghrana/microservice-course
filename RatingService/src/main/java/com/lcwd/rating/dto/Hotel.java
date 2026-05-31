package com.lcwd.rating.dto;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Hotel {
    private Long id;
    private String name;
    private String location;
    private String about;
}
