package com.example.tjournal.member;

import com.example.tjournal.commons.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.UniqueElements;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto extends BaseDto implements IMember {
    private Long id;
    private String name;
    private String nickname;
    private String loginId;
    private String password;
    private String email;
    private String role;
    private Boolean active;
}
