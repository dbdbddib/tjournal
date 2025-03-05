package com.example.tjournal.naverAPI.login;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Slf4j
@Controller
@RequestMapping
public class ApiLoginRestController {

    @Value("${naver.login.id}")
    private String loginClientId;

    @Value("${naver.login.secret}")
    private String loginClientSecret;

    @Value("${naver.redirect-uri}")
    private String redirectUri;


    @GetMapping("/naver_login")
    public String naver_login(HttpServletRequest request) {
        String state = UUID.randomUUID().toString();
        String login_url = "https://nid.naver.com/oauth2.0/authorize?response_type=code"
                + "&client_id=" + loginClientId
                + "&redirect_uri=" + redirectUri + "/naver_redirect"
                + "&state=" + state;

        request.getSession().setAttribute("state", state);

        return "redirect:" + login_url;
    }

    @GetMapping("/oauth/login/naver_redirect")
    public String naver_redirect(HttpServletRequest request, Model model) {
        // 네이버에서 전달해준 code, state 값 가져오기
        String code = request.getParameter("code");
        String state = request.getParameter("state");

        // 세션에 저장해둔 state값 가져오기
        String session_state = String.valueOf(request.getSession().getAttribute("state"));

        // CSRF 공격 방지를 위해 state 값 비교
        if (!state.equals(session_state)) {
            System.out.println("세션 불일치");
            request.getSession().removeAttribute("state");
            return "/error";
        }

        String tokenURL = "https://nid.naver.com/oauth2.0/token";

        // body data 생성
        MultiValueMap<String, String> parameter = new LinkedMultiValueMap<>();
        parameter.add("grant_type", "authorization_code");
        parameter.add("client_id", loginClientId);
        parameter.add("client_secret", loginClientSecret);
        parameter.add("code", code);
        parameter.add("state", state);

        // request header 설정
        HttpHeaders headers = new HttpHeaders();
        // Content-type을 application/x-www-form-urlencoded 로 설정
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // header 와 body로 Request 생성
        HttpEntity<?> entity = new HttpEntity<>(parameter, headers);

        try {
            RestTemplate restTemplate = new RestTemplate();
            // 응답 데이터(json)를 Map 으로 받을 수 있도록 관련 메시지 컨버터 추가
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            // Post 방식으로 Http 요청
            // 응답 데이터 형식은 Hashmap 으로 지정
            ResponseEntity<HashMap> result = restTemplate.postForEntity(tokenURL, entity, HashMap.class);
            Map<String, String> resMap = result.getBody();

            // 리턴받은 access_token 가져오기
            String access_token = resMap.get("access_token");

            String userInfoURL = "https://openapi.naver.com/v1/nid/me";
            // Header에 access_token 삽입
            headers.set("Authorization", "Bearer "+access_token);

            // Request entity 생성
            HttpEntity<?> userInfoEntity = new HttpEntity<>(headers);

            // Post 방식으로 Http 요청
            // 응답 데이터 형식은 Hashmap 으로 지정
            ResponseEntity<HashMap> userResult = restTemplate.postForEntity(userInfoURL, userInfoEntity, HashMap.class);
            Map<String, String> userResultMap = userResult.getBody();

            model.addAttribute("result", userResultMap);
            log.info("네이버 사용자 정보: {}", userResultMap);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // 세션에 저장된 state 값 삭제
        request.getSession().removeAttribute("state");

        return "/login/naver_result";
    }
}
