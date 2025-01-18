package com.example.tjournal.board;

import com.example.tjournal.commons.dto.CUDInfoDto;
import com.example.tjournal.commons.dto.SearchAjaxDto;
import com.example.tjournal.commons.inif.IServiceCRUD;
import com.example.tjournal.sbfile.SbFileDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IBoardService extends IServiceCRUD<BoardDto> {
    void addViewQty(Long id);
    void addLikeQty(CUDInfoDto cudInfoDto, Long id);
    void subLikeQty(CUDInfoDto cudInfoDto, Long id);

    BoardDto insert(CUDInfoDto info, BoardDto dto, List<MultipartFile> files) throws RuntimeException;
    BoardDto update(CUDInfoDto info, BoardDto dto, List<SbFileDto> sbFileDtoList, List<MultipartFile> files) throws RuntimeException;
    Integer countAllByNameContains(SearchAjaxDto searchAjaxDto);
    List<BoardDto> findAllByNameContains(SearchAjaxDto searchAjaxDto);
}
