package com.example.tjournal.follow;

import com.example.tjournal.commons.dto.BaseDto;
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
public class FollowDto extends BaseDto implements IFollow{
    private Long id;
    private Long followerId;
    private Long followingId;

    private Long isFollow;

    private Long follower;
    private Long following;
}
