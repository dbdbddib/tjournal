package com.example.tjournal.follow;

import com.example.tjournal.commons.dto.IBase;

public interface IFollow extends IBase {
    Long getId();
    void setId(Long id);

    String getCreateDt();
    void setCreateDt(String createDt);

    Long getFollowerId();
    void setFollowerId(Long followerId);

    Long getFollowingId();
    void setFollowingId(Long followingId);

    default void copyFields(IFollow from) {
        if (from == null) {
            return;
        }
        if (from.getId() != null) {
            this.setId(from.getId());
        }
        if (from.getCreateDt() != null && !from.getCreateDt().isEmpty()) {
            this.setCreateDt(from.getCreateDt());
        }
        if (from.getFollowerId() != null) {
            this.setFollowerId(from.getFollowerId());
        }
        if (from.getFollowingId() != null) {
            this.setFollowingId(from.getFollowingId());
        }
    }
}
