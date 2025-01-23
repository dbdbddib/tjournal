package com.example.tjournal.commons.inif;

import com.example.tjournal.commons.dto.CUDInfoDto;
import com.example.tjournal.commons.dto.IBase;
import com.example.tjournal.commons.dto.ResponseCode;
import com.example.tjournal.commons.dto.ResponseDto;
import com.example.tjournal.commons.exeption.LoginAccessException;
import com.example.tjournal.member.IMember;
import com.example.tjournal.member.MemberRole;
import com.example.tjournal.security.config.SecurityConfig;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

public interface IResponseController {
    default CUDInfoDto makeResponseCheckLogin(Model model) {
        IMember loginUser = (IMember) model.getAttribute(SecurityConfig.LOGINUSER);
        if (loginUser == null) {
            throw new LoginAccessException("로그인 필요");
        }
        return new CUDInfoDto(loginUser);
    }

    default String makeResponseCheckLogout(Model model) {
        IMember loginUser = (IMember) model.getAttribute(SecurityConfig.LOGINUSER);
        if (loginUser != null) {
            return "redirect:/";
        }
        return "null";
    }

    default CUDInfoDto makeResponseCheckLoginAdmin(Model model) {
        IMember loginUser = (IMember) model.getAttribute(SecurityConfig.LOGINUSER);
        if (loginUser == null) {
            throw new LoginAccessException("로그인 필요");
        } else if (!loginUser.getRole().equals(MemberRole.ADMIN.toString())) {
            throw new LoginAccessException("관리자만 가능");
        }
        return new CUDInfoDto(loginUser);
    }

    // login 체크
    default CUDInfoDto makeResponseCheckSelfOrAdmin(Model model, IBase checkObject) {
        IMember loginUser = (IMember) model.getAttribute(SecurityConfig.LOGINUSER);
        if (loginUser == null) {
            throw new LoginAccessException("로그인 필요");
        } else if (!loginUser.getRole().equals(MemberRole.ADMIN.toString()) && !loginUser.getId().equals(checkObject.getCreateId())) {
            throw new LoginAccessException("관리자와 본인만 가능");
        }
        return new CUDInfoDto(loginUser);
    }

    default ResponseEntity<ResponseDto> makeResponseEntity(HttpStatus httpStatus
            , ResponseCode responseCode
            , String message
            , Object responseData) {
        ResponseDto dto = ResponseDto.builder()
                .responseCode(responseCode)
                .message(message)
                .responseData(responseData)
                .build();
        return ResponseEntity.status(httpStatus).body(dto);
    }
}
