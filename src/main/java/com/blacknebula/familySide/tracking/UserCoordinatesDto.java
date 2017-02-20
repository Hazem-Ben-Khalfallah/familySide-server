package com.blacknebula.familySide.tracking;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class UserCoordinatesDto {

    private String username;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    /* example: 2010-07-19T14:08:44+0000 */
    private Date date;

    private double lat;

    private double lang;

}