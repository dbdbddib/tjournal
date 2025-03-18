package com.example.tjournal.emailapi;

import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.Optional;

@Mapper
public interface IEmailMybatisMapper {
    void save(MemberEmailDto memberEmailDto);

    // Optional은 Java 8에서 추가된 클래스이며, null 값을 안전하게 처리하고, null 관련 오류를 방지하기 위해 사용
    // Optional은 값이 있을 수도 있고 없을 수도 있음을 명시적으로 표현하고, NullPointerException를 피할 수 있도록 도와준다
    // 결과가 없으면 Optional.empty()를 반환
    // 결과가 있으면 해당 객체를 담고 반환
    Optional<MemberEmailDto> findByEmailAndCode(String email, String code);
    int deleteByExpireTimeBefore(LocalDateTime now);

    void deleteByEmail(String email);
}
