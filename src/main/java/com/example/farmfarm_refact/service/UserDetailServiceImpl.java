package com.example.farmfarm_refact.service;


import com.example.farmfarm_refact.apiPayload.ExceptionHandler;
import com.example.farmfarm_refact.apiPayload.code.status.ErrorStatus;
import com.example.farmfarm_refact.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Slf4j
@RequiredArgsConstructor
@Service
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;


    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        System.out.println("로그인한 memberId : " + memberId);
        UserDetails result = (UserDetails) userRepository.findById(Integer.parseInt(memberId))
                .orElseThrow(() -> new ExceptionHandler(ErrorStatus.MEMBER_NOT_FOUND));
        log.info("UserDetails: 여기ㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣ");
        //로그인할 때  result.getUsername() 여기서 에러남
//        log.info("UserDetails: " + result.getUsername());
        log.info("UserDetails: " + result.toString());

        return result;
    }
}
