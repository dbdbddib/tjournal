package com.example.tjournal.member;

import com.example.tjournal.commons.dto.SearchAjaxDto;
import com.example.tjournal.commons.inif.IMybatisCRUD;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IMemberMybatisMapper extends IMybatisCRUD<MemberDto> {
    MemberDto findByLoginId(String loginId);
    MemberDto findByNickname(String nickname);
    void changePassword(MemberDto dto);

    Integer countAllByNameContains(SearchAjaxDto search);
    List<MemberDto> findAllByNameContains(SearchAjaxDto search);

    Integer countBySnsId(String snsId);
    Integer countByEmail(String snsId);
}