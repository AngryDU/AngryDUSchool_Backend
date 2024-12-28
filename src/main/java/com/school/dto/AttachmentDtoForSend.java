package com.school.dto;

import org.springframework.http.MediaType;

import java.time.LocalDate;

public record AttachmentDtoForSend(
        String fileName,
        LocalDate uploadDate,
        MediaType mediaType,
        String base64Data) {
}
