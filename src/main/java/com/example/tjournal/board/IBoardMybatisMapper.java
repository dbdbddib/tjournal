package com.example.tjournal.board;

import com.example.tjournal.commons.dto.SearchAjaxDto;
import com.example.tjournal.commons.inif.IMybatisCRUD;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IBoardMybatisMapper extends IMybatisCRUD<BoardDto> {
    void addViewQty(Long id);
    void addLikeQty(Long id);
    void subLikeQty(Long id);

    Integer countAllByNameContains(SearchAjaxDto searchAjaxDto);
    Integer countIdByNameContains(SearchAjaxDto searchAjaxDto);
    List<BoardDto> findAllByNameContains(SearchAjaxDto searchAjaxDto);
    List<BoardDto> findIdByNameContains(SearchAjaxDto searchAjaxDto);
}
