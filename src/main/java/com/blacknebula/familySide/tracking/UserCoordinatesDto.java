package com.blacknebula.familySide.tracking;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

class UserCoordinatesDto {

    private String username;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    /* example: 2010-07-19T14:08:44+0000 */
    private Date date;

    private Double lat;

    private Double lang;

    @SuppressWarnings("unused")
    public UserCoordinatesDto() {
    }

    private UserCoordinatesDto(Builder builder) {
        setUsername(builder.username);
        setDate(builder.date);
        setLat(builder.lat);
        setLang(builder.lang);
    }

    public static Builder newBuilder() {
        return new Builder();
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

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLang() {
        return lang;
    }

    public void setLang(Double lang) {
        this.lang = lang;
    }


    public static final class Builder {
        private String username;
        private Date date;
        private Double lat;
        private Double lang;

        private Builder() {
        }

        public Builder username(String val) {
            username = val;
            return this;
        }

        public Builder date(Date val) {
            date = val;
            return this;
        }

        public Builder lat(Double val) {
            lat = val;
            return this;
        }

        public Builder lang(Double val) {
            lang = val;
            return this;
        }

        public UserCoordinatesDto build() {
            return new UserCoordinatesDto(this);
        }
    }
}