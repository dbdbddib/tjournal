package com.example.tjournal.board;

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
public class BoardDto  {
    private Long id;
    @Size(min = 10, max = 100, message = "제목은 10~100자 입니다.")
    private String name;
    @Size(min = 10, max = 1000, message = "본문은 10~1000자 입니다.")
    private String content;

    public String getTbl() {
        return "board";
    }
}
