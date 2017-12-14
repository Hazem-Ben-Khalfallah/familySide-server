package com.blacknebula.familySide.connection;

/**
 * @author hazem
 */
class UserConnectionDto {
    private String connectionId;
    private String username;
    private UserConnectionStatusEnum status;

    public UserConnectionDto() {
    }

    private UserConnectionDto(Builder builder) {
        setConnectionId(builder.connectionId);
        setUsername(builder.username);
        setStatus(builder.status);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserConnectionStatusEnum getStatus() {
        return status;
    }

    public void setStatus(UserConnectionStatusEnum status) {
        this.status = status;
    }


    public static final class Builder {
        private String connectionId;
        private String username;
        private UserConnectionStatusEnum status;

        private Builder() {
        }

        public Builder connectionId(String connectionId) {
            this.connectionId = connectionId;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder status(UserConnectionStatusEnum status) {
            this.status = status;
            return this;
        }

        public UserConnectionDto build() {
            return new UserConnectionDto(this);
        }
    }
}
