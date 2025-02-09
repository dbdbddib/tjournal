package com.example.tjournal.board;

import com.example.tjournal.commons.dto.CUDInfoDto;
import com.example.tjournal.commons.dto.SearchAjaxDto;
import com.example.tjournal.commons.exeption.IdNotFoundException;
import com.example.tjournal.sbfile.ISbFileService;
import com.example.tjournal.sbfile.SbFileDto;
import com.example.tjournal.sblike.ISbLikeMybatisMapper;
import com.example.tjournal.sblike.SbLikeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class BoardServiceImpl implements IBoardService {
    @Autowired
    private IBoardMybatisMapper boardMybatisMapper;

    @Autowired
    private ISbLikeMybatisMapper sbLikeMybatisMapper;

    @Autowired
    private ISbFileService sbFileService;

    @Override
    @Transactional
    public BoardDto insert(CUDInfoDto cudInfoDto, BoardDto dto, List<MultipartFile> files) {
        if ( cudInfoDto == null || dto == null ) {
            return null;
        }
        BoardDto insert = BoardDto.builder().build();
        insert.copyFields(dto);
        cudInfoDto.setCreateInfo(insert);
        this.boardMybatisMapper.insert(insert);
        this.sbFileService.insertFiles(insert, files);
        return insert;
    }

    @Override
    public BoardDto update(CUDInfoDto info, BoardDto dto
            , List<SbFileDto> sbFileDtoList, List<MultipartFile> files) throws RuntimeException {
        if (info == null || dto == null) {
            return null;
        }
        BoardDto update = BoardDto.builder().build();
        update.copyFields(dto);
        info.setUpdateInfo(update);
        this.boardMybatisMapper.update(update);
        this.sbFileService.updateFiles(sbFileDtoList);
        this.sbFileService.insertFiles(update, files);
        return update;
    }

    @Override
    public void addViewQty(Long id) {
        if (id == null || id <= 0) {
            return;
        }
        this.boardMybatisMapper.addViewQty(id);
    }


    @Override
    @Transactional
    public void addLikeQty(CUDInfoDto cudInfoDto, Long id) {
        if ( cudInfoDto == null || cudInfoDto.getLoginUser() == null || id == null || id <= 0 ) {
            return;
        }
        SbLikeDto boardLikeDto = SbLikeDto.builder()
                .tbl(new BoardDto().getTbl())
                .createId(cudInfoDto.getLoginUser().getId())
                .boardId(id)
                .build();

        Integer count = this.sbLikeMybatisMapper.countByTableUserBoard(boardLikeDto);
        if ( count > 0 ) {
            return;
        }
        this.sbLikeMybatisMapper.insert(boardLikeDto);
        this.boardMybatisMapper.addLikeQty(id);
    }

    @Override
    @Transactional
    public void subLikeQty(CUDInfoDto cudInfoDto, Long id) {
        if ( cudInfoDto == null || cudInfoDto.getLoginUser() == null || id == null || id <= 0 ) {
            return;
        }
        SbLikeDto boardLikeDto = SbLikeDto.builder()
                .tbl(new BoardDto().getTbl())
                .createId(cudInfoDto.getLoginUser().getId())
                .boardId(id)
                .build();

        Integer count = this.sbLikeMybatisMapper.countByTableUserBoard(boardLikeDto);
        if ( count < 1 ) {
            return;
        }
        this.sbLikeMybatisMapper.deleteByTableUserBoard(boardLikeDto);
        this.boardMybatisMapper.subLikeQty(id);
    }

    @Override
    public BoardDto insert(CUDInfoDto cudInfoDto, BoardDto dto) {
        return null;
    }

    // Transactional
    // 메소드 내에서 실행되는 모든 작업(데이터 삽입, 수정, 삭제 등)은 하나의 트랜잭션으로 묶이며,
    // 중간에 에러가 발생하면 모든 작업이 롤백
    // 실패하면 전부 실패(롤백) 성공하면 전부 성공(커밋)
    @Override
    @Transactional
    public BoardDto update(CUDInfoDto info, BoardDto dto) throws RuntimeException {
        if ( info == null || dto == null ) {
            return null;
        }
        BoardDto update = BoardDto.builder().build();
        update.copyFields(dto);
        info.setUpdateInfo(update);
        this.boardMybatisMapper.update(update);
        return update;
    }

    @Override
    public Boolean updateDeleteFlag(CUDInfoDto info, BoardDto dto) {
        if ( info == null || dto == null ) {
            return false;
        }
        BoardDto delete = BoardDto.builder().build();
        delete.copyFields(dto);
        info.setDeleteInfo(delete);
        this.boardMybatisMapper.updateDeleteFlag(delete);
        return true;
    }

    @Override
    public Integer countAllByNameContains(SearchAjaxDto searchAjaxDto) {
        if ( searchAjaxDto == null ) {
            return 0;
        }
        Integer count = this.boardMybatisMapper.countAllByNameContains(searchAjaxDto);
        return count;
    }

    @Override
    public Integer countIdByNameContains(SearchAjaxDto searchAjaxDto) {
        if ( searchAjaxDto == null ) {
            return 0;
        }
        Integer count = this.boardMybatisMapper.countIdByNameContains(searchAjaxDto);
        return count;
    }

    @Override
    public List<BoardDto> findAllByNameContains(SearchAjaxDto searchAjaxDto) {
        if ( searchAjaxDto == null ) {
            return List.of();
        }
        searchAjaxDto.settingValues();
        List<BoardDto> list = this.boardMybatisMapper.findAllByNameContains(searchAjaxDto);
        return list;
    }

    @Override
    public List<BoardDto> findIdByNameContains(SearchAjaxDto searchAjaxDto) {
        if ( searchAjaxDto == null ) {
            return List.of();
        }
        searchAjaxDto.settingValues();
        List<BoardDto> list = this.boardMybatisMapper.findIdByNameContains(searchAjaxDto);
        return list;
    }

    private List<IBoard> getInterfaceList(List<BoardDto> list) {
        if( list == null ) {
            return List.of();
        }
        List<IBoard> result = list.stream().map(item -> (IBoard)item).toList();
        return result;
    }

    @Override
    public Boolean deleteById(Long id) {
        if ( id == null || id <= 0 ) {
            return false;
        }
        this.boardMybatisMapper.deleteById(id);
        return true;
    }

    @Override
    public BoardDto findById(Long id) {
        if ( id == null || id <= 0 ) {
            return null;
        }
        BoardDto find = this.boardMybatisMapper.findById(id);
        if ( find == null ) {
            throw new IdNotFoundException(String.format("Error : not found id = %d !", id));
        }
        return find;
    }


}
