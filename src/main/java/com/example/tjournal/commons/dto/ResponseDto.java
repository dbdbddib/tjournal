package com.example.tjournal.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto {
    private ResponseCode responseCode;  // 서버에서 정의한 응답 코드
    private String message;             // 응답 메시지
    private Object responseData;        // 클라이언트에 전달할 데이터
}
