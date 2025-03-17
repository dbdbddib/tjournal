package com.example.tjournal.security.controller;

import com.example.tjournal.member.IMember;
import com.example.tjournal.member.IMemberService;
import com.example.tjournal.security.config.SecurityConfig;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Controller
@RequestMapping("/")
public class IndexController {
    @Value("${spring.naver.login.id}")
    private String loginClientId;

    @Value("${spring.naver.login.secret}")
    private String loginClientSecret;

    @Autowired
    private IMemberService memberService;

    @GetMapping("")
    private String index(Model model, @SessionAttribute(name = SecurityConfig.LOGINUSER, required = false) String nickname) {
        if ( nickname != null ) {
            IMember loginUser = this.memberService.findByNickname(nickname);
            model.addAttribute(SecurityConfig.LOGINUSER, loginUser);
        }
        return "index";
    }

    @GetMapping("/signout")
    private String signout(HttpServletResponse response, HttpSession session, @SessionAttribute(name = SecurityConfig.LOGINUSER, required = false) String nickname) {
        if (nickname == null) {
            return "redirect:/"; // 로그인 정보가 없으면 바로 리디렉션
        }

        IMember loginUser = memberService.findByNickname(nickname);
        if (loginUser == null) {
            return "redirect:/";
        }

        String provider = loginUser.getProvider();
        String accessToken = (String) session.getAttribute("access_token");

        try {

            // 네이버 api 에서는 로그아웃 기능을 제공하지 않는다
            // token 삭제시 다시 사용자 제동의를 받아야 하는 편의성 저하

//            if ("NAVER".equals(provider)) {
//                // 네이버 로그아웃 API 호출
//                String logoutUrl = "https://nid.naver.com/oauth2.0/token?"
//                        + "grant_type=delete"
//                        + "&client_id=" + loginClientId
//                        + "&client_secret=" + loginClientSecret
//                        + "&access_token=" + accessToken
//                        + "&service_provider=NAVER";
//
//                RestTemplate restTemplate = new RestTemplate();
//                restTemplate.getForObject(logoutUrl, String.class);
//            }
                if ("KAKAO".equals(provider)) {
                // 카카오 로그아웃 API 호출
                String logoutUrl = "https://kapi.kakao.com/v1/user/logout";

                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Bearer " + accessToken);
                HttpEntity<?> entity = new HttpEntity<>(headers);

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.postForEntity(logoutUrl, entity, String.class);
            }
        } catch (Exception e) {
            log.error("OAuth 로그아웃 실패: " + e.getMessage());
        }


        // 세션 무효화
        session.invalidate();
        Cookie cookie = new Cookie(SecurityConfig.LOGINUSER, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);



        return "login/signout";
    }
}
