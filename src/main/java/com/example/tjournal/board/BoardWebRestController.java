package com.example.tjournal.board;

import com.example.tjournal.commons.dto.CUDInfoDto;
import com.example.tjournal.commons.dto.ResponseCode;
import com.example.tjournal.commons.dto.ResponseDto;
import com.example.tjournal.commons.dto.SearchAjaxDto;
import com.example.tjournal.commons.exeption.IdNotFoundException;
import com.example.tjournal.commons.exeption.LoginAccessException;
import com.example.tjournal.commons.inif.ICommonRestController;
import com.example.tjournal.member.IMember;
import com.example.tjournal.member.IMemberService;
import com.example.tjournal.sbfile.SbFileDto;
import com.example.tjournal.sblike.ISbLikeService;
import com.example.tjournal.sblike.SbLikeDto;
import com.example.tjournal.security.config.SecurityConfig;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/board")
public class BoardWebRestController implements ICommonRestController<BoardDto> {
    @Autowired
    private IBoardService boardService;

    @Autowired
    private IMemberService memberService;

    @Autowired
    private ISbLikeService sbLikeService;

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> findById(HttpSession session, @PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return makeResponseEntity(HttpStatus.BAD_REQUEST, ResponseCode.R000051, "입력 매개변수 에러", null);
            }
            String nickname = (String) session.getAttribute(SecurityConfig.LOGINUSER);
            if (nickname == null) {
                return makeResponseEntity(HttpStatus.FORBIDDEN, ResponseCode.R888881, "로그인 필요", null);
            }

            // 중복 조회수 방지 로직
            // 세션을 활용해 동일 브라우저에서의 조회수 중복 증가를 방지
            String viewKey = "viewed_" + id; // 게시글 ID 기반 Key
            Long lastViewedTime = (Long) session.getAttribute(viewKey);
            // 세션에 저장된 이전 조회 시간
            // 처음 조회할 때는 null 그러므로 조건문 if문 실행 (|| -> or)

            if (lastViewedTime == null || (System.currentTimeMillis() - lastViewedTime) >= 3600000) { // 1시간(3600000ms)
                this.boardService.addViewQty(id);
                session.setAttribute(viewKey, System.currentTimeMillis());
                // System.currentTimeMillis() 현재 시간 호출
                // 세션에 viewKey 라는 키와 현재 시간 set
            }

            IBoard result = this.boardService.findById(id);
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

    @PostMapping
    public ResponseEntity<ResponseDto> insert(HttpSession session
            , @Validated @RequestPart(value = "boardDto") BoardDto dto
            , @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        try {
            log.info("Received DTO: " + dto);
            if (dto == null) {
                return makeResponseEntity(HttpStatus.BAD_REQUEST, ResponseCode.R000051, "입력 매개변수 에러", null);
            }
            String nickname = (String) session.getAttribute(SecurityConfig.LOGINUSER);
            IMember loginUser = this.memberService.findByNickname(nickname);
            if (loginUser == null) {
                return makeResponseEntity(HttpStatus.FORBIDDEN, ResponseCode.R888881, "로그인 필요", null);
            }
            CUDInfoDto cudInfoDto = new CUDInfoDto(loginUser);

            Map<String, String> uploadedFilesMap = new HashMap<>();
            for (MultipartFile file : files) {
                String originalFilename = file.getOriginalFilename();
                String uuid = UUID.randomUUID().toString() + ".png";
                uploadedFilesMap.put(originalFilename, uuid);
            }
            dto.setUuidMap(uploadedFilesMap);
            IBoard result = this.boardService.insert(cudInfoDto, dto, files);
            if (result == null) {
                return makeResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ResponseCode.R000011, "서버 입력 에러", null);
            }
            return makeResponseEntity(HttpStatus.OK, ResponseCode.R000000, "성공", uploadedFilesMap);
            // data.responseData  ->  uploadedFiles(UUID 파일명)
        } catch (Exception ex) {
            log.error("Error Occurred: ", ex);
            return makeResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ResponseCode.R999999, ex.toString(), null);
        }
    }

    @Override
    public ResponseEntity<ResponseDto> insert(Model model, BoardDto dto) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDto> update(Model model, Long id, BoardDto dto) {
        return null;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseDto> update(HttpSession session, @PathVariable Long id
            , @Validated @RequestPart(value = "boardDto") BoardDto dto
            , @RequestPart(value = "sbfiles", required = false) List<SbFileDto> sbFileDtoList
            , @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        try {
            if (id == null || dto == null || dto.getId() == null || dto.getId() <= 0 || !id.equals(dto.getId())) {
                return makeResponseEntity(HttpStatus.BAD_REQUEST, ResponseCode.R000051, "입력 매개변수 에러", null);
            }
            String nickname = (String) session.getAttribute(SecurityConfig.LOGINUSER);
            IMember loginUser = this.memberService.findByNickname(nickname);
            CUDInfoDto cudInfoDto = new CUDInfoDto(loginUser);
            IBoard result = this.boardService.update(cudInfoDto, dto, sbFileDtoList, files);
            return makeResponseEntity(HttpStatus.OK, ResponseCode.R000000, "성공", result);
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

    @DeleteMapping("/deleteFlag/{id}")
    public ResponseEntity<ResponseDto> updateDeleteFlag(Model model, @Validated @PathVariable Long id, @Validated @RequestBody BoardDto dto) {
        try {
            if (id == null || dto == null || dto.getId() == null || dto.getId() <= 0 || !id.equals(dto.getId()) || dto.getDeleteFlag() == null) {
                return makeResponseEntity(HttpStatus.BAD_REQUEST, ResponseCode.R000051, "입력 매개변수 에러", null);
            }
            IBoard find = this.boardService.findById(id);
            CUDInfoDto cudInfoDto = makeResponseCheckSelfOrAdmin(model, find);
            Boolean result = this.boardService.updateDeleteFlag(cudInfoDto, dto);
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

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteById(Model model, @Validated @PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return makeResponseEntity(HttpStatus.BAD_REQUEST, ResponseCode.R000051, "입력 매개변수 에러", null);
            }
            IBoard find = this.boardService.findById(id);
            makeResponseCheckSelfOrAdmin(model, find);
            Boolean result = this.boardService.deleteById(id);
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

    @Override
    public ResponseEntity<ResponseDto> findById(Model model, Long id) {
        return null;
    }


    @PostMapping("/countName")
    public ResponseEntity<ResponseDto> countAllByNameContains(Model model, @Validated @RequestBody SearchAjaxDto searchAjaxDto) {
        try {
            if (searchAjaxDto == null) {
                return makeResponseEntity(HttpStatus.BAD_REQUEST, ResponseCode.R000051, "입력 매개변수 에러", null);
            }
            makeResponseCheckLogin(model);
            Integer result = this.boardService.countAllByNameContains(searchAjaxDto);
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


    // 전체 게시판 리스트 검색
    @PostMapping("/searchName")
    public ResponseEntity<ResponseDto> findAllByNameContains(Model model, @Validated @RequestBody SearchAjaxDto searchAjaxDto) {
        try {
            if (searchAjaxDto == null) {
                return makeResponseEntity(HttpStatus.BAD_REQUEST, ResponseCode.R000051, "입력 매개변수 에러", null);
            }
            makeResponseCheckLogin(model);
            int total = this.boardService.countAllByNameContains(searchAjaxDto);
            List<BoardDto> list = this.boardService.findAllByNameContains(searchAjaxDto);

            searchAjaxDto.setTotal(total);
            searchAjaxDto.setDataList(list);
            // 요청한 곳의 ajax done()함수로 데이터 전송
            return makeResponseEntity(HttpStatus.OK, ResponseCode.R000000, "OK", searchAjaxDto);
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

    // 회원 게시판 리스트 검색
    @PostMapping("/searchId")
    public ResponseEntity<ResponseDto> findIdByNameContains(Model model, @Validated @RequestBody SearchAjaxDto searchAjaxDto) {
        try {
            if (searchAjaxDto == null) {
                return makeResponseEntity(HttpStatus.BAD_REQUEST, ResponseCode.R000051, "입력 매개변수 에러", null);
            }
            makeResponseCheckLogin(model);

            int total = this.boardService.countIdByNameContains(searchAjaxDto);
            List<BoardDto> list = this.boardService.findIdByNameContains(searchAjaxDto);

            searchAjaxDto.setTotal(total);
            searchAjaxDto.setDataList(list);
            // 요청한 곳의 ajax done()함수로 데이터 전송
            return makeResponseEntity(HttpStatus.OK, ResponseCode.R000000, "OK", searchAjaxDto);
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

    // 게시판 카테고리별 리스트 검색
    // 메소드 및 mapper 새로 만들자
//    @PostMapping("/searchName/{region}")
//    public ResponseEntity<ResponseDto> findAllByNameContains(Model model,
//                                                             @Validated @RequestBody SearchAjaxDto searchAjaxDto) {
//        try {
//            if (searchAjaxDto == null) {
//                return makeResponseEntity(HttpStatus.BAD_REQUEST, ResponseCode.R000051, "입력 매개변수 에러", null);
//            }
//            makeResponseCheckLogin(model);
//
//            int total = this.boardService.countRegionByNameContains(searchAjaxDto);
//
//            List<BoardDto> list = this.boardService.findRegionByNameContains(searchAjaxDto);
//
//            searchAjaxDto.setTotal(total);
//            searchAjaxDto.setDataList(list);
//            // 요청한 곳의 ajax done()함수로 데이터 전송
//            return makeResponseEntity(HttpStatus.OK, ResponseCode.R000000, "OK", searchAjaxDto);
//        } catch (LoginAccessException ex) {
//            log.error(ex.toString());
//            return makeResponseEntity(HttpStatus.FORBIDDEN, ResponseCode.R888881, ex.getMessage(), null);
//        } catch (IdNotFoundException ex) {
//            log.error(ex.toString());
//            return makeResponseEntity(HttpStatus.NOT_FOUND, ResponseCode.R000041, ex.getMessage(), null);
//        } catch (Exception ex) {
//            log.error(ex.toString());
//            return makeResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ResponseCode.R999999, ex.getMessage(), null);
//        }
//    }

    @GetMapping("/like/{id}")
    public ResponseEntity<ResponseDto> addLikeQty(Model model, @Validated @PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return makeResponseEntity(HttpStatus.BAD_REQUEST, ResponseCode.R000051, "입력 매개변수 에러", null);
            }
            this.boardService.findById(id);
            CUDInfoDto cudInfoDto = makeResponseCheckLogin(model);
            this.boardService.addLikeQty(cudInfoDto, id);
            IBoard result = this.getBoardAndLike(id, cudInfoDto.getLoginUser());
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

    @GetMapping("/unlike/{id}")
    public ResponseEntity<ResponseDto> subLikeQty(Model model, @Validated @PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return makeResponseEntity(HttpStatus.BAD_REQUEST, ResponseCode.R000051, "입력 매개변수 에러", null);
            }
            this.boardService.findById(id);
            CUDInfoDto cudInfoDto = makeResponseCheckLogin(model);
            this.boardService.subLikeQty(cudInfoDto, id);

            IBoard result = this.getBoardAndLike(id, cudInfoDto.getLoginUser());
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

    private IBoard getBoardAndLike(Long id, IMember loginUser) {
        IBoard result = this.boardService.findById(id);
        SbLikeDto boardLikeDto = SbLikeDto.builder()
                .tbl(new BoardDto().getTbl())
                .createId(loginUser.getId())
                .boardId(id)
                .build();
        Integer likeCount = this.sbLikeService.countByTableUserBoard(boardLikeDto);
        // deleteDt 0 or 1 -> heartIcon src = off or on 값 설정
        result.setDeleteDt(likeCount.toString());
        return result;
    }
}
