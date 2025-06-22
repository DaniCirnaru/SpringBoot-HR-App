package com.ucv.us.dto;

public class UserUpdateResponse {
    private UserDTO user;
    private boolean emailChanged;

    public UserUpdateResponse(UserDTO user, boolean emailChanged) {
        this.user = user;
        this.emailChanged = emailChanged;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public boolean isEmailChanged() {
        return emailChanged;
    }

    public void setEmailChanged(boolean emailChanged) {
        this.emailChanged = emailChanged;
    }
// Getters and setters
}

