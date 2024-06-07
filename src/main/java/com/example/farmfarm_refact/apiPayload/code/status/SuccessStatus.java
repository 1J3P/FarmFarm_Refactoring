package com.example.farmfarm_refact.apiPayload.code.status;

import com.example.farmfarm_refact.apiPayload.code.BaseCode;
import com.example.farmfarm_refact.apiPayload.code.ReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {

    // 일반적인 응답
    _OK(HttpStatus.OK, "COMMON200", "성공입니다."),

    // 멤버 관련 응답

    // 임장 관련 응답
    LIMJANG_DELETE(HttpStatus.OK, "LIMJANG2000", "임장 게시글 삭제 성공하였습니다."),
    LIMJANG_UPDATE(HttpStatus.OK, "LIMJANG2001", "임장 게시글 수정 성공하였습니다."),


    // 스크랩 관련 응답
    _SCRAP_ACTION_SCRAP(HttpStatus.OK, "SCRAP2000", "스크랩 추가 성공하였습니다."),
    _SCRAP_ACTION_UNSCRAP(HttpStatus.OK, "SCRAP2001", "스크랩 취소 성공하였습니다."),

    // 이미지 관련 응답
    IMAGE_UPDATE(HttpStatus.OK, "IMAGE2000", "이미지 업로드 성공하였습니다."),
    IMAGE_DELETE(HttpStatus.OK, "IMAGE2001", "이미지 삭제 성공하였습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDTO getReason() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .build();
    }

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}

