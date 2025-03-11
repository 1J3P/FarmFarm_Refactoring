package com.example.farmfarm_refact.service;

import com.example.farmfarm_refact.apiPayload.ExceptionHandler;
import com.example.farmfarm_refact.converter.UserConverter;
import com.example.farmfarm_refact.dto.LoginResponseDto;
import com.example.farmfarm_refact.dto.UserRequestDto;
import com.example.farmfarm_refact.dto.UserResponseDto;
import com.example.farmfarm_refact.entity.UserEntity;
import com.example.farmfarm_refact.entity.oauth.KakaoProfile;
import com.example.farmfarm_refact.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static com.example.farmfarm_refact.apiPayload.code.status.ErrorStatus.*;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;

    @Transactional
    public LoginResponseDto regenerateAccessToken(String accessToken, String refreshToken) {
        if (jwtService.validateToken(accessToken))
            throw new ExceptionHandler(ACCESS_TOKEN_AUTHORIZED);

        if (!jwtService.validateToken(refreshToken))
            throw new ExceptionHandler(REFRESH_TOKEN_UNAUTHORIZED);

        Long memberId = jwtService.extractUserIdFromToken(refreshToken);

        Optional<UserEntity> getUser = userRepository.findById(Math.toIntExact(memberId));
        if (getUser.isEmpty())
            throw new ExceptionHandler(MEMBER_NOT_FOUND);

        UserEntity user = getUser.get();
        if (!refreshToken.equals(user.getRefreshToken()))
            throw new ExceptionHandler(REFRESH_TOKEN_UNAUTHORIZED);

        String newRefreshToken = jwtService.generateRefreshToken(memberId);
        String newAccessToken = jwtService.generateAccessToken(memberId);

        user.updateRefreshToken(newRefreshToken);
        userRepository.save(user);

        return new LoginResponseDto(newAccessToken, newRefreshToken, user.getEmail(), user.getNickname());
    }

    @Transactional
    public LoginResponseDto saveUserAndGetToken(String accessToken) {
        KakaoProfile profile = findProfile(accessToken);
        System.out.println("saveUserAndGetToken : " + profile);

        UserEntity user = userRepository.findByEmail(profile.getKakao_account().getEmail());

        if (user == null) {
            user = new UserEntity(profile.getId(), null, profile.getKakao_account().getEmail(), "ROLE_USER", "KAKAO", "활동중", "https://farmfarm-bucket.s3.ap-northeast-2.amazonaws.com/7cc20134-7565-44e3-ba1d-ae6edbc213e5.png");
            userRepository.save(user);
        }

        String newAccessToken = jwtService.generateAccessToken(user.getUId());
        String newRefreshToken = jwtService.generateRefreshToken(user.getUId());

        user.updateRefreshToken(newRefreshToken);
        userRepository.save(user);

        return new LoginResponseDto(newAccessToken, newRefreshToken, user.getEmail(), user.getNickname());
    }

    private KakaoProfile findProfile(String accessToken) {
        RestTemplate rt = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.readValue(accessToken, KakaoProfile.class);
        } catch (Exception e) {
            throw new RuntimeException("카카오 프로필 정보를 가져오는 중 오류 발생", e);
        }
    }

    @Transactional
    public String logout(String refreshToken) {
        Optional<UserEntity> getMember = userRepository.findByRefreshToken(refreshToken);
        if (getMember.isEmpty())
            throw new ExceptionHandler(MEMBER_NOT_FOUND);

        UserEntity user = getMember.get();
        if (user.getRefreshToken().equals(""))
            throw new ExceptionHandler(ALREADY_LOGOUT);

        user.refreshTokenExpires();
        userRepository.save(user);

        return "로그아웃 성공";
    }

    // ✅ `updateProfile()` 메서드 다시 추가!
    public UserResponseDto.UserUpdateProfileResponseDto updateProfile(UserEntity user, UserRequestDto.UserUpdateProfileRequestDto userDto) {
        user.setNickname(userDto.getNickname());
        user.setImage(userDto.getImage());
        UserEntity saveUser = userRepository.save(user);
        return UserConverter.toUserUpdateResponseDto(saveUser);
    }

    public UserEntity deleteUser(UserEntity user) {
        user.setStatus("delete");
        return user;
    }

    public UserEntity changeNickname(UserEntity user, UserRequestDto.UserSetNicknameRequestDto userSetNicknameRequestDto) {
        if (user.getNickname() == null && (!userSetNicknameRequestDto.getNickname().equals(""))) {
            user.setNickname(userSetNicknameRequestDto.getNickname());
        }
        return userRepository.save(user);
    }
}
