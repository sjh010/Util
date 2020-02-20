package com.mobileleader.rpa.api.model.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RobotAuthenticationRequest {

    @NotNull
    @Size(max = 20)
    private String ipAddress;

    @NotNull
    @Size(max = 50)
    private String pcName;

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getPcName() {
        return pcName;
    }

    public void setPcName(String pcName) {
        this.pcName = pcName;
    }
}
