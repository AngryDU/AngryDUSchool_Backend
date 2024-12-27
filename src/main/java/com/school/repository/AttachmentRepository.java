package com.school.repository;

import com.school.entity.Attachment;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@Qualifier(value = "AttachmentRepository")
public interface AttachmentRepository extends JpaRepository<Attachment, UUID> {
    @Query("SELECT a FROM Attachment a WHERE a.attachmentTitle = :fileName")
    Optional<Attachment> findAttachmentByFileName(@Param("fileName") String fileName);
}
