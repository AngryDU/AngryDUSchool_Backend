package com.school.service.api;

import com.school.dto.AttachmentDtoForSend;
import com.school.entity.Attachment;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface AttachmentService {
    Attachment saveAttachment(MultipartFile file);

    Optional<AttachmentDtoForSend> getAttachment(String fileName);

    void deleteAttachment(String fileName);

}
