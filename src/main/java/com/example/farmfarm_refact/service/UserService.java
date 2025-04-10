package com.example.farmfarm_refact.service;

import com.example.farmfarm_refact.apiPayload.ExceptionHandler;
import com.example.farmfarm_refact.dto.LoginResponseDto;
import com.example.farmfarm_refact.dto.TokenDto;
import com.example.farmfarm_refact.dto.UserRequestDto;
import com.example.farmfarm_refact.dto.UserResponseDto;
import com.example.farmfarm_refact.entity.UserEntity;
import com.example.farmfarm_refact.entity.oauth.KakaoProfile;
import com.example.farmfarm_refact.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static com.example.farmfarm_refact.apiPayload.code.status.ErrorStatus.*;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Transactional
    public LoginResponseDto regenerateAccessToken(String accessToken, String refreshToken) {
        if (jwtService.validateToken(accessToken)) {
            throw new ExceptionHandler(ACCESS_TOKEN_AUTHORIZED);
        }

        Optional<String> newAccessTokenOpt = jwtService.refreshAccessToken(refreshToken);
        if (newAccessTokenOpt.isEmpty()) {
            throw new ExceptionHandler(REFRESH_TOKEN_UNAUTHORIZED);
        }

        Long userId = jwtService.extractUserIdFromToken(refreshToken); // ✅ 유저 ID 추출
        String newRefreshToken = jwtService.generateRefreshToken(userId); // ✅ 새로운 refreshToken 생성

        return new LoginResponseDto(newAccessTokenOpt.get(), newRefreshToken, "", "토큰이 자동 갱신되었습니다.");
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

    // 로그인 성능테스트용
    @Transactional
    public LoginResponseDto saveUserAndGetTokenTest(String email) {
        UserEntity user = userRepository.findByEmail(email);

        if (user == null) {
            user = new UserEntity(email, "테스트");
            userRepository.save(user);
        }

        String newAccessToken = jwtService.generateAccessToken(user.getUId());
        String newRefreshToken = jwtService.generateRefreshToken(user.getUId());

        user.updateRefreshToken(newRefreshToken);
        userRepository.save(user);

        return new LoginResponseDto(newAccessToken, newRefreshToken, user.getEmail(), null);
    }

    private KakaoProfile findProfile(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        String kakaoProfileUrl = "https://kapi.kakao.com/v2/user/me";

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    kakaoProfileUrl, HttpMethod.GET, entity, String.class);

            return objectMapper.readValue(response.getBody(), KakaoProfile.class);

        } catch (Exception e) {
            log.error("카카오 프로필 요청 중 예외 발생", e);
            throw new RuntimeException("카카오 프로필 정보를 가져오는 중 오류 발생", e);
        }
    }
    @Transactional
    public UserResponseDto.UserUpdateProfileResponseDto updateProfile(UserEntity user, UserRequestDto.UserUpdateProfileRequestDto userDto) {
        if (user == null) {
            throw new ExceptionHandler(MEMBER_NOT_FOUND);
        }

        user.setNickname(userDto.getNickname());
        user.setImage(userDto.getImage());

        UserEntity updatedUser = userRepository.save(user);
        return new UserResponseDto.UserUpdateProfileResponseDto(updatedUser.getNickname(), updatedUser.getImage());
    }

    public UserEntity changeNickname(UserEntity user, UserRequestDto.UserSetNicknameRequestDto userSetNicknameRequestDto) {
        if (user.getNickname() == null || !userSetNicknameRequestDto.getNickname().isEmpty()) {
            user.setNickname(userSetNicknameRequestDto.getNickname());
        }
        return userRepository.save(user);
    }

    @Transactional
    public String logout(String refreshToken) {
        Optional<UserEntity> getUser = userRepository.findByRefreshToken(refreshToken);

        if (getUser.isEmpty()) {
            throw new ExceptionHandler(MEMBER_NOT_FOUND);
        }

        UserEntity user = getUser.get();
        if (user.getRefreshToken().isEmpty()) {
            throw new ExceptionHandler(ALREADY_LOGOUT);
        }

        // ✅ 로그아웃 처리 (refreshToken 삭제)
        user.updateRefreshToken("");
        userRepository.save(user);

        return "로그아웃 성공";
    }
}
