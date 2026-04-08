package com.victor.usersms.enums;

public enum UserStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    BLOCKED("Blocked");

    private final String name;

    UserStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
