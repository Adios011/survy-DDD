package org.survy.domain.core.entity;

import org.survy.domain.core.valueObject.UserId;

import java.util.Random;
import java.util.UUID;

public class MyUser extends BaseEntity<UserId> {

    private String name;
    private String email;

    public MyUser(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public void initUser(){
        //TODO : validate its properties and throw appropriate exception.
        this.setId(new UserId(UUID.randomUUID()));
    }






    private MyUser(Builder builder) {
        setId(builder.id);

        name = builder.name;
        email = builder.email;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static final class Builder {
        private UserId id;
        private String name;
        private String email;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(UserId val) {
            id = val;
            return this;
        }


        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder email(String val) {
            email = val;
            return this;
        }

        public MyUser build() {
            return new MyUser(this);
        }
    }
}
