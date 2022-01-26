package com.example.demo.securityconfig;

public class Security_Constants {

    public static final String SECRET_JWT_KEY = "Secret_Key_To_Gen_JWTs";
    public static final long TOKEN_EXPIRATION_TIME = 321_000_000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/api/user/create";
}
