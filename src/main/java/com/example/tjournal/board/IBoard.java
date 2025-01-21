package com.example.tjournal.board;

import com.example.tjournal.commons.dto.IBase;

public interface IBoard extends IBase {
    Long getId();
    void setId(Long id);

    String getName();
    void setName(String name);

    String getContent();
    void setContent(String content);

    String getCategory();
    void setCategory(String category);

    String getTbl();


    // 넣어야할 필드 값에 null 채크 후 값 생성
    default void copyFields(IBoard from) {
        if (from == null) {
            return;
        }
        if (from.getId() != null) {
            this.setId(from.getId());
        }
        if (from.getName() != null && !from.getName().isEmpty()) {
            this.setName(from.getName());
        }
        if (from.getContent() != null && !from.getContent().isEmpty()) {
            this.setContent(from.getContent());
        }
        if (from.getCategory() != null && !from.getCategory().isEmpty()) {
            this.setCategory(from.getCategory());
        }
        IBase.super.copyFields(from);
    }
}
