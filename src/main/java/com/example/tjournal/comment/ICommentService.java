package com.example.tjournal.comment;

import com.example.tjournal.commons.inif.IServiceCRUD;
import com.example.tjournal.member.IMember;

import java.util.List;

public interface ICommentService extends IServiceCRUD<CommentDto> {
    List<CommentDto> findAllByTblBoardId(IMember loginUser, SearchCommentDto dto);
}
