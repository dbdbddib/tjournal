package com.example.tjournal.follow;

import com.example.tjournal.commons.inif.IMybatisCRUD;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IFollowMybatisMapper extends IMybatisCRUD<FollowDto> {
    void follow(Long followingId, Long followerId);
    void unFollow(Long followingId, Long followerId);
    Integer getFollowingsByUserId(Long id);
    Integer getFollowersByUserId(Long id);
    Integer checkFollowingStatus(Long loginUserId, Long boardUserId);
}
