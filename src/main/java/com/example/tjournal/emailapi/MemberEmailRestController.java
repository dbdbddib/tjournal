package com.example.tjournal.emailapi;

import com.example.tjournal.commons.dto.ResponseCode;
import com.example.tjournal.commons.inif.IResponseController;
import com.example.tjournal.member.MemberDto;
import com.example.tjournal.member.MemberServiceImpl;
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
public class MemberEmailRestController implements IResponseController {
    @Autowired
    private MemberEmailService memberEmailService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private MemberServiceImpl memberService;

    @PostMapping("/sendEmail")
    public ResponseEntity<?> sendEmail(@RequestBody EmailDto requestDto) {
        MemberDto memberDto = MemberDto.builder().build();
        memberDto.setEmail(requestDto.getEmail());

        // 중복 이메일 검사
        Integer countEmail = memberService.countByEmail(memberDto);
        if (countEmail > 0) {
            return makeResponseEntity(HttpStatus.BAD_REQUEST, ResponseCode.R000000, "이미 가입된 이메일", null);
        }

        // 이메일 유효성 검사
        if (!memberEmailService.isRequestDelayedEnough(requestDto.getEmail())) {
            return makeResponseEntity(HttpStatus.BAD_REQUEST, ResponseCode.R000000, "1분이 지난 후 다시 요청해주세요", null);
        }

        memberEmailService.sendVerifyCode(requestDto.getEmail());
        // boolean 값으로 받자 이메일 인증 1분에 3개까지 가능하게 3개 초과 false  message 로 알려주자
        return makeResponseEntity(HttpStatus.OK, ResponseCode.R000000, "성공", requestDto);
    }

    @PostMapping("/verifyEmail")
    public ResponseEntity<?> verifyEmail(@RequestBody EmailDto requestDto) {
        boolean isVerified = memberEmailService.verifyCode(requestDto.getEmail(), requestDto.getCode());
        EmailDto responseDto = new EmailDto();
        responseDto.setEmail(requestDto.getEmail());
        responseDto.setMessage(
                isVerified ? "Email verified successfully." : "Invalid or expired verification code."
        );

        if (isVerified) {
            return makeResponseEntity(
                    HttpStatus.OK, ResponseCode.R000000, responseDto.getMessage(), responseDto
            );
        } else {
            return makeResponseEntity(
                    HttpStatus.BAD_REQUEST, ResponseCode.R000051, responseDto.getMessage(), null
            );
        }
    }

}
