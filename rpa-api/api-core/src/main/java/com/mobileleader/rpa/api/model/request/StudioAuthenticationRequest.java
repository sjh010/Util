package com.mobileleader.rpa.api.model.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class StudioAuthenticationRequest {

    @Size(max = 20)
    private String ipAddress;

    @NotNull
    @Size(max = 20)
    private String userId;

    private String password;

    private String encryptTypeCode = "00";

    public String getEncryptTypeCode() {
        return encryptTypeCode;
    }

    public void setEncryptTypeCode(String encryptTypeCode) {
        this.encryptTypeCode = encryptTypeCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
