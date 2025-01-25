package com.example.tjournal.follow;

import com.example.tjournal.commons.dto.CUDInfoDto;
import com.example.tjournal.commons.dto.ResponseCode;
import com.example.tjournal.commons.dto.ResponseDto;
import com.example.tjournal.commons.inif.ICommonRestController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Slf4j
@RestController
@RequestMapping("/api/v1/follow")
public class FollowWebRestController implements ICommonRestController<FollowDto> {
    @Autowired
    private IFollowService followService;

    @GetMapping("/addFollow/{id}")
    public ResponseEntity<ResponseDto> addFollow(Model model, @Validated @PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return makeResponseEntity(HttpStatus.BAD_REQUEST, ResponseCode.R000051, "입력 매개변수 에러", null);
            }
            CUDInfoDto cudInfoDto = makeResponseCheckLogin(model);
            Long followingId = cudInfoDto.getLoginUser().getId();
            this.followService.addFollow(followingId, id);

            IFollow result = this.getFollw(id);

            return makeResponseEntity(HttpStatus.OK, ResponseCode.R000000, "OK", result);
            } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<ResponseDto> insert(Model model, FollowDto dto) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDto> update(Model model, Long id, FollowDto dto) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDto> updateDeleteFlag(Model model, Long id, FollowDto dto) {
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

    private FollowDto getFollw(Long id) {
        Integer followerCount = this.followService.getFollowersByUserId(id);
        Integer followingCount = this.followService.getFollowingsByUserId(id);
        // deleteDt 0 or 1 -> heartIcon src = off or on 값 설정
        FollowDto followDto = FollowDto.builder()
                .follower(Long.valueOf(followerCount))
                .following(Long.valueOf(followingCount))
                        .build();
        return followDto;
    }
}
