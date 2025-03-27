package com.example.tjournal.naverAPI;

import com.example.tjournal.category.RegionEnum;
import com.example.tjournal.commons.dto.ResponseCode;
import com.example.tjournal.commons.dto.ResponseDto;
import com.example.tjournal.commons.exeption.IdNotFoundException;
import com.example.tjournal.commons.exeption.LoginAccessException;
import com.example.tjournal.commons.inif.IResponseController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/server")
public class ServerApiController implements IResponseController {

    @Autowired
    private IApiServiceImpl apiService;

    @Value("${naver.client.id}")
    private String naverClientId;

    @Value("${naver.client.secret}")
    private String naverClientSecret;

    private String regionKorean;

    @GetMapping("/naver")
    public String naver(@RequestParam("query") String query,
                        @RequestParam(value = "region", required = false) String region) throws JsonProcessingException {

        RegionEnum regionEnum = RegionEnum.valueOf(region.toUpperCase()); // RegionEnum 객체 얻기
        String regionKorean = regionEnum.getKoreanName();


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
                .header("X-Naver-Client-Id", naverClientId)
                .header("X-Naver-Client-Secret", naverClientSecret)
                .build();

        // 응답 받을 클래스 지정
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> result = restTemplate.exchange(req, String.class);

        // 여기서 json 값의 지역 부분을 가져와 지역과 비교 if문으로 처리

        String responseBody = result.getBody();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(responseBody);
        ArrayNode items = (ArrayNode) root.path("items");
        ArrayNode filteredItems = mapper.createArrayNode();

        if (regionKorean != null && !regionKorean.trim().isEmpty()) {
            for (JsonNode item : items) {
                String address = item.path("address").asText();
                if (address.contains(regionKorean)) {
                    filteredItems.add(item);
                }
            }
            ((ObjectNode) root).put("total", filteredItems.size());
            ((ObjectNode) root).set("items", filteredItems);
            responseBody = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
        }
        return responseBody;
    }


    @GetMapping("/findbyboardid")
    public ResponseEntity<ResponseDto> findByBoardId(Model model
            , @Validated MarkerDataDto search) {
        try {
            if ( search == null ) {
                return makeResponseEntity(HttpStatus.BAD_REQUEST, ResponseCode.R000051, "입력 매개변수 에러", null);
            }
            makeResponseCheckLogin(model);
            List<IMarkerData> result = this.apiService.findAllByBoardId(search);
            return makeResponseEntity(HttpStatus.OK, ResponseCode.R000000, "OK", result);
        } catch (LoginAccessException ex) {
            log.error(ex.toString());
            return makeResponseEntity(HttpStatus.FORBIDDEN, ResponseCode.R888881, ex.getMessage(), null);
        } catch (IdNotFoundException ex) {
            log.error(ex.toString());
            return makeResponseEntity(HttpStatus.NOT_FOUND, ResponseCode.R000041, ex.getMessage(), null);
        } catch (Exception ex) {
            log.error(ex.toString());
            return makeResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ResponseCode.R999999, ex.getMessage(), null);
        }
    }
}