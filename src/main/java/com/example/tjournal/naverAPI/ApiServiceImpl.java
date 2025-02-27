package com.example.tjournal.naverAPI;

import com.example.tjournal.board.IBoard;
import com.example.tjournal.commons.dto.CUDInfoDto;
import com.example.tjournal.sbfile.ISbFile;
import com.example.tjournal.sbfile.SbFileDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class ApiServiceImpl implements IApiServiceImpl {

    @Autowired
    private IApiMybatisMapper apiMybatisMapper;

    public Boolean insertMarker(IBoard boardDto, List<MarkerDataDto> markerData) {
        // 파라미터 유효성 검사
        if (boardDto == null || markerData == null) {
            return false;
        }

        try {
            // markerData 리스트를 순회하며 DB에 insert
            for (MarkerDataDto data : markerData) {
                // boardId는 boardDto의 id를 참조
                MarkerDataDto insert = MarkerDataDto.builder()
                        .lat(data.getLat())
                        .lng(data.getLng())
                        .content(data.getContent())
                        .boardId(boardDto.getId())
                        .build();

                this.apiMybatisMapper.insert(insert);
            }
            return true;
        } catch (Exception ex) {
            // 예외 발생 시 처리
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<IMarkerData> findAllByBoardId(IMarkerData search) {
        if (search == null) {
            return List.of();
        }

        MarkerDataDto dto = MarkerDataDto.builder().build();
        dto.copyFields(search);

        List<MarkerDataDto> list = this.apiMybatisMapper.findAllByBoardId(dto);
        List<IMarkerData> result = this.getInterfaceList(list);
        return result;
    }

    private List<IMarkerData> getInterfaceList(List<MarkerDataDto> list) {
        if (list == null) {
            return List.of();
        }
        List<IMarkerData> result = list.stream().map(x -> (IMarkerData) x).toList();
        return result;
    }

    @Override
    public IMarkerData insert(CUDInfoDto cudInfoDto, IMarkerData dto) {
        return null;
    }

    @Override
    public IMarkerData update(CUDInfoDto cudInfoDto, IMarkerData dto) {
        return null;
    }

    @Override
    public Boolean updateDeleteFlag(CUDInfoDto cudInfoDto, IMarkerData dto) {
        return null;
    }

    @Override
    public Boolean deleteById(Long id) {
        return null;
    }

    @Override
    public IMarkerData findById(Long id) {
        return null;
    }
}

