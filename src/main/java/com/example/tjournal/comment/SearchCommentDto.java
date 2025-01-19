package com.example.tjournal.comment;

import com.example.tjournal.commons.dto.SearchAjaxDto;
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
public class SearchCommentDto extends SearchAjaxDto {
    private String boardId;
    private String tbl;
    private Long createId;
    private Long commentId;
}
