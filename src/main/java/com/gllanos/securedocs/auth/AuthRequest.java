package com.gllanos.securedocs.auth;

import lombok.Data;

@Data
public class AuthRequest {
    private String correo;
    private String password;
}