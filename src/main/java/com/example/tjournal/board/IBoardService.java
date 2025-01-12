package com.example.tjournal.board;

import com.example.tjournal.commons.dto.CUDInfoDto;
import com.example.tjournal.commons.dto.SearchAjaxDto;
import com.example.tjournal.commons.inif.IServiceCRUD;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IBoardService extends IServiceCRUD<BoardDto> {
    BoardDto insert(CUDInfoDto info, BoardDto dto, List<MultipartFile> files) throws RuntimeException;
    BoardDto update(CUDInfoDto info, BoardDto dto) throws RuntimeException;
    Integer countAllByNameContains(SearchAjaxDto searchAjaxDto);
    List<BoardDto> findAllByNameContains(SearchAjaxDto searchAjaxDto);
}
