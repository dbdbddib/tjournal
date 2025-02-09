package com.example.tjournal.sbfile;

import com.example.tjournal.board.IBoard;
import com.example.tjournal.commons.inif.IServiceCRUD;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ISbFileService extends IServiceCRUD<ISbFile> {
    List<ISbFile> findAllByTblBoardId(ISbFile search);
    Boolean insertFiles(IBoard boardDto, List<MultipartFile> files, String nick) throws RuntimeException;
    Boolean updateFiles(List<SbFileDto> sbFileDtoList);
    byte[] getBytesFromFile(ISbFile down);
}
