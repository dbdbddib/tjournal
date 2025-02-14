package com.example.tjournal.board;

import com.example.tjournal.commons.dto.BaseDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto extends BaseDto implements IBoard{
    private Long id;
    @Size(min = 1, max = 100, message = "제목은 1~100자 입니다.")
    private String name;
    private String content;
    private String region;
    private String category;
    private Integer viewQty;
    private Integer likeQty;
    private Integer countLike;
    private Map<String, String> uuidMap;
    public String getTbl() {
        return "board";
    }
}
