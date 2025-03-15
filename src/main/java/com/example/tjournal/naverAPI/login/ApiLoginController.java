package com.example.tjournal.naverAPI.login;

import com.example.tjournal.member.IMemberService;
import com.example.tjournal.member.MemberDto;
import com.example.tjournal.member.MemberProviderRole;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Slf4j
@Controller
@RequestMapping
public class ApiLoginController {

    @Autowired
    private IMemberService memberService;

    @Value("${naver.login.id}")
    private String loginClientId;

    @Value("${naver.login.secret}")
    private String loginClientSecret;

    @Value("${naver.redirect-uri}")
    private String naverRedirectUri;

    @Value("${kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @Value("${kakao.login.rest-api}")
    private String kakaoRestApiKey;

    // 사용자 인증 → 인증 코드 발급
    // 인증 코드로 → 액세스 토큰(access token) 발급
    // 액세스 토큰으로 → 사용자 정보나 데이터 접근

    // OAuth: Open Authorization
    // 사용자 인증(Authentication) 을 통해 사용자의 리소스 접근 권한(Authorization)을 위임받는 것

    // 토큰이란 사용자가 정상적으로 인증(로그인)을 마쳤다는 사실을 증명하는 디지털 키
    // 일종의 임시 출입증 역할
    // 일정 시간 후 만료되며, 재발급 필요


    @GetMapping("/naver_login")
    public String naver_login(HttpServletRequest request) {
        // HTTP 요청(request)에 관한 모든 정보를 캡슐화하는 역할
        // getSession() 메서드를 통해 현재 사용자의 세션 객체에 접근하거나, 필요시 새 세션을 생성
        String state = UUID.randomUUID().toString();
        String login_url = "https://nid.naver.com/oauth2.0/authorize?response_type=code"
                + "&client_id=" + loginClientId
                + "&redirect_uri=" + naverRedirectUri + "/naver_redirect"
                + "&state=" + state;

        request.getSession().setAttribute("state", state);
        // 세션이 없음 새로운 세션 만듬, state 값은 서버의 세션 저장소에 저장
        // 브라우저는 해당 세션 ID 값을 저장 ID 값으로 통신

        // 네이버 요청: 네이버 로그인 창으로 응답
        // 브라우저 주소창 url에 표시
        return "redirect:" + login_url;
    }

        // 로그인 창에서 로그인 후 해당 로그인 정보 받는 URI
    @GetMapping("/oauth/login/naver_redirect")
    public String naver_redirect(HttpServletRequest request, Model model) {
        // URL 파라미터로 인증 코드(code)와 CSRF 방지를 위한 state 값을 전달
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
        // 인증 코드(code), state, 클라이언트 아이디, 클라이언트 시크릿 등을 함께
        // 네이버의 토큰 발급 URL(https://nid.naver.com/oauth2.0/token)로 POST 요청을 보냅니다.
        // 네이버는 이 정보를 검증하여, 인증 코드가 유효하면 access_token을 반환

        // 토큰 요청을 위한 파라미터 구성
        // LinkedMultiValueMap는 하나의 키에 여러 값을 저장할 수 있는 Map
        MultiValueMap<String, String> parameter = new LinkedMultiValueMap<>();
        parameter.add("grant_type", "authorization_code");
        parameter.add("client_id", loginClientId);
        parameter.add("client_secret", loginClientSecret);
        parameter.add("code", code);
        parameter.add("state", state);

        // HTTP 요청 헤더 설정
        // 헤더: 메타 정보(본문을 해석하기 위한 설명)
        // 본문(파라미터): 실제 전송하고자 하는 데이터
        // 메타 정보는 데이터를 이해하고 적절히 처리하기 위한 환경 설정 및 문맥 정보를 제공하는 역할
        HttpHeaders headers = new HttpHeaders();
        // Content-type을 application/x-www-form-urlencoded 로 설정
        // form-urlencoded 형식은 데이터를 URL 쿼리 스트링이 아닌 HTTP 요청 본문에 담아 전송
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 헤더와 파라미터 데이터를 하나의 객체로 생성
        HttpEntity<?> entity = new HttpEntity<>(parameter, headers);

        try {
            // RestTemplate을 통한 토큰 요청
            // Spring에서 제공하는 RestTemplate은 HTTP 요청을 간편하게 보낼 수 있는 템플릿
            RestTemplate restTemplate = new RestTemplate();
            // 응답 데이터(json)를 Map 으로 받을 수 있도록 관련 메시지 컨버터 추가
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            // POST 방식으로 토큰 발급 URL(tokenURL)에 요청을 보냅니다. 응답은 JSON 형태이므로 HashMap으로 변환되어 리턴
            ResponseEntity<HashMap> result = restTemplate.postForEntity(tokenURL, entity, HashMap.class);
            Map<String, String> resMap = result.getBody();

            // 리턴받은 access_token 가져오기
            String access_token = resMap.get("access_token");

            String userInfoURL = "https://openapi.naver.com/v1/nid/me";
            // Header에 access_token 삽입
            headers.set("Authorization", "Bearer "+access_token);

            request.getSession().setAttribute("access_token", access_token);

            // Request entity 생성
            HttpEntity<?> userInfoEntity = new HttpEntity<>(headers);

            // Post 방식으로 Http 요청
            // 응답은 JSON 형태이므로 HashMap으로 변환되어 리턴
            ResponseEntity<Map<String, Object>> userResult =
                    restTemplate.postForEntity(userInfoURL, userInfoEntity, (Class) Map.class);

            Map<String, Object> userResultMap = userResult.getBody();
            Map<String, Object> responseMap = (Map<String, Object>) userResultMap.get("response");

            String naverId = (String) responseMap.get("id");
            String email   = (String) responseMap.get("email");
            String name    = (String) responseMap.get("name");

            if (naverId == null || email == null || name == null) {
                log.error("네이버 API 응답 값 중 null이 포함됨. naverId: {}, email: {}, name: {}", naverId, email, name);
                return "login/fail";
            }

            MemberDto memberDto = MemberDto.builder().build();
            memberDto.setSnsId(naverId);
            memberDto.setEmail(email);
            memberDto.setName(name);
            memberDto.setProvider(MemberProviderRole.NAVER.toString());
            request.getSession().setAttribute("tempMember", memberDto);

            Integer countSnsId = this.memberService.countBySnsId(memberDto);
            Integer countEmail = this.memberService.countByEmail(memberDto);

            request.getSession().removeAttribute("state");


            if(countEmail == 0){
                model.addAttribute("dto", memberDto);
                return "/login/nicknameInput";
            } else {
                if (countSnsId == 0) {
                    this.memberService.updateSnsInfo(memberDto, MemberProviderRole.NAVER.toString());
                }
                return "redirect:/selogin/signinsns";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "login/fail";  // 예외 발생 시 에러 페이지로 리턴
        }
    }

    @GetMapping("/kakao_login")
    public String kakao_login(HttpServletRequest request) {
        String state = UUID.randomUUID().toString();
        String login_url = "https://kauth.kakao.com/oauth/authorize?response_type=code"
                + "&client_id=" + kakaoRestApiKey
                + "&redirect_uri=" + kakaoRedirectUri
                + "&state=" + state;
        request.getSession().setAttribute("state", state);
        return "redirect:" + login_url;
    }

    @GetMapping("/oauth/login/kakao_redirect")
    public String kakao_redirect(HttpServletRequest request, Model model) {
        String code = request.getParameter("code");
        String state = request.getParameter("state");

        String session_state = String.valueOf(request.getSession().getAttribute("state"));

        if (!state.equals(session_state)) {
            System.out.println("세션 불일치");
            request.getSession().removeAttribute("state");
            return "/error";
        }
        String tokenURL = "https://kauth.kakao.com/oauth/token";

        MultiValueMap<String, String> parameter = new LinkedMultiValueMap<>();
        parameter.add("grant_type", "authorization_code");
        parameter.add("client_id", kakaoRestApiKey);
        parameter.add("redirect_url", kakaoRedirectUri);
        parameter.add("code", code);
        parameter.add("state", state);


        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        HttpEntity<?> entity = new HttpEntity<>(parameter, headers);



        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            ResponseEntity<HashMap> result = restTemplate.postForEntity(tokenURL, entity, HashMap.class);
            Map<String, String> resMap = result.getBody();

            String access_token = resMap.get("access_token");
            request.getSession().setAttribute("access_token", access_token);

            String userInfoURL = "https://kapi.kakao.com/v2/user/me";
            headers.set("Authorization", "Bearer "+access_token);

            HttpEntity<?> userInfoEntity = new HttpEntity<>(headers);

            ResponseEntity<Map<String, Object>> userResult =
                    restTemplate.postForEntity(userInfoURL, userInfoEntity, (Class) Map.class);

            Map<String, Object> userResultMap = userResult.getBody();
            Map<String, Object> kakaoAccount  = (Map<String, Object>) userResultMap.get("kakao_account");

            String kakaoId = String.valueOf(userResultMap.get("id"));
            String email = (String) kakaoAccount.get("email");

// 프로필 내부 정보가 필요하다면 추가로 접근
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            String nickname = (String) profile.get("nickname");

            if (kakaoId == null || email == null || nickname == null) {
                log.error("네이버 API 응답 값 중 null이 포함됨. naverId: {}, email: {}, name: {}", kakaoId, email, nickname);
                return "login/fail";
            }

            MemberDto memberDto = MemberDto.builder().build();
            memberDto.setSnsId(kakaoId);
            memberDto.setEmail(email);
            memberDto.setName(nickname);
            memberDto.setProvider(MemberProviderRole.KAKAO.toString());

            request.getSession().setAttribute("tempMember", memberDto);

            Integer countSnsId = this.memberService.countBySnsId(memberDto);
            Integer countEmail = this.memberService.countByEmail(memberDto);

            request.getSession().removeAttribute("state");


            if(countEmail == 0){
                model.addAttribute("dto", memberDto);
                return "/login/nicknameInput";
            } else {
                if (countSnsId == 0) {
                    this.memberService.updateSnsInfo(memberDto, MemberProviderRole.KAKAO.toString());
                }
                return "redirect:/selogin/signinsns";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "login/fail";  // 예외 발생 시 에러 페이지로 리턴
        }
    }




}
