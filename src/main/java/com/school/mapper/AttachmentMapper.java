package com.school.mapper;

import com.school.dto.AttachmentDtoForSend;
import com.school.entity.Attachment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class AttachmentMapper {
    public AttachmentDtoForSend attachmentToAttachmentDtoForSend(Attachment entity, String base64Data) {
        AttachmentDtoForSend dto = new AttachmentDtoForSend(
                entity.getAttachmentTitle(),
                entity.getUploadDate(),
                determineMediaType(entity.getExtension()),
                base64Data
        );
        return dto;
    }

    private MediaType determineMediaType(String extension) {
        switch (extension) {
            case ".jpg":
            case ".jpeg":
                return MediaType.IMAGE_JPEG;
            case ".png":
                return MediaType.IMAGE_PNG;
            case ".gif":
                return MediaType.IMAGE_GIF;
            case ".pdf":
                return MediaType.APPLICATION_PDF;
            case ".txt":
                return MediaType.TEXT_PLAIN;
            case ".html":
                return MediaType.TEXT_HTML;
            case ".json":
                return MediaType.APPLICATION_JSON;
            default:
                return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}
