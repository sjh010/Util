package com.mobileleader.rpa.view.data.dto;

import com.mobileleader.rpa.data.dto.base.User;

public class UserList extends User {

    private String authorityName;

    public String getAuthorityName() {
        return authorityName;
    }

    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }

}
