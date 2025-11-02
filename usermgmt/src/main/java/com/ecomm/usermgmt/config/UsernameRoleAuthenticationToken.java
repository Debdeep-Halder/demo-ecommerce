package com.ecomm.usermgmt.config;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class UsernameRoleAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private final String role;

    public UsernameRoleAuthenticationToken(String username, String password, String role) {
        super(username, password);
        this.role = role;
    }
    public String getRole() { return role; }
}