package com.example.demo.confirmation.code.repository;

import com.example.demo.confirmation.code.entity.ConfirmationCode;
import com.example.demo.confirmation.code.entity.ConfirmationCode.ConfirmationCodeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface ConfirmationCodeRepository extends JpaRepository<ConfirmationCode, Long> {

    @Query("select cc from ConfirmationCode cc " +
        "where cc.codeType = :type and cc.code = :code " +
        "and cc.expiresAt >= CURRENT_TIMESTAMP")
    Optional<ConfirmationCode> findActiveByCodeAndType(UUID code, ConfirmationCodeType type);

    @Modifying
    @Query("delete from ConfirmationCode cc where cc.expiresAt >= CURRENT_TIMESTAMP")
    void deleteExpired();

}
