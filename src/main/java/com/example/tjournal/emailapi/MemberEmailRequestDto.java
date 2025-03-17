package com.example.tjournal.emailapi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberEmailRequestDto {
    private String email;
    // 인증 코드가 필요한 경우에만 사용 (이메일 전송 시에는 null 가능)
    private String code;
}
