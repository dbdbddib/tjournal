package com.example.tjournal.comment;

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
public class CommentDto extends BaseDto implements IComment {
    Long id;
    @Size(min = 1, max = 500, message = "댓글은 1~500자 입니다.")
    String comment;
    String writer;
    Long boardId;
    String tbl;
}
