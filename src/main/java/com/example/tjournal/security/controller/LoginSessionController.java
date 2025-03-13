package com.example.tjournal.security.controller;

import com.example.tjournal.commons.dto.CUDInfoDto;
import com.example.tjournal.member.IMember;
import com.example.tjournal.member.MemberDto;
import com.example.tjournal.member.MemberProviderRole;
import com.example.tjournal.member.MemberServiceImpl;
import com.example.tjournal.security.config.SecurityConfig;
import com.example.tjournal.security.dto.LoginRequest;
import com.example.tjournal.security.dto.SignUpRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import com.example.tjournal.commons.inif.IResponseController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/selogin")
public class LoginSessionController implements IResponseController{

    @Autowired
    private MemberServiceImpl memberService;

    @GetMapping("/signup")
    private String viewSignUp() {
        return "login/signup";
    }

    @PostMapping("/signup")
    private String signUp(Model model, @Valid @ModelAttribute SignUpRequest dto, BindingResult bindingResult) {
        try {
            if (dto == null) {
                return "redirect:/";
            }
            if (bindingResult.hasErrors()) {
                List<String> errorList = new ArrayList<>();
                for (FieldError error : bindingResult.getFieldErrors()) {
                    errorList.add(error.getField() + " : " + error.getDefaultMessage());
                    log.info(error.getDefaultMessage());
                }
                model.addAttribute("errorList", errorList);
                return "login/fail";
            }
            CUDInfoDto cudInfoDto = new CUDInfoDto(dto);
            dto.setProvider(MemberProviderRole.LOCAL.toString());
            IMember iMember = this.memberService.insert(cudInfoDto, dto);
        } catch (Exception ex) {
            log.error(ex.toString());
            model.addAttribute("message", "회원 가입 실패 했습니다. 입력 정보를 다시 확인하거나 관리자에게 문의하세요");
            return "login/fail";
        }
        return "redirect:/";
    }

    @PostMapping("/signupsns")
    public String signupNaver(HttpServletRequest request,
                              @RequestParam("loginId") String loginId,
                              @RequestParam("password") String password,
                              @RequestParam("nickname") String nickname,
                              Model model) {

        MemberDto tempMember = (MemberDto) request.getSession().getAttribute("tempMember");
        if(tempMember == null) {
            return "redirect:/";
        }
        tempMember.setLoginId(loginId);
        tempMember.setPassword(password);
        tempMember.setNickname(nickname);

        // 추가 검증(닉네임 중복 체크 등)을 수행한 후 회원 가입 처리
        try {
            CUDInfoDto cudInfoDto = new CUDInfoDto(tempMember);
            this.memberService.insert(cudInfoDto, tempMember);
            // 회원가입 완료 후 세션 정리
            request.getSession().removeAttribute("tempMember");
            model.addAttribute("message", "회원 가입 성공");
            return "redirect:/";  // 또는 회원가입 완료 페이지
        } catch (Exception e) {
            model.addAttribute("message", "회원 가입 실패하였습니다. 다시 시도해주세요.");
            return "login/fail";
        }
    }

    @GetMapping("/login")
    private String viewLogin(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 이미 존재하는 세션만 반환
        if (session != null && session.getAttribute(SecurityConfig.LOGINUSER) != null) {
            return "redirect:/"; // 세션에 로그인 정보가 있으면 메인 페이지로 리다이렉트
        }
        return "login/login"; // 로그인 정보가 없으면 로그인 페이지 반환
    }

    @PostMapping("/signin")
    private String signin(Model model, @ModelAttribute LoginRequest dto
        , HttpServletRequest request) {
        try {
            if (dto == null) {
                return "redirect:/";
            }
            IMember loginUser = this.memberService.login(dto);
            if ( loginUser == null ) {
                model.addAttribute("message", "로그인 실패 실패 했습니다. ID와 암호를 확인하세요");
                return "login/fail";
            } else if ( !loginUser.getActive() ) {
                model.addAttribute("message", "회원계정이 비활성 상태입니다, 관리자에게 문의 하세요");
                return "login/fail";
            }
            HttpSession session = request.getSession();
            session.setAttribute(SecurityConfig.LOGINUSER, loginUser.getNickname());
            session.setMaxInactiveInterval(60 * 60);
        } catch (Exception ex) {
            log.error(ex.toString());
            model.addAttribute("message", "로그인 실패 실패 했습니다. 관리자에게 문의 하세요");
            return "login/fail";
        }
        return "redirect:/";
    }

    @GetMapping("/signinsns")
    public String signinSns(HttpServletRequest request, Model model) {
        // 세션에서 임시 memberDto 가져오기
        MemberDto memberDto = (MemberDto) request.getSession().getAttribute("tempMember");
        if(memberDto == null) {
            model.addAttribute("message", "임시 회원 정보가 존재하지 않습니다.");
            return "login/fail";
        }

        IMember loginUser = memberService.loginSns(memberDto);
        if(loginUser == null) {
            model.addAttribute("message", "SNS 로그인 처리에 실패하였습니다.");
            return "login/fail";
        }

        // 로그인 성공 시 세션에 로그인 사용자 정보 저장
        HttpSession session = request.getSession();
        session.setAttribute(SecurityConfig.LOGINUSER, loginUser.getNickname());
        session.setMaxInactiveInterval(60 * 60);

        // 임시 회원 정보 세션에서 제거
        request.getSession().removeAttribute("tempMember");

        return "redirect:/";  // 로그인 후 메인페이지 등으로 리다이렉트
    }



    @GetMapping("/logout")
    private String logout(HttpServletResponse response) {
        // /logout 은 스프링 security 에서 처리하므로 이쪽 url 로 오지 않음
        return "login/signout";
    }

//    @GetMapping("/signout")
//    private String signout(HttpSession session, HttpServletResponse response) {
//        Cookie cookie = new Cookie("loginId", null);
//        cookie.setMaxAge(0);
//        response.addCookie(cookie);
//        session.invalidate();
//        return "login/signout";
//    }
}
