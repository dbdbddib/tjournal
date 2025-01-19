package com.example.tjournal.comment;

import com.example.tjournal.commons.dto.IBase;

public interface IComment extends IBase {
    Long getId();
    void setId(Long id);

    String getComment();
    void setComment(String comment);

    String getWriter();
    void setWriter(String writer);

    Long getBoardId();
    void setBoardId(Long boardId);

    String getTbl();
    void setTbl(String tbl);

    default void copyFields(IComment from) {
        if (from == null) {
            return;
        }
        if (from.getId() != null) {
            this.setId(from.getId());
        }
        if (from.getComment() != null && !from.getComment().isEmpty()) {
            this.setComment(from.getComment());
        }
        if (from.getTbl() != null && !from.getTbl().isEmpty()) {
            this.setTbl(from.getTbl());
        }
        if (from.getWriter() != null) {
            this.setWriter(from.getWriter());
        }
        if (from.getBoardId() != null) {
            this.setBoardId(from.getBoardId());
        }
        IBase.super.copyFields(from);
    }
}
