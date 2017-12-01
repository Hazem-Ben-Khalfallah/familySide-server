package com.blacknebula.familySide.tracking;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Document
class UserCoordinatesEntity {

    @Id
    private String id;
    private String username;
    private Date date;
    @GeoSpatialIndexed
    private double[] coordinates;

    public UserCoordinatesEntity() {
    }

    private UserCoordinatesEntity(Builder builder) {
        setId(builder.id);
        setUsername(builder.username);
        setDate(builder.date);
        setCoordinates(builder.coordinates);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }


    public static final class Builder {
        private String id;
        private String username;
        private Date date;
        private double[] coordinates;

        private Builder() {
        }

        public Builder id(String val) {
            id = val;
            return this;
        }

        public Builder username(String val) {
            username = val;
            return this;
        }

        public Builder date(Date val) {
            date = val;
            return this;
        }

        public Builder coordinates(double[] val) {
            coordinates = val;
            return this;
        }

        public UserCoordinatesEntity build() {
            return new UserCoordinatesEntity(this);
        }
    }
}