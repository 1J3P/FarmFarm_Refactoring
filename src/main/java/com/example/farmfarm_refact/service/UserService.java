package com.example.farmfarm_refact.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import com.example.farmfarm_refact.apiPayload.ExceptionHandler;
import com.example.farmfarm_refact.converter.UserConverter;
import com.example.farmfarm_refact.dto.LoginResponseDto;
import com.example.farmfarm_refact.dto.TokenDto;
import com.example.farmfarm_refact.dto.UserRequestDto;
import com.example.farmfarm_refact.dto.UserResponseDto;
import com.example.farmfarm_refact.entity.FileEntity;
import com.example.farmfarm_refact.entity.FileType;
import com.example.farmfarm_refact.entity.UserEntity;
import com.example.farmfarm_refact.entity.oauth.KakaoProfile;
import com.example.farmfarm_refact.entity.oauth.OauthToken;
import com.example.farmfarm_refact.repository.FileRepository;
import com.example.farmfarm_refact.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


import java.util.Date;
import java.util.Optional;

import static com.example.farmfarm_refact.apiPayload.code.status.ErrorStatus.*;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository; //(1)만들어둔 UserRepository 를 @Autowired 해준다.
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private JwtService jwtService;


    @Value("${KakaoApiKey}")
    private String KakaoApiKey;

    @Value("${RedirectURI}")
    private String RedirectURI;

    @Value("${KakaoApiUrl}")
    private String KakaoApiUrl;

    public OauthToken getAccessToken(String code) {

        //(2)RestTemplate 객체를 만든다. 통신에 유용한 클래스이다. 클래스에 대해 자세히 알고싶다면 구글에 서치!
        RestTemplate rt = new RestTemplate();

        //(3)HttpHeader 객체를 생성한다. 헤더에 들어가야하는 정보는 공식 문서를 잘 찾아보자.
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //(4)HttpBody 객체를 생성한다. 바디에도 참 많은..🤤 파라미터가 요구된다. 마찬가지로 공식 문서를 찾아보면 나와있다. Required 항목에 필수라고 체크된 것만 넣으면 된다. 만약 앱을 등록할 때 시크릿 키를 만들었다면 반드시 넣어야 한다.
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", KakaoApiKey);
        params.add("redirect_uri", RedirectURI);
        params.add("code", code);
        //params.add("client_secret", "{시크릿 키}"); // 생략 가능!

        //(5)HttpEntity 객체를 생성한다. 앞서 만든 HttpHeader 와 HttpBody 정보를 하나의 객체에 담기 위해서이다.
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(params, headers);

        //(6)ResponseEntity 객체를 String 형만 받도록 생성해준다. 이유는 응답받는 값이 Json 형식이기 때문이다.
        ResponseEntity<String> accessTokenResponse = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        //(7)String으로 받은 Json 형식의 데이터를 ObjectMapper 라는 클래스를 사용해 객체로 변환해줄 것이다. 그러기 위해서는 해당 Json 형식과 맞는 OauthToken 이라는 클래스를 만들어 줘야한다(👇아래 참고).
        //.readValue(Json 데이터, 변환할 클래스) 메소드를 이용해 바디값을 읽어온다.
        ObjectMapper objectMapper = new ObjectMapper();
        OauthToken oauthToken = null;
        try {
            oauthToken = objectMapper.readValue(accessTokenResponse.getBody(), OauthToken.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return oauthToken; //(8)Json 데이터가 OauthToken 객체에 잘 담기면 리턴해준다.
    }
    public LoginResponseDto saveUserAndGetToken(String token) {

        //(1)findProfile()이라는 메소드를 이용해 엑세스 토큰으로 카카오 서버에서 사용자 정보를 가져온다. 해당 메소드는 saveUser() 메소드 아래에 구현한다.
        KakaoProfile profile = findProfile(token);
        System.out.println("saveUserAndGetToken : " + profile);

        //(2)UserReapository 에 만들어뒀던 findByKakaoEmail() 메소드를 이용해 User 객체에 담아준다.
        UserEntity user = userRepository.findByEmail(profile.getKakao_account().getEmail());
        //(3)DB에 사용자를 저장하기 전, 이미 존재하는 사용자인지 체크할 필요가 있다.
        //이를 user 변수의 값이 null인지 아닌지로 판단한다. 만약 null 이라면 DB에 저장되지 않은 사용자이므로 사용자 저장 로직을 실행한다
        if(user == null) {
            user = new UserEntity(profile.getId(), null, profile.getKakao_account().getEmail(), "ROLE_USER", "KAKAO", "활동중", "https://farmfarm-bucket.s3.ap-northeast-2.amazonaws.com/7cc20134-7565-44e3-ba1d-ae6edbc213e5.png");
            userRepository.save(user);
        }
        //createToken() 메소드를 이용해 String 형의 JWT 를 반환한다.
        LoginResponseDto responseDto = createToken(user);
        System.out.println("saveUserAndGetToken : " + responseDto.getAccessToken() + responseDto.getRefreshToken() + responseDto.getEmail());
        return responseDto;
    }
//    public String createToken(UserEntity user) { //(2-1)파라미터로 User 객체를 받고 리턴 타입이 String 인 createToken() 메소드를 만든다.
//
//        //(2-2)해당 프로젝트에서는 java-jwt 라이브러리를 사용하기 때문에 jjwt 라이브러리와 문법이 다르므로 주의한다.
//        String jwtToken = JWT.create()
//
//                //(2-3)Payload 에 들어갈 등록된 클레임 을 설정한다.
//                //sub 는 자유롭게 지정한다. (별로 중요하지 않다)
//                //exp 는 앞서 만든 JwtProperties 의 만료 시간 필드를 불러와 위와 같이 작성한다.
//                .withSubject(user.getEmail())
//                .withExpiresAt(new Date(System.currentTimeMillis()+ JwtProperties.EXPIRATION_TIME))
//
//                //(2-4)Payload 에 들어갈 개인 클레임 을 설정한다.
//                //.withClaim(이름, 내용) 형태로 작성한다. 사용자를 식별할 수 있는 값과, 따로 추가하고 싶은 값을 자유롭게 넣는다.
//                .withClaim("id", user.getId())
//                .withClaim("nickname", user.getNickname())
//
//                //(2-5)Signature 를 설정한다. 위와 같이 알고리즘을 명시하고 앞서 만든 JwtProperties 의 비밀 키 필드를 불러와 넣어준다.
//                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
//
//        return jwtToken; //(2-6)만들어진 JWT 를 반환한다.
//    }
    @Transactional
    public LoginResponseDto createToken(UserEntity user) {
        String newAccessToken = jwtService.encodeJwtToken(new TokenDto(user.getUId()));
        String newRefreshToken = jwtService.encodeJwtRefreshToken(user.getUId());

        System.out.println("newAccessToken : " + newAccessToken);
        System.out.println("newRefreshToken : " + newRefreshToken);

        // DB에 refreshToken 저장
        user.updateRefreshToken(newRefreshToken);
        userRepository.save(user);

        System.out.println("user nickname : " + user.getNickname());

        LoginResponseDto responseDto = new LoginResponseDto(newAccessToken, newRefreshToken, user.getEmail(), user.getNickname());
        System.out.println(responseDto.getAccessToken() + responseDto.getRefreshToken() + responseDto.getEmail());
        return responseDto;

    }

    //(1-1)응답 받은 Json 데이터와 정확히 일치하는 KakaoProfile 클래스를 만든다.(👇아래 참고)
    public KakaoProfile findProfile(String token) {
        //(1-2)통신에 필요한 RestTemplate 객체를 만든다. 이후 이어질 부분은 이전 포스팅에서 설명했기 때문에 간단히 짚고 넘어가겠다.
        RestTemplate rt = new RestTemplate();

        //(1-3)HttpHeader 객체를 생성한다.
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token); //(1-4)헤더에는 발급받은 엑세스 토큰을 넣어 요청해야한다. 카카오 공식 문서를 참고하자.
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //(1-5)HttpHeader 와 HttpBody 정보를 하나의 객체에 담아준다.
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest =
                new HttpEntity<>(headers);

        //(1-6)해당 주소로 Http 요청을 보내 String 변수에 응답을 받는다.
        // Http 요청 (POST 방식) 후, response 변수에 응답을 받음
        ResponseEntity<String> kakaoProfileResponse = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                kakaoProfileRequest,
                String.class
        );
        System.out.println("kakaoProfileResponse : " + kakaoProfileResponse);
        System.out.println("kakaoProfileResponse1 : " + kakaoProfileResponse.getStatusCode());
        System.out.println("kakaoProfileResponse2 : " + kakaoProfileResponse.getBody());

        //(1-7)Json 응답을 KakaoProfile 객체로 변환해 리턴해준다.
        ObjectMapper objectMapper = new ObjectMapper();
        KakaoProfile kakaoProfile = null;
        try {
            kakaoProfile = objectMapper.readValue(kakaoProfileResponse.getBody(), KakaoProfile.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return kakaoProfile;
    }

    public UserEntity deleteUser(UserEntity user) {
        user.setStatus("delete");
        return user;
    }

    public UserEntity changeNickname(UserEntity user, UserRequestDto.UserSetNicknameRequestDto userSetNicknameRequestDto) {
        if (user.getNickname()== null && (!userSetNicknameRequestDto.getNickname().equals(""))) {
            user.setNickname(userSetNicknameRequestDto.getNickname());
        }
        return userRepository.save(user);
    }

    @Transactional
    public LoginResponseDto regenerateAccessToken(String accessToken, String refreshToken) {
        if(jwtService.validateTokenBoolean(accessToken))  // access token 유효성 검사

            throw new ExceptionHandler(ACCESS_TOKEN_AUTHORIZED);

        if(!jwtService.validateTokenBoolean(refreshToken))  // refresh token 유효성 검사
            throw new ExceptionHandler(REFRESH_TOKEN_UNAUTHORIZED);

        Long memberId = jwtService.getMemberIdFromJwtToken(refreshToken);

        Optional<UserEntity> getUser = userRepository.findById(Math.toIntExact(memberId));
        if(getUser.isEmpty())
            throw new ExceptionHandler(MEMBER_NOT_FOUND);

        UserEntity user = getUser.get();
        if(!refreshToken.equals(user.getRefreshToken()))
            throw new ExceptionHandler(REFRESH_TOKEN_UNAUTHORIZED);

        String newRefreshToken = jwtService.encodeJwtRefreshToken(memberId);
        String newAccessToken = jwtService.encodeJwtToken(new TokenDto(memberId));

        user.updateRefreshToken(newRefreshToken);
        userRepository.save(user);

        System.out.println("user nickname : " + user.getNickname());

        return new LoginResponseDto(newAccessToken, newRefreshToken, user.getNickname(), user.getNickname());
    }

    // 로그아웃
    @Transactional
    public String logout(String refreshToken) {
        Optional<UserEntity> getMember = userRepository.findByRefreshToken(refreshToken);
        if(getMember.isEmpty())
            throw new ExceptionHandler(MEMBER_NOT_FOUND);

        UserEntity user = getMember.get();
        if(user.getRefreshToken().equals(""))
            throw new ExceptionHandler(ALREADY_LOGOUT);

        user.refreshTokenExpires();
        userRepository.save(user);

        return "로그아웃 성공";
    }

    // 프로필 수정
    public UserResponseDto.UserUpdateProfileResponseDto updateProfile(UserEntity user, UserRequestDto.UserUpdateProfileRequestDto userDto) {
        user.setNickname(userDto.getNickname());
        user.setImage(userDto.getImage());
        UserEntity saveUser = userRepository.save(user);
        return UserConverter.toUserUpdateResponseDto(saveUser);
    }
}
