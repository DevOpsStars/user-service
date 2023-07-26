package com.devops.userservice.model;

public enum Role {
    GUEST,
    HOST,
    NO_ROLE;

    @Override
    public String toString() {
        switch (this.ordinal()) {
            case 0:
                return "Guest";
            case 1:
                return "Host";
            default:
                return "No role";
        }
    }
}
