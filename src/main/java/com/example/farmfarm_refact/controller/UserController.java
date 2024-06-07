package com.example.farmfarm_refact.controller;


import com.example.farmfarm_refact.apiPayload.ApiResponse;

import com.example.farmfarm_refact.dto.LoginResponseDto;
import com.example.farmfarm_refact.dto.UserResponseDto;
import com.example.farmfarm_refact.entity.UserEntity;
import com.example.farmfarm_refact.entity.oauth.OauthToken;
import com.example.farmfarm_refact.repository.UserRepository;
import com.example.farmfarm_refact.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import com.example.farmfarm_refact.apiPayload.ExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;




import static com.example.farmfarm_refact.apiPayload.code.status.ErrorStatus.TOKEN_EMPTY;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService; //(1)만들어둔 UserRepository 를 @Autowired 해준다.
//    @Autowired
//    private FarmService farmService;

    @Value("${KakaoAuthUrl}")
    private String KakaoAuthUrl;

    @Value("${KakaoApiKey}")
    private String KakaoApiKey;

    @Value("${RedirectURI}")
    private String RedirectURI;

    @Value("${KakaoApiUrl}")
    private String KakaoApiUrl;

    @Value("${serverUrl}")
    private String serverUrl;
    @Autowired
    private UserRepository userRepository;

    @ResponseBody
    @GetMapping(value = "/login/getKakaoAuthUrl")
    public String getKakaoAuthUrl(HttpServletRequest request){
        String reqUrl = KakaoAuthUrl + "/oauth/authorize?client_id=" + KakaoApiKey + "&redirect_uri="+ RedirectURI + "&response_type=code";
        System.out.println(reqUrl);
        return reqUrl;
    }

    // 프론트에서 인가코드 받아오는 url
    @GetMapping("/login/oauth_kakao")
    public ApiResponse<LoginResponseDto> getLogin(@RequestParam("code") String code) {
        System.out.println("code : " + code);
        // 넘어온 인가 코드를 통해 access_token 발급
        OauthToken oauthToken= userService.getAccessToken(code);
        System.out.println("oauthToken : " + oauthToken);
        // 발급 받은 accessToken 으로 카카오 회원 정보 DB 저장 후 JWT 를 생성
        //UserService 의 기존 SaveUser 메소드를 수정한다
        LoginResponseDto responseDto = userService.saveUserAndGetToken(oauthToken.getAccess_token());
        System.out.println("Controller get login : " + responseDto.getAccessToken() + responseDto.getRefreshToken() + responseDto.getEmail());
        return ApiResponse.onSuccess(responseDto);
    }

    @GetMapping("/me")
    public ApiResponse<UserResponseDto> getCurrentUser(@AuthenticationPrincipal UserEntity user) {
        return ApiResponse.onSuccess(new UserResponseDto(user.getUId(), user.getNickname(), user.getEmail(), user.getImage()));
    }
//    @GetMapping("/me")
//    public ResponseEntity<Object> getCurrentUser(HttpServletRequest request) {
//        UserEntity user = userService.getUser(request);
//        return ResponseEntity.ok().body(user);
//    }
//
//    @DeleteMapping("/")
//    public ResponseEntity<Object> deleteUser(HttpServletRequest request) {
//        UserEntity user = userService.getUser(request);
//        UserEntity newUser = userService.deleteUser(user);
//        return ResponseEntity.ok().body(newUser);
//    }



//    @GetMapping("/nickname/create")
//    public String setNickname(HttpServletRequest request, @RequestParam String nickname, Model model) {
//        //UserEntity user = (UserEntity)session.getAttribute("user");
//        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!" + nickname);
//        UserEntity newUser = userService.setNickname(userService.getUser(request), request.getParameter("nickname"));
//        model.addAttribute("user", newUser);
//        return "redirect:/";
//    }


//    @PostMapping("/profile")
//    public ResponseEntity<Object> updateProfile(@RequestBody UserEntity updateUser, HttpSession session, Model model) {
//        System.out.println("updateUser1" + updateUser.getImage());
//        System.out.println("updateUser2" + updateUser.getNickname());
//        UserEntity user = (UserEntity)session.getAttribute("user");
//        UserEntity getUser = userService.findById(user.getId());
//        getUser.setNickname(updateUser.getNickname());
//        getUser.setImage(updateUser.getImage());
//        UserEntity saveUser = userRepository.save(getUser);
//        model.addAttribute("user", saveUser);
//        return ResponseEntity.ok().body(saveUser);
//    }

    @PostMapping("/logout")
    public ApiResponse<String> logout(jakarta.servlet.http.HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            String result = userService.logout(token.substring(7));
            return ApiResponse.onSuccess(result);
        } else
            throw new ExceptionHandler(TOKEN_EMPTY);
    }
    // refreshToken으로 accessToken 재발급
    // Authorization : Bearer Token에 refreshToken 담기
    @PostMapping("/regenerate-token")
    public ApiResponse<LoginResponseDto> regenerateAccessToken(jakarta.servlet.http.HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");
        String refreshToken = request.getHeader("Refresh-Token");

        System.out.println("Access Token: " + accessToken.substring(7));
        System.out.println("Refresh Token: " + refreshToken.substring(7));

        if (StringUtils.hasText(accessToken) && accessToken.startsWith("Bearer ") && StringUtils.hasText(refreshToken) && refreshToken.startsWith("Bearer ")) {
            LoginResponseDto result = userService.regenerateAccessToken(accessToken.substring(7), refreshToken.substring(7));
            return ApiResponse.onSuccess(result);
        } else
            throw new ExceptionHandler(TOKEN_EMPTY);
    }
}
