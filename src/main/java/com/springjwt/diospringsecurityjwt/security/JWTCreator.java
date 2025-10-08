package com.springjwt.diospringsecurityjwt.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JWTCreator {

    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String ROLES_AUTHORITIES = "authorization";

    public static String create(String prefix, String key, JWTObject jwtObject) {
        SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
        
        String token = Jwts.builder()
                .subject(jwtObject.getSubject())
                .issuedAt(jwtObject.getIssuedAt())
                .expiration(jwtObject.getExpiration())
                .claim(ROLES_AUTHORITIES, checkRoles(jwtObject.getRoles()))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
        
        return prefix + " " + token;
    }

    public static JWTObject create(String token, String prefix, String key)
        throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException {

        JWTObject object = new JWTObject();
        SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));

        token = token.replace(prefix + " ", "");

        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
                
        object.setSubject(claims.getSubject());
        object.setIssuedAt(claims.getIssuedAt());
        object.setExpiration(claims.getExpiration());

        // Correção: O checkRoles junta com vírgula, então precisa separar
        String rolesString = (String) claims.get(ROLES_AUTHORITIES);
        if (rolesString != null && !rolesString.isEmpty()) {
            List<String> rolesList = Arrays.asList(rolesString.split(","));
            object.setRoles(rolesList.toArray(new String[0]));
        }

        return object;
    }

    private static String checkRoles(List<String> roles) {
        return roles.stream()
                .map(s -> "ROLE_".concat(s.replaceAll("ROLE_", "")))
                .collect(Collectors.joining(","));
    }
}