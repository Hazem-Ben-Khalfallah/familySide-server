package com.blacknebula.familySide.connection;

/**
 * @author hazem
 */
class UserConnectionRequest {
    private String targetUsername;

    public UserConnectionRequest() {
    }

    private UserConnectionRequest(Builder builder) {
        setTargetUsername(builder.targetUsername);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getTargetUsername() {
        return targetUsername;
    }

    public void setTargetUsername(String targetUsername) {
        this.targetUsername = targetUsername;
    }


    public static final class Builder {
        private String targetUsername;

        private Builder() {
        }

        public Builder targetUsername(String targetUsername) {
            this.targetUsername = targetUsername;
            return this;
        }

        public UserConnectionRequest build() {
            return new UserConnectionRequest(this);
        }
    }
}
