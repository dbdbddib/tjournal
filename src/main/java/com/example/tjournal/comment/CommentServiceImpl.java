package com.example.tjournal.comment;

import com.example.tjournal.commons.dto.CUDInfoDto;
import com.example.tjournal.commons.exeption.IdNotFoundException;
import com.example.tjournal.member.IMember;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CommentServiceImpl implements ICommentService {
    @Autowired
    private ICommentMybatisMapper commentMybatisMapper;

    @Override
    public List<CommentDto> findAllByTblBoardId(IMember loginUser, SearchCommentDto dto) {
        if ( dto == null ) {
            return List.of();
        }
        dto.settingValues();
        dto.setCreateId(loginUser.getId());
        List<CommentDto> list = this.commentMybatisMapper.findAllByTblBoardId(dto);
        return list;
    }

    @Override
    public CommentDto insert(CUDInfoDto info, CommentDto dto) {
        if(info == null || dto == null){
            return null;
        }
        CommentDto insert = CommentDto.builder().build();
        insert.copyFields(dto);
        info.setCreateInfo(insert);
        this.commentMybatisMapper.insert(insert);
        return insert;
    }

    @Override
    public CommentDto update(CUDInfoDto info, CommentDto dto) {
        if(info == null || dto == null){
            return null;
        }
        CommentDto update = CommentDto.builder().build();
        update.copyFields(dto);
        info.setUpdateInfo(update);
        this.commentMybatisMapper.update(update);
        return update;
    }

    @Override
    public Boolean updateDeleteFlag(CUDInfoDto info, CommentDto dto) {
        if(info == null || dto == null){
            return false;
        }
        CommentDto delete = CommentDto.builder().build();
        delete.copyFields(dto);
        delete.setDeleteFlag(true);
        info.setDeleteInfo(delete);
        this.commentMybatisMapper.updateDeleteFlag(delete);
        return true;
    }

    @Override
    public Boolean deleteById(Long id) {
        if(id == null || id <= 0){
            return false;
        }
        this.commentMybatisMapper.deleteById(id);
        return true;
    }

    @Override
    public CommentDto findById(Long id) {
        if(id == null || id <= 0){
            return null;
        }
        CommentDto find = this.commentMybatisMapper.findById(id);
        if(find == null){
            throw new IdNotFoundException(String.format("Error : not found id = %d !", id));
        }
        return find;
    }
}
