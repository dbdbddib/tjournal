package com.example.tjournal.emailapi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberEmailVerifyResponseDto {
    private String email;
    private String message;
}
