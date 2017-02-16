package com.blacknebula.familySide.model;

import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


/**
 * An entity to represent a UserCoordinates.
 *
 * @author Mark Paluch
 */
@Data
@Document
public class UserCoordinates {

    @Id
    private String id;
    private String username;
    private Date date;
    private Point coordinates;

    public UserCoordinates atDate(Date date) {
        this.date = date;
        return this;
    }

    public UserCoordinates withUsername(String username) {
        this.username = username;
        return this;
    }

    public UserCoordinates withCoordinates(double lat, double lng) {
        this.coordinates = new Point(new Position(lat, lng));
        return this;
    }
}