package com.walkary.service;

import com.walkary.config.security.security.jwt.JwtToken;
import com.walkary.config.security.security.jwt.JwtProvider;
import com.walkary.exception.UserAlreadyExistsException;
import com.walkary.models.dto.request.UserLoginRequest;
import com.walkary.models.dto.request.UserSignupRequest;
import com.walkary.models.entity.UserEntity;
import com.walkary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Override
    public JwtToken login(UserLoginRequest loginRequest) {
        UserEntity loginUser = UserEntity.login(
                loginRequest.userId(),
                loginRequest.password()
        );

        UserEntity userEntity = userRepository.findById(loginUser.getId()).orElseThrow(() ->
                new UsernameNotFoundException("잘못된 id 혹은 password 입니다."));

        if (loginRequest.password().equals(userEntity.getPassword())) {
            Collection<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
            User user = new User(userEntity.getId(), userEntity.getPassword(), authorities);
            JwtToken jwtToken = jwtProvider.generateToken(user);
            userEntity.setToken(jwtToken.refreshToken());
            userRepository.save(userEntity);
            return jwtToken;
        } else {
            throw new UsernameNotFoundException("잘못된 id 혹은 password 입니다.");
        }
    }

    @Override
    public String signup(UserSignupRequest signupRequest) {
        UserEntity user = UserEntity.signup(
                signupRequest.userId(),
                signupRequest.password(),
                signupRequest.username(),
                signupRequest.email(),
                signupRequest.phoneNumber()
        );

        Optional<UserEntity> userEntity = userRepository.findById(user.getId());
        if (userEntity.isPresent()) {
            throw new UserAlreadyExistsException("이미 존재하는 아이디입니다.");
        } else {
            userRepository.save(user);
            return "회원가입이 완료되었습니다.";
        }
    }
}
