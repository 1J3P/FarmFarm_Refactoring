package com.example.farmfarm_refact.service;

import com.example.farmfarm_refact.apiPayload.ExceptionHandler;
import com.example.farmfarm_refact.dto.LoginResponseDto;
import com.example.farmfarm_refact.dto.UserRequestDto;
import com.example.farmfarm_refact.dto.UserResponseDto;
import com.example.farmfarm_refact.entity.UserEntity;
import com.example.farmfarm_refact.entity.oauth.KakaoProfile;
import com.example.farmfarm_refact.repository.UserRepository;
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
        if (jwtService.validateToken(accessToken)) {
            throw new ExceptionHandler(ACCESS_TOKEN_AUTHORIZED);
        }

        Optional<String> newAccessTokenOpt = jwtService.refreshAccessToken(refreshToken);
        if (newAccessTokenOpt.isEmpty()) {
            throw new ExceptionHandler(REFRESH_TOKEN_UNAUTHORIZED);
        }

        return new LoginResponseDto(newAccessTokenOpt.get(), refreshToken, "", "토큰이 자동 갱신되었습니다."); // ✅ 인자 개수 맞춤
    }

    @Transactional
    public LoginResponseDto saveUserAndGetToken(String accessToken) {
        KakaoProfile profile = findProfile(accessToken);
        UserEntity user = userRepository.findByEmail(profile.getKakao_account().getEmail());

        if (user == null) {
            user = new UserEntity(profile.getId(), null, profile.getKakao_account().getEmail(), "ROLE_USER", "KAKAO", "활동중", "https://default-image.com");
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
        try {
            return new RestTemplate().getForObject("https://kapi.kakao.com/v2/user/me", KakaoProfile.class);
        } catch (Exception e) {
            throw new RuntimeException("카카오 프로필 정보를 가져오는 중 오류 발생", e);
        }
    }

    @Transactional
    public String logout(String refreshToken) {
        Optional<UserEntity> getMember = userRepository.findByRefreshToken(refreshToken);
        if (getMember.isEmpty()) {
            throw new ExceptionHandler(MEMBER_NOT_FOUND);
        }

        UserEntity user = getMember.get();
        if (user.getRefreshToken().equals("")) {
            throw new ExceptionHandler(ALREADY_LOGOUT);
        }

        user.refreshTokenExpires();
        userRepository.save(user);

        return "로그아웃 성공";
    }

    public UserEntity changeNickname(UserEntity user, UserRequestDto.UserSetNicknameRequestDto userSetNicknameRequestDto) {
        if (user.getNickname() == null || !userSetNicknameRequestDto.getNickname().isEmpty()) {
            user.setNickname(userSetNicknameRequestDto.getNickname());
        }
        return userRepository.save(user);
    }

    public UserResponseDto.UserUpdateProfileResponseDto updateProfile(UserEntity user, UserRequestDto.UserUpdateProfileRequestDto userDto) {
        user.setNickname(userDto.getNickname());
        user.setImage(userDto.getImage());
        UserEntity saveUser = userRepository.save(user);
        return new UserResponseDto.UserUpdateProfileResponseDto(saveUser.getNickname(), saveUser.getImage());
    }
}
