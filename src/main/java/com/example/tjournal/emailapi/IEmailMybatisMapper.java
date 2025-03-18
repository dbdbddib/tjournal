package com.example.tjournal.emailapi;

import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.Optional;

@Mapper
public interface IEmailMybatisMapper {
    void save(VerificationCode verificationCode);
    Optional<VerificationCode> findByEmailAndCode(String email, String code);
    int deleteByExpireTimeBefore(LocalDateTime now);
}
