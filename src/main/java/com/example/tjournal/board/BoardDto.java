package com.example.tjournal.board;

import com.example.tjournal.commons.dto.BaseDto;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto extends BaseDto implements IBoard{
    private Long id;
    @Size(min = 5, max = 100, message = "제목은 5~100자 입니다.")
    private String name;
    @Size(min = 5, max = 1000, message = "본문은 5~1000자 입니다.")
    private String content;

    public String getTbl() {
        return "board";
    }
}
