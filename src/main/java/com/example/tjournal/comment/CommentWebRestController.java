package com.example.tjournal.comment;

import com.example.tjournal.commons.dto.CUDInfoDto;
import com.example.tjournal.commons.dto.ResponseCode;
import com.example.tjournal.commons.dto.ResponseDto;
import com.example.tjournal.commons.exeption.IdNotFoundException;
import com.example.tjournal.commons.exeption.LoginAccessException;
import com.example.tjournal.commons.inif.ICommonRestController;
import com.example.tjournal.member.IMember;
import com.example.tjournal.member.IMemberService;
import com.example.tjournal.member.IMember;
import com.example.tjournal.security.config.SecurityConfig;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/comment")
public class CommentWebRestController implements ICommonRestController<CommentDto> {
    @Autowired
    private ICommentService commentService;

    @Autowired
    private IMemberService memberService;

    @PostMapping
    public ResponseEntity<ResponseDto> insert(HttpSession session, @Validated @RequestBody CommentDto dto) {
        try {
            if ( dto == null || dto.getBoardId() == null) {
                return makeResponseEntity(HttpStatus.BAD_REQUEST, ResponseCode.R000051, "입력 매개변수 에러", null);
            }
            String nickname = (String) session.getAttribute(SecurityConfig.LOGINUSER);
            IMember loginUser = this.memberService.findByNickname(nickname);
            if (loginUser == null) {
                return makeResponseEntity(HttpStatus.FORBIDDEN, ResponseCode.R888881, "로그인 필요", null);
            }
            dto.setWriter(nickname);
            CUDInfoDto cudInfoDto = new CUDInfoDto(loginUser);
            IComment result = this.commentService.insert(cudInfoDto, dto);
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


    @PatchMapping("/{id}")
    public ResponseEntity<ResponseDto> update(HttpSession session, @Validated @PathVariable Long id, @Validated @RequestBody CommentDto dto) {
        try {
            if ( id == null || dto == null || !id.equals(dto.getId()) ) {
                return makeResponseEntity(HttpStatus.BAD_REQUEST, ResponseCode.R000051, "입력 매개변수 에러", null);
            }
            String nickname = (String) session.getAttribute(SecurityConfig.LOGINUSER);
            IMember loginUser = this.memberService.findByNickname(nickname);
            if (loginUser == null) {
                return makeResponseEntity(HttpStatus.FORBIDDEN, ResponseCode.R888881, "로그인 필요", null);
            }
            CUDInfoDto cudInfoDto = new CUDInfoDto(loginUser);
            IComment result = this.commentService.update(cudInfoDto, dto);
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

    @Transactional
    @DeleteMapping("/deleteFlag/{id}")
    public ResponseEntity<ResponseDto> updateDeleteFlag(HttpSession session, @Validated @PathVariable Long id, @Validated @RequestBody CommentDto dto) {
        try {
            if (id == null || dto == null || dto.getId() == null || dto.getId() <= 0 || !id.equals(dto.getId()) || dto.getDeleteFlag() == null) {
                return makeResponseEntity(HttpStatus.BAD_REQUEST, ResponseCode.R000051, "입력 매개변수 에러", null);
            }
            String nickname = (String) session.getAttribute(SecurityConfig.LOGINUSER);
            IMember loginUser = this.memberService.findByNickname(nickname);

            CommentDto commentDto = this.commentService.findById(id);
            if(!nickname.equals(commentDto.getWriter())){
                return makeResponseEntity(HttpStatus.FORBIDDEN, ResponseCode.R888881, "타인 댓글 삭제 불가!", null);
            }

            if (loginUser == null) {
                return makeResponseEntity(HttpStatus.FORBIDDEN, ResponseCode.R888881, "로그인 필요", null);
            }
            CUDInfoDto cudInfoDto = new CUDInfoDto(loginUser);
            Boolean result = this.commentService.updateDeleteFlag(cudInfoDto, dto);
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
    public ResponseEntity<ResponseDto> deleteById(HttpSession session, @Validated @PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return makeResponseEntity(HttpStatus.BAD_REQUEST, ResponseCode.R000051, "입력 매개변수 에러", null);
            }
            String nickname = (String) session.getAttribute(SecurityConfig.LOGINUSER);
            IMember loginUser = this.memberService.findByNickname(nickname);
            if (loginUser == null) {
                return makeResponseEntity(HttpStatus.FORBIDDEN, ResponseCode.R888881, "로그인 필요", null);
            }

            Boolean result = this.commentService.deleteById(id);
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

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> findById(HttpSession session, @Validated @PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return makeResponseEntity(HttpStatus.BAD_REQUEST, ResponseCode.R000051, "입력 매개변수 에러", null);
            }
            String nickname = (String) session.getAttribute(SecurityConfig.LOGINUSER);
            IMember loginUser = this.memberService.findByNickname(nickname);
            if (loginUser == null) {
                return makeResponseEntity(HttpStatus.FORBIDDEN, ResponseCode.R888881, "로그인 필요", null);
            }
            IComment result = this.commentService.findById(id);
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

    @PostMapping("/findbyboardid")
    public ResponseEntity<ResponseDto> findByBoardId(HttpSession session
            , @Validated @RequestBody SearchCommentDto dto) {
        try {
            if ( dto == null ) {
                return makeResponseEntity(HttpStatus.BAD_REQUEST, ResponseCode.R000051, "입력 매개변수 에러", null);
            }
            String nickname = (String) session.getAttribute(SecurityConfig.LOGINUSER);
            IMember loginUser = this.memberService.findByNickname(nickname);
            if (loginUser == null) {
                return makeResponseEntity(HttpStatus.FORBIDDEN, ResponseCode.R888881, "로그인 필요", null);
            }
            CUDInfoDto cudInfoDto = new CUDInfoDto(loginUser);
            List<CommentDto> result = this.commentService.findAllByTblBoardId(cudInfoDto.getLoginUser(), dto);
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
    public ResponseEntity<ResponseDto> insert(Model model, CommentDto dto) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDto> update(Model model, Long id, CommentDto dto) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDto> updateDeleteFlag(Model model, Long id, CommentDto dto) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDto> deleteById(Model model, Long id) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDto> findById(Model model, Long id) {
        return null;
    }
}
