package com.blacknebula.familySide.connection;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author hazem
 */
@Document(collection = "connection")
class UserConnectionEntity {
    @Id
    private String id;
    // connection creator
    private String userId1;
    // connection target
    private String userId2;
    private UserConnectionStatusEnum status;

    public UserConnectionEntity() {
    }

    private UserConnectionEntity(Builder builder) {
        setId(builder.id);
        setUserId1(builder.userId1);
        setUserId2(builder.userId2);
        setStatus(builder.status);
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

    public String getUserId1() {
        return userId1;
    }

    public void setUserId1(String userId1) {
        this.userId1 = userId1;
    }

    public String getUserId2() {
        return userId2;
    }

    public void setUserId2(String userId2) {
        this.userId2 = userId2;
    }

    public UserConnectionStatusEnum getStatus() {
        return status;
    }

    public void setStatus(UserConnectionStatusEnum status) {
        this.status = status;
    }


    public static final class Builder {
        private String id;
        private String userId1;
        private String userId2;
        private UserConnectionStatusEnum status;

        private Builder() {
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder userId2(String userId2) {
            this.userId2 = userId2;
            return this;
        }

        public Builder userId1(String userId1) {
            this.userId1 = userId1;
            return this;
        }

        public Builder status(UserConnectionStatusEnum status) {
            this.status = status;
            return this;
        }

        public UserConnectionEntity build() {
            return new UserConnectionEntity(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        UserConnectionEntity that = (UserConnectionEntity) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(userId1, that.userId1)
                .append(userId2, that.userId2)
                .append(status, that.status)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(userId1)
                .append(userId2)
                .append(status)
                .toHashCode();
    }
}
