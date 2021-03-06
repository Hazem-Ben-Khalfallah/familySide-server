package com.blacknebula.familySide.user;

public class UserDto {

    private String id;
    private String username;
    private String password;
    private String email;

    @SuppressWarnings("unused")
    public UserDto() {
    }

    private UserDto(Builder builder) {
        setId(builder.id);
        setUsername(builder.username);
        setPassword(builder.password);
        setEmail(builder.email);
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public static final class Builder {
        private String id;
        private String username;
        private String password;
        private String email;

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

        public Builder password(String val) {
            password = val;
            return this;
        }

        public Builder email(String val) {
            email = val;
            return this;
        }

        public UserDto build() {
            return new UserDto(this);
        }
    }
}