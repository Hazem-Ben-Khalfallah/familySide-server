package com.blacknebula.familySide.authentication;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Document
class UserEntity {

    @Id
    private String id;
    private String username;
    private String email;
    private String password;
    private Date creationDate;

    public UserEntity() {
    }

    private UserEntity(Builder builder) {
        setId(builder.id);
        setUsername(builder.username);
        setEmail(builder.email);
        setPassword(builder.password);
        setCreationDate(builder.creationDate);
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }


    public static final class Builder {
        private String id;
        private String username;
        private String email;
        private String password;
        private Date creationDate;

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

        public Builder email(String val) {
            email = val;
            return this;
        }

        public Builder password(String val) {
            password = val;
            return this;
        }

        public Builder creationDate(Date val) {
            creationDate = val;
            return this;
        }

        public UserEntity build() {
            return new UserEntity(this);
        }
    }
}