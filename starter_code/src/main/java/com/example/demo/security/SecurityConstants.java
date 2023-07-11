package com.example.demo.security;

public class SecurityConstants {

    public static final String HEADER_STRING = "Authorization";

    public static final String TOKEN_PREFIX = "Baerer ";

    public static final long EXPIRATION_TIME = 864_000_000;

    public static final String SECRET = "simplesecretkey";

    public static final String SIGN_UP_URL = "/api/user/create";
}
