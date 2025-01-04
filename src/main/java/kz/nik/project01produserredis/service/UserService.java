package kz.nik.project01produserredis.service;

import kz.nik.project01produserredis.dto.UserDto;
import kz.nik.project01produserredis.mapper.UserMapper;
import kz.nik.project01produserredis.model.User;
import kz.nik.project01produserredis.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final CacheService cacheService;



    @Transactional
    public UserDto createUser(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername()) || userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("User already exists with the same username or email");
        }

        User user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    public UserDto getUserById(Long id) {
        String cacheKey = "user_" + id;
        UserDto cachedUser = (UserDto) cacheService.getCachedObject(cacheKey);
        if (cachedUser != null) {
            return cachedUser;
        }
        UserDto userDto = userMapper.toDto(userRepository.findById(id).orElse(null));
        if (userDto != null) {
            cacheService.cacheObject(cacheKey, userDto, 1, TimeUnit.DAYS);
        }
        return userDto;
    }

    public void deleteUser(Long id) {

        userRepository.findById(id).ifPresent(user -> {
            userRepository.delete(user);
            cacheService.deleteCachedObject("user_" + id);
        });
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String cacheKey = "user_" + username;
        UserDto cacheUser = (UserDto) cacheService.getCachedObject(cacheKey);
        if (cacheUser != null) {
            return (UserDetails) cacheUser;
        }
         User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (user != null) {
            cacheService.cacheObject(cacheKey, user, 1, TimeUnit.DAYS);
        }
        return (UserDetails) user;
    }
}