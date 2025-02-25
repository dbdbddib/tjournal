package com.example.tjournal.naverAPI;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.nio.charset.Charset;

@Slf4j
@RestController
@RequestMapping("/api/server")
public class ServerApiController {

    @GetMapping("/naver")
    public String naver(@RequestParam("query") String query) {

        // 공식 문서의 [파라미터] 부분 참고
        URI uri = UriComponentsBuilder
                .fromUriString("https://openapi.naver.com")
                .path("/v1/search/local.json")
//                검색할 단어
                .queryParam("query", query)
//                한 번에 표시할 검색 결과 개수
                .queryParam("display", 5)
//                검색 시작 위치
                .queryParam("start", 1)
//                검색 결과 정렬 방법 - random: 정확도순으로 내림차순 정렬
                .queryParam("sort", "random")
                .encode(Charset.forName("UTF-8"))
                .build()
                .toUri();

        log.info("--- naver ---");
        log.info("uri : {}", uri);

        // Header를 위함
        RequestEntity<Void> req = RequestEntity
                .get(uri)
                .header("X-Naver-Client-Id", "7I3RXbAtnLVCIM8q67kB")
                .header("X-Naver-Client-Secret", "gSghi79Nte")
                .build();

        // 응답 받을 클래스 지정
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> result = restTemplate.exchange(req, String.class);

        return result.getBody();
    }

}