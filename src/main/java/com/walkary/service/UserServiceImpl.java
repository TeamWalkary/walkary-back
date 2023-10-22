package com.walkary.service;

import com.walkary.config.security.security.jwt.JwtDto;
import com.walkary.config.security.security.jwt.JwtProvider;
import com.walkary.models.dto.UserDto;
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
    public JwtDto login(UserDto userDto) {
        UserEntity userEntity = userRepository.findById(userDto.userId()).orElseThrow(() ->
                new UsernameNotFoundException("잘못된 id 혹은 password 입니다."));

        if(userDto.password().equals(userEntity.getPassword())) {
            Collection<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
            User user = new User(userDto.userId(), userDto.password(), authorities);
            return jwtProvider.generateToken(user);
        } else {
            throw new UsernameNotFoundException("잘못된 id 혹은 password 입니다.");
        }
    }

    @Override
    public String signup(UserDto userDto) {
        UserEntity user = UserEntity.builder()
                .id(userDto.userId())
                .password(userDto.password())
                .nickname(userDto.username())
                .build();

        Optional<UserEntity> userEntity = userRepository.findById(user.getId());
        if(userEntity.isPresent()) {
            return "이미 존재하는 아이디입니다.";
        } else {
            userRepository.save(user);
            return "회원가입이 완료되었습니다.";
        }
    }
}
