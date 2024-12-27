package com.school.dto;

import org.springframework.http.MediaType;

import java.time.LocalDate;

public record AttachmentDtoForSend(
        String fileName,
        LocalDate uploadDate,
        String extension,
        MediaType mediaType,
        byte[] data) {
}
