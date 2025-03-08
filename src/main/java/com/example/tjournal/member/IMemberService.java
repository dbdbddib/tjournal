package com.example.tjournal.member;

import com.example.tjournal.commons.dto.SearchAjaxDto;
import com.example.tjournal.commons.inif.IServiceCRUD;
import com.example.tjournal.security.dto.LoginRequest;

import java.util.List;

public interface IMemberService extends IServiceCRUD<IMember> {
    IMember login(LoginRequest loginRequest);
    IMember loginNaver(MemberDto dto);
    Boolean changePassword(IMember dto) throws Exception;

    IMember findByLoginId(String loginId);
    IMember findByNickname(String nickname);

    Integer countAllByNameContains(SearchAjaxDto dto);

    List<IMember> findAllByNameContains(SearchAjaxDto dto);

    Integer countBySnsId(MemberDto memberDto);
    Integer countByEmail(MemberDto memberDto);
}
