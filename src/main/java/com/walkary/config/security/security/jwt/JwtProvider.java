package com.walkary.config.security.security.jwt;


import com.walkary.exception.CustomAuthenticationException;
import com.walkary.models.entity.UserEntity;
import com.walkary.repository.UserRepository;
import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Component
public class JwtProvider {

    private static final String SECRET = "walkarySecretKey";
    private static final String AUTH_KEY = "auth";
    private static final long JWT_EXPIRE_MS = 3600000; // 1시간
    private static final long REFRESH_EXPIRE_MS = 604800000; // 2주

    private final UserRepository userRepository;

    public JwtProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public JwtToken generateToken(User user) {
        String authorities = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String accessToken = createToken(authorities, user.getUsername(), JWT_EXPIRE_MS);
        String refreshToken = createToken(authorities, user.getUsername(), REFRESH_EXPIRE_MS);

        return new JwtToken("Bearer "+accessToken, refreshToken);
    }

    public String createToken(String authorities, String name, Long expire) {
        Date now = new Date();
        long nowDate = (now).getTime();
        Date validity = new Date(nowDate + expire);
        return Jwts.builder()
                .setSubject(name)
                .claim(AUTH_KEY, authorities)
                .signWith(SignatureAlgorithm.HS256, SECRET.getBytes())
                .setIssuedAt(now)
                .setExpiration(validity)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                .parser()
                .setSigningKey(SECRET.getBytes())
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTH_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String token) throws CustomAuthenticationException {
        try {
            Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.debug("잘못된 JWT 서명입니다.");
            throw new CustomAuthenticationException("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.debug("만료된 JWT 토큰입니다.");
            return false;
        } catch (UnsupportedJwtException e) {
            log.debug("지원되지 않는 JWT 토큰입니다.");
            throw new CustomAuthenticationException("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.debug("JWT 토큰이 잘못되었습니다.");
            throw new CustomAuthenticationException("JWT 토큰이 잘못되었습니다.");
        }
    }
    public static String resolveToken(HttpServletRequest request, String header) {
        if(header.equals("refreshAuthorization")){
            return Optional.ofNullable(request.getCookies())
                    .map(cookies -> Arrays.stream(cookies)
                            .filter(cookie -> cookie.getName().equals("refreshAuthorization"))
                            .findFirst())
                    .orElseThrow(() -> new CustomAuthenticationException("검증 토큰이 없습니다. 재로그인해주세요."))
                    .map(Cookie::getValue)
                    .orElse(null);
        }
        String bearerToken = request.getHeader(header);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public static String extractUserId(HttpServletRequest request) {
        final String token = resolveToken(request, "refreshAuthorization");
        Claims claims = Jwts
                .parser()
                .setSigningKey(SECRET.getBytes())
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public String newFreshToken(String refreshToken, String tokenFlg) {
        Authentication authentication = getAuthentication(refreshToken);
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        if(tokenFlg.equals("access")){
            return createToken(authorities,authentication.getName(), JWT_EXPIRE_MS);
        } else {
            String token = createToken(authorities,authentication.getName(), REFRESH_EXPIRE_MS);
            Optional<UserEntity> userEntity = userRepository.findById(authentication.getName());
            if(userEntity.isPresent()) {
                userEntity.map(user -> {
                   user.setToken(token);
                   return user;
                });
                userRepository.save(userEntity.get());
            }
            return token;
        }
    }

    public UserEntity validateRefreshToken(String refreshToken) {
        Authentication authentication = getAuthentication(refreshToken);
        Optional<UserEntity> userEntity = userRepository.findById(authentication.getName());
        if(userEntity.isPresent()) {
            return userEntity.get();
        } else {
            throw new AuthenticationCredentialsNotFoundException("토큰을 찾을 수 없습니다.");
        }
    }
}
