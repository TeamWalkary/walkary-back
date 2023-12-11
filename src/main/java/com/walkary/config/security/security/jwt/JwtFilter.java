package com.walkary.config.security.security.jwt;

import com.walkary.exception.CustomAuthenticationException;
import com.walkary.models.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    private final JwtProvider tokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest =(HttpServletRequest) request;
        String requestURI = httpServletRequest.getRequestURI();
        if(requestURI.equals("/apis/login") ||  requestURI.equals("/apis/signup")) {
            chain.doFilter(request, response);
            return;
        }
        try {
            String jwt = JwtProvider.resolveToken(httpServletRequest, HttpHeaders.AUTHORIZATION);

            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                Authentication authentication = tokenProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("Security Context에 '{}' 인증 정보를 저장했습니다", authentication.getName());
            } else {
                String refreshToken = JwtProvider.resolveToken(httpServletRequest, "refreshAuthorization");
                UserEntity userEntity = tokenProvider.validateRefreshToken(refreshToken);
                if(userEntity.getToken().equals(refreshToken)) {
                    String access = tokenProvider.newFreshToken(refreshToken, "access");
                    HttpServletResponse res = (HttpServletResponse) response;
                    res.setHeader(HttpHeaders.AUTHORIZATION, access);

                    String refresh = tokenProvider.newFreshToken(refreshToken, "refresh");
                    userEntity.setToken(refresh);
                    ResponseCookie cookie = ResponseCookie.from("refreshAuthorization", refresh)
                            .path("/")
                            .domain(".walkary.fun")
                            .sameSite("None")
                            .httpOnly(true)
                            .secure(true)
                            .build();
                    res.addHeader("Set-Cookie", cookie.toString());
                    log.info("renew refresh & access Token");
                    SecurityContextHolder.getContext().setAuthentication(tokenProvider.getAuthentication(refresh));
                }

            }
        } catch (Exception e) {
            throw new CustomAuthenticationException(e.getMessage());
        }

        chain.doFilter(request, response);
    }
}
