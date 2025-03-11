package com.example.tjournal.member;

import com.example.tjournal.commons.dto.CUDInfoDto;
import com.example.tjournal.commons.dto.SearchAjaxDto;
import com.example.tjournal.commons.exeption.IdNotFoundException;
import com.example.tjournal.security.dto.LoginRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MemberServiceImpl implements IMemberService {
    @Autowired
    private IMemberMybatisMapper memberMybatisMapper;

    @Autowired
    private PasswordEncoder encoder;

    // 데이터, 삽입, 수정, 삭제와 같은 작업은 데이터 일관성을 위해 트랜잭션으로 묶음

    @Transactional
    @Override
    public IMember insert(CUDInfoDto cudInfoDto, IMember member) {
        if ( !this.isValidInsert(member) ) {
            return null;
        }
        MemberDto dto = MemberDto.builder().build();
        dto.copyFields(member);
        dto.setPassword(encoder.encode(dto.getPassword()));
        dto.setRole(MemberRole.USER.toString());
        dto.setActive(false);
        cudInfoDto.setCreateInfo(dto);
        this.memberMybatisMapper.insert(dto);
        return dto;
    }

    @Transactional
    @Override
    public IMember updateSnsInfo(IMember member, String provider) {
        if (member == null || provider == null || provider.isEmpty()) {
            throw new IllegalArgumentException("Member와 provider는 null 또는 빈 문자열일 수 없습니다.");
        }
        MemberDto dto = MemberDto.builder().build();
        dto.copyFields(member);
        dto.setProvider(provider);

        this.memberMybatisMapper.updateSnsInfo(dto);
        return dto;
    }

    private boolean isValidInsert(IMember dto) {
        if (dto == null) {
            return false;
        } else if ( dto.getName() == null || dto.getName().isEmpty() ) {
            return false;
        } else if ( dto.getNickname() == null || dto.getNickname().isEmpty() ) {
            return false;
        } else if ( dto.getLoginId() == null || dto.getLoginId().isEmpty() ) {
            return false;
        } else if ( dto.getPassword() == null || dto.getPassword().isEmpty() ) {
            return false;
        } else if ( dto.getEmail() == null || dto.getEmail().isEmpty() ) {
            return false;
//        } else if ( dto.getRole() == null ) {
//            return false;
//        } else if ( dto.getActive() == null ) {
//            return false;
        }
        return true;
    }

    @Transactional
    @Override
    public IMember update(CUDInfoDto cudInfoDto, IMember member) {
        if ( member == null || member.getId() == null || member.getId() <= 0 ) {
            return null;
        }
        IMember find = this.findById(member.getId());
        find.copyFields(member);
        cudInfoDto.setUpdateInfo(find);
        this.memberMybatisMapper.update((MemberDto) find);
        return find;
    }

    @Transactional
    @Override
    public Boolean updateDeleteFlag(CUDInfoDto cudInfoDto, IMember member) {
        if ( member == null || member.getId() == null || member.getId() <= 0 ) {
            return false;
        }
        MemberDto find = this.memberMybatisMapper.findById(member.getId());
        if (find == null) {
            throw new IdNotFoundException(String.format("Error : not found id = %d !", member.getId()));
        }
        find.copyFields(member);
        cudInfoDto.setDeleteInfo(find);
        this.memberMybatisMapper.updateDeleteFlag(find);
        return true;
    }

    @Transactional
    @Override
    public Boolean deleteById(Long id) {
        if ( id == null || id <= 0 ) {
            return null;
        }
        MemberDto find = this.memberMybatisMapper.findById(id);
        if (find == null) {
            throw new IdNotFoundException(String.format("Error : not found id = %d !", id));
        }
        this.memberMybatisMapper.deleteById(id);
        return true;
    }

    @Override
    public IMember findById(Long id) {
        if ( id == null || id <= 0 ) {
            return null;
        }
        MemberDto find = this.memberMybatisMapper.findById(id);
        if (find == null) {
            throw new IdNotFoundException(String.format("Error : not found id = %d !", id));
        }
        return find;
    }

    @Override
    public IMember findByLoginId(String loginId) {
        if ( loginId == null || loginId.isEmpty() ) {
            return null;
        }
        MemberDto find = this.memberMybatisMapper.findByLoginId(loginId);
        return find;
    }

    @Override
    public IMember findByNickname(String nickname) {
        if ( nickname == null || nickname.isEmpty() ) {
            return null;
        }
        MemberDto find = this.memberMybatisMapper.findByNickname(nickname);
        return find;
    }

    @Override
    public IMember login(LoginRequest loginRequest) {
        if ( loginRequest == null || loginRequest.getLoginId() == null
                || loginRequest.getPassword() == null) {
            return null;
        }
        IMember member = this.memberMybatisMapper.findByLoginId(loginRequest.getLoginId());
        if ( member == null || !encoder.matches(loginRequest.getPassword(),
                member.getPassword()) ) {
            return null;
        }
        return member;
    }

    @Override
    public IMember loginNaver(MemberDto dto) {
        if ( dto == null || dto.getSnsId() == null) {
            return null;
        }
        IMember member = this.memberMybatisMapper.findBySnsId(dto.getSnsId());
        return member;
    }

    @Transactional
    @Override
    public Boolean changePassword(IMember member) throws Exception {
        if ( member == null ) {
            return false;
        }
        MemberDto dto = new MemberDto();
        dto.copyFields(member);
        dto.setPassword(encoder.encode(dto.getPassword()));
        this.memberMybatisMapper.changePassword(dto);
        return true;
    }

    private List<IMember> getIMemberList(List<MemberDto> list) {
        if ( list == null || list.size() <= 0 ) {
            return new ArrayList<>();
        }
        // input : [MemberDto|MemberDto|MemberDto|MemberDto|MemberDto]
//        List<IMember> result = new ArrayList<>();
//        for( MemberDto item : list ) {
//            result.add( (IMember)item );
//        }
        // output : [IMember|IMember|IMember|IMember|IMember]
        List<IMember> result = list.stream()
                .map(item -> (IMember)item)
                .toList();
        // return : [IMember|IMember|IMember|IMember|IMember]
        return result;
    }

    @Override
    public Integer countAllByNameContains(SearchAjaxDto searchCategoryDto) {
        if( searchCategoryDto == null ) {
            return null;
        }
        return this.memberMybatisMapper.countAllByNameContains(searchCategoryDto);
    }

    @Override
    public List<IMember> findAllByNameContains(SearchAjaxDto dto) {
        if (dto == null) {
            return List.of();
        }
        dto.settingValues();
        List<IMember> result = this.getIMemberList(
                this.memberMybatisMapper.findAllByNameContains(dto)
        );
        return result;
    }

    @Override
    public Integer countBySnsId(MemberDto memberDto) {
        if( memberDto == null ) {
            return null;
        }
        return this.memberMybatisMapper.countBySnsId(memberDto.getSnsId());
    }

    @Override
    public Integer countByEmail(MemberDto memberDto) {
        if( memberDto == null ) {
            return null;
        }
        return this.memberMybatisMapper.countByEmail(memberDto.getEmail());
    }
}
