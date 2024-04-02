package com.school.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import com.school.entity.TokenLink;

import java.util.Optional;
import java.util.UUID;

public interface TokenLinkRepository extends CrudRepository<TokenLink, UUID> {
    @Query("select t from TokenLink t where t.token = :token")
    Optional<TokenLink> findByEmailToken(@Param("token") String token);
}
