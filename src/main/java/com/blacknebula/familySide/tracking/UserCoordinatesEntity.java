package com.blacknebula.familySide.tracking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
@Builder
class UserCoordinatesEntity {

    @Id
    private String id;
    private String username;
    private Date date;
    @GeoSpatialIndexed
    private double[] coordinates;

}