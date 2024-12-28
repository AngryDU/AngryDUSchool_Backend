package com.school.service.impl;

import com.school.dto.AttachmentDtoForSend;
import com.school.entity.Attachment;
import com.school.exception.CustomException;
import com.school.exception.ExceptionLocations;
import com.school.mapper.AttachmentMapper;
import com.school.message.InternalizationMessageManagerConfig;
import com.school.repository.AttachmentRepository;
import com.school.service.api.AttachmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Base64;
import java.util.UUID;

@Slf4j
@Service(value = "attachmentService")
public class AttachmentServiceImpl implements AttachmentService {
    public static final String KEY_FOR_IMAGE_CATALOG_CANNOT_BE_CREATED = "AttachmentService.CannotCreatedDirectory";
    public static final String KEY_FOR_FILE_IS_EMPTY = "AttachmentService.FileIsEmpty";
    public static final String KEY_FOR_ERROR_SAVING_THE_IMAGE = "AttachmentService.ErrorSavingImage";
    public static final String KEY_FOR_IMAGE_READING_ERROR = "AttachmentService.ImageReadingError";
    public static final String KEY_FOR_IMAGE_DELETING_ERROR = "AttachmentService.ImageDeletingError";
    public static final String KEY_FOR_FILE_NOT_FOUND = "AttachmentService.FileNotFound";

    private String attachmentDirectory;
    private final AttachmentRepository attachmentRepository;
    private final AttachmentMapper attachmentMapper;

    @Autowired
    public AttachmentServiceImpl(AttachmentRepository attachmentRepository,
                                 @Value("${attachment.directory}") String imageDirectory,
                                 AttachmentMapper attachmentMapper) {
        this.attachmentRepository = attachmentRepository;
        this.attachmentDirectory = imageDirectory;
        this.attachmentMapper = attachmentMapper;

        Path directoryPath = Paths.get(imageDirectory);
        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
                log.info("Attachment directory created at: {}", directoryPath.toString());
            } catch (IOException e) {
                log.error("Failed to create attachment directory at: {}", directoryPath.toString(), e);
                throw new CustomException(InternalizationMessageManagerConfig
                        .getExceptionMessage("File not exist"),
                        ExceptionLocations.USER_SERVICE_NOT_FOUND);
            }
        }
    }

    /**
     * Saves the image to disk.
     *
     * @param file image file.
     * @return unique filename.
     */
    public Attachment saveAttachment(MultipartFile file) {
        log.info("Saving attachment: {}", file.getOriginalFilename());

        if (file.isEmpty()) {
            log.warn("Attempted to save an empty file.");
            throw new IllegalArgumentException(KEY_FOR_FILE_IS_EMPTY);
        }

        try {
            String extension = getFileExtension(file.getOriginalFilename());
            String fileName = UUID.randomUUID().toString() + extension;
            Path filePath = Paths.get(attachmentDirectory, fileName);

            Files.write(filePath, file.getBytes());
            log.info("Attachment written to disk: {}", filePath.toString());

            Attachment attachment = new Attachment();
            attachment.setAttachmentTitle(fileName);
            attachment.setExtension(extension);
            attachment.setDownloadLink(filePath.toString());
            attachment.setUploadDate(LocalDate.now());

            Attachment savedAttachment = attachmentRepository.save(attachment);
            log.info("Attachment saved to database with ID: {}", savedAttachment.getId());

            return savedAttachment;
        } catch (IOException e) {
            log.error("Error saving the attachment to disk.", e);
            throw new RuntimeException(KEY_FOR_ERROR_SAVING_THE_IMAGE, e);
        }
    }

    /**
     * Gets the image by file name.
     *
     * @param fileName file name.
     * @return array of image byte.
     */
    public AttachmentDtoForSend getAttachment(String fileName) {
        log.info("Fetching attachment: {}", fileName);

        Path filePath = Paths.get(attachmentDirectory, fileName);
        if (!Files.exists(filePath)) {
            log.warn("Attachment file not found: {}", filePath.toString());
            throw new CustomException(InternalizationMessageManagerConfig
                    .getExceptionMessage(KEY_FOR_FILE_NOT_FOUND),
                    ExceptionLocations.ATTACHMENT_NOT_FOUND);
        }

        String base64Data;
        try {
            byte[] data = Files.readAllBytes(filePath);
            base64Data = Base64.getEncoder().encodeToString(data);
            log.info("Attachment successfully read from disk: {}", filePath.toString());
        } catch (IOException e) {
            log.error("Error reading attachment file: {}", filePath.toString(), e);
            throw new RuntimeException(KEY_FOR_IMAGE_READING_ERROR, e);
        }

        Attachment attachment = attachmentRepository.findAttachmentByFileName(fileName)
                .orElseThrow(() -> {
                    log.warn("Attachment not found in database: {}", fileName);
                    return new CustomException(InternalizationMessageManagerConfig
                            .getExceptionMessage(KEY_FOR_FILE_NOT_FOUND),
                            ExceptionLocations.ATTACHMENT_NOT_FOUND);
                });

        log.info("Attachment successfully retrieved from database with ID: {}", attachment.getId());
        return attachmentMapper.attachmentToAttachmentDtoForSend(attachment, base64Data);
    }

    /**
     * Deletes the image by file name.
     *
     * @param fileName file name.
     */
    public void deleteAttachment(String fileName) {
        log.info("Deleting attachment: {}", fileName);

        Path filePath = Paths.get(attachmentDirectory, fileName);
        try {
            boolean deleted = Files.deleteIfExists(filePath);
            if (deleted) {
                log.info("Attachment successfully deleted from disk: {}", filePath.toString());
            } else {
                log.warn("Attachment file not found for deletion: {}", filePath.toString());
            }
        } catch (IOException e) {
            log.error("Error deleting attachment file: {}", filePath.toString(), e);
            throw new RuntimeException(KEY_FOR_IMAGE_DELETING_ERROR, e);
        }
    }

    /**
     * Returns the file extension.
     *
     * @param fileName file name.
     * @return file extension, including dot (for example, ".jpg").
     */
    private String getFileExtension(String fileName) {
        int index = fileName.lastIndexOf('.');
        String extension = index > 0 ? fileName.substring(index) : "";
        log.debug("Extracted file extension: {}", extension);
        return extension;
    }
}
