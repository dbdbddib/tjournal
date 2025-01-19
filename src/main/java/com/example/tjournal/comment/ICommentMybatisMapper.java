package com.example.tjournal.comment;

import com.example.tjournal.commons.inif.IMybatisCRUD;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface ICommentMybatisMapper extends IMybatisCRUD<CommentDto> {
    Integer countAllByTblBoardId(CommentDto search);
    List<CommentDto> findAllByTblBoardId(SearchCommentDto dto);
}
