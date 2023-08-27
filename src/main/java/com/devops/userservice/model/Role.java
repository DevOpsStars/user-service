package com.devops.userservice.model;

public enum Role {
    GUEST,
    HOST,
    NO_ROLE;

    @Override
    public String toString() {
        switch (this.ordinal()) {
            case 0:
                return "guest";
            case 1:
                return "host";
            default:
                return "no role";
        }
    }
}
