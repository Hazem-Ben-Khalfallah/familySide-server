package com.blacknebula.familySide.authentication;

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
class UserEntity {

    @Id
    private String username;
    private String email;
    private String password;
    private Date creationDate;

}