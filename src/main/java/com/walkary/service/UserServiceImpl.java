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

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Override
    public JwtDto login(UserDto userDto) {
        UserEntity userEntity = userRepository.findByUserId(userDto.userId()).orElseThrow(() ->
                new UsernameNotFoundException("해당 사용자가 존재하지 않습니다. : " + userDto.userId()));

        if(userDto.password().equals(userEntity.getId())) {
            Collection<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
            User user = new User(userDto.userId(), userDto.password(), authorities);
            return jwtProvider.generateToken(user);
        }
        return null;
    }
}
