package com.example.tjournal.emailapi;

import com.example.tjournal.commons.dto.ResponseCode;
import com.example.tjournal.commons.inif.IResponseController;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberEmailController implements IResponseController {
    @Autowired
    private MemberEmailService memberService;
    @Autowired
    private EmailService emailService;

    @PostMapping("/sendEmail")
    public ResponseEntity<?> sendEmail(@RequestBody MemberEmailRequestDto requestDto) {
        memberService.sendVerifyCode(requestDto.getEmail());

        // boolean 값으로 받자 이메일 인증 1분에 3개까지 가능하게 3개 초과 false  message 로 알려주자

        return makeResponseEntity(HttpStatus.OK, ResponseCode.R000000, "성공", requestDto);
    }

    @PostMapping("/verifyEmail")
    public ResponseEntity<?> verifyEmail(@RequestBody MemberEmailRequestDto requestDto) {
        boolean isVerified = memberService.verifyCode(requestDto.getEmail(), requestDto.getCode());
        MemberEmailVerifyResponseDto responseDto = new MemberEmailVerifyResponseDto();
        responseDto.setEmail(requestDto.getEmail());
        responseDto.setMessage(isVerified ? "Email verified successfully." : "Invalid or expired verification code.");

        if (isVerified) {
            return makeResponseEntity(HttpStatus.OK, ResponseCode.R000000, responseDto.getMessage(), responseDto);
        } else {
            return makeResponseEntity(HttpStatus.BAD_REQUEST, ResponseCode.R000051, responseDto.getMessage(), null);
        }
    }

}
