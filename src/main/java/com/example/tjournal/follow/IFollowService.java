package com.example.tjournal.follow;

import com.example.tjournal.commons.dto.CUDInfoDto;
import com.example.tjournal.commons.inif.IServiceCRUD;

public interface IFollowService extends IServiceCRUD<FollowDto> {
    void addFollow(Long following, Long follower);
    void subFollow(Long following, Long follower);
    Integer getFollowersByUserId(Long id);
    Integer getFollowingsByUserId(Long id);
}
