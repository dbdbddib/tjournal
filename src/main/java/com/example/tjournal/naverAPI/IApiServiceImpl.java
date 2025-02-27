package com.example.tjournal.naverAPI;

import com.example.tjournal.board.IBoard;
import com.example.tjournal.commons.inif.IServiceCRUD;
import com.example.tjournal.sbfile.ISbFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IApiServiceImpl extends IServiceCRUD<IMarkerData> {
    Boolean insertMarker(IBoard boardDto, List<MarkerDataDto> markerData) throws RuntimeException;
}
