package com.mysite.jump.user;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_UESR");

    private String value;

    UserRole(String value) {
        this.value = value;
    }
}
