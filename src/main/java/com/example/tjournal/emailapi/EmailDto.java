package com.example.tjournal.emailapi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailDto {

    // 이메일 인증 코드 관련 데이터
    private Long id;
    private String email;
    private String code;
    private LocalDateTime expireTime;

    // 이메일 인증 응답 데이터
    private String message;

}
