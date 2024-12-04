package com.example.farmfarm_refact.dto;

import com.example.farmfarm_refact.entity.UserEntity;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

public class EnquiryResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnquiryCreateResponseDto {
        private Long eId;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnquiryUpdateResponseDto {
        private String content;
        private String status;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnquiryListDto {
        private Long eId;
        private String username;
        private String content;
        private Timestamp created_at;
        private String productName;
        private List<FileResponseDto.FileCreateResponseDto> images;
        private String status;
        private String reply;
        private Boolean isMyEnquiry;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnquiryListResponseDto {
        private List<EnquiryListDto> enquiryList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnquiryReplyCreateResponseDto {
        private Long eId;
        private String reply;
    }
}
