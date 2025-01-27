package com.example.tjournal.follow;

import com.example.tjournal.commons.dto.CUDInfoDto;
import com.example.tjournal.sblike.ISbLikeMybatisMapper;
import com.example.tjournal.sblike.SbLikeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FollowSerciveImpl implements IFollowService{
    @Autowired
    private IFollowMybatisMapper followMybatisMapper;

    @Override
    public void addFollow(Long followingId, Long followerId) {
        this.followMybatisMapper.follow(followingId, followerId);
    }

    @Override
    public void subFollow(Long followingId, Long followerId) {
        this.followMybatisMapper.unFollow(followingId, followerId);
    }

    @Override
    public Integer getFollowersByUserId(Long id) {
        Integer countFollower = this.followMybatisMapper.getFollowersByUserId(id);
        return countFollower;
    }

    @Override
    public Integer getFollowingsByUserId(Long id) {
        Integer countFollowing = this.followMybatisMapper.getFollowingsByUserId(id);
        return countFollowing;
    }

    @Override
    public Integer checkFollowingStatus(Long loginUserId, Long boardUserId) {
        Integer checkFollowingStatus = this.followMybatisMapper.checkFollowingStatus(loginUserId, boardUserId);
        return checkFollowingStatus;
    }

    @Override
    public FollowDto insert(CUDInfoDto cudInfoDto, FollowDto dto) {
        return null;
    }

    @Override
    public FollowDto update(CUDInfoDto cudInfoDto, FollowDto dto) {
        return null;
    }

    @Override
    public Boolean updateDeleteFlag(CUDInfoDto cudInfoDto, FollowDto dto) {
        return null;
    }

    @Override
    public Boolean deleteById(Long id) {
        return null;
    }

    @Override
    public FollowDto findById(Long id) {
        return null;
    }
}
