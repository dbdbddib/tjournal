package com.example.tjournal.naverAPI;

import com.example.tjournal.commons.inif.IMybatisCRUD;
import com.example.tjournal.sbfile.SbFileDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IApiMybatisMapper extends IMybatisCRUD<MarkerDataDto>{
    List<MarkerDataDto> findAllByBoardId(MarkerDataDto search);
}
