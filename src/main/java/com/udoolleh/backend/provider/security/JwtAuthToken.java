package com.udoolleh.backend.provider.security;

import com.udoolleh.backend.core.security.auth.AuthToken;
import com.udoolleh.backend.exception.CustomException;
import com.udoolleh.backend.exception.ErrorCode;
import io.jsonwebtoken.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Slf4j
public class JwtAuthToken implements AuthToken<Claims> {

    @Getter
    private final String token;
    private final Key key;


    private static final String AUTHORITIES_KEY = "role";

    JwtAuthToken(String token, Key key){
        this.token = token;
        this.key = key;
    }

    JwtAuthToken(String id, String role, Date expiredDate, Key key){
        this.key = key;
        this.token = createJwtAuthToken(id, role, expiredDate).get();
    }

    @Override
    public boolean validate() { //검증
        return getData() != null;
    }

    @Override
    public Claims getData(){
        try{
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        }catch(SecurityException e){
            log.info("Invalid JWT signature."); //유효하지 않은 토큰 예외처리
            throw new CustomException(ErrorCode.AUTHENTICATION_FAILED);
        }catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
            throw new CustomException(ErrorCode.AUTHENTICATION_FAILED);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            throw new CustomException(ErrorCode.AUTHENTICATION_FAILED);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            throw new CustomException(ErrorCode.AUTHENTICATION_FAILED);
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            throw new CustomException(ErrorCode.AUTHENTICATION_FAILED);
        }
    }

    private Optional<String> createJwtAuthToken(String id, String role, Date expiredDate){
        var token = Jwts.builder()
                .setSubject(id)
                .claim(AUTHORITIES_KEY, role)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(expiredDate)
                .compact();

        return Optional.ofNullable(token);
    }
}
