package com.licoreria.dto;

public class CambiarPasswordRequest {
    private String newPassword;

    public CambiarPasswordRequest() {
    }

    public CambiarPasswordRequest(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
