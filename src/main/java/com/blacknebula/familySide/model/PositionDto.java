package com.blacknebula.familySide.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PositionDto {

    private String username;
    private double lat;
    private double lang;

}