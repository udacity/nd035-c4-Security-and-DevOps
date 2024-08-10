package com.example.demo.security;

public class Contants {

    public static final String SECRET_KEY = "SecretKeyGenJWT";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final long EXPIRATION_TIME = 864_000_000;
    public static final String SIGN_UP_URL = "/api/user/create";
}
