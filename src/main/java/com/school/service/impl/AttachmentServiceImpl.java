package com.school.service.impl;

import com.school.dto.AttachmentDtoForSend;
import com.school.entity.Attachment;
import com.school.exception.CustomException;
import com.school.exception.ExceptionLocations;
import com.school.mapper.AttachmentMapper;
import com.school.message.InternalizationMessageManagerConfig;
import com.school.repository.AttachmentRepository;
import com.school.service.api.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

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
                                 @Value("${attachment.directory}") String imageDirectory, AttachmentMapper attachmentMapper) {
        this.attachmentRepository = attachmentRepository;
        this.attachmentDirectory = imageDirectory;
        this.attachmentMapper = attachmentMapper;

        Path directoryPath = Paths.get(imageDirectory);
        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
            } catch (IOException e) {
                //FixMe
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
        if (file.isEmpty()) {
            throw new IllegalArgumentException(KEY_FOR_FILE_IS_EMPTY);
        }

        try {
            String extension = getFileExtension(file.getOriginalFilename());
            String fileName = UUID.randomUUID().toString() + getFileExtension(extension);
            Path filePath = Paths.get(attachmentDirectory, fileName);

            Files.write(filePath, file.getBytes());

            Attachment attachment = new Attachment();
            attachment.setAttachmentTitle(fileName);
            attachment.setExtension(extension);
            attachment.setDownloadLink(filePath.toString());
            attachment.setUploadDate(LocalDate.now());

            Attachment savedAttachment = attachmentRepository.save(attachment);

            return savedAttachment;
        } catch (IOException e) {
            throw new RuntimeException(KEY_FOR_ERROR_SAVING_THE_IMAGE, e);
        }
    }

    /**
     * Gets the image by file name.
     *
     * @param fileName file name.
     * @return array of image byte.
     */
    public Optional<AttachmentDtoForSend> getAttachment(String fileName) {
        Path filePath = Paths.get(attachmentDirectory, fileName);

        if (!Files.exists(filePath)) {
            throw new CustomException(InternalizationMessageManagerConfig
                    .getExceptionMessage(KEY_FOR_FILE_NOT_FOUND),
                    ExceptionLocations.ATTACHMENT_NOT_FOUND);
        }

        byte[] data;
        try {
            data = Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new RuntimeException(KEY_FOR_IMAGE_READING_ERROR, e);
        }

        Attachment attachment = attachmentRepository.findAttachmentByFileName(fileName)
                .orElseThrow(() -> new CustomException(InternalizationMessageManagerConfig
                        .getExceptionMessage(KEY_FOR_FILE_NOT_FOUND),
                        ExceptionLocations.ATTACHMENT_NOT_FOUND));

        AttachmentDtoForSend dto = attachmentMapper.attachmentToAttachmentDtoForSend(attachment, data);

        return Optional.of(dto);
    }

    /**
     * Deletes the image by file name.
     *
     * @param fileName file name.
     */
    public void deleteAttachment(String fileName) {
        Path filePath = Paths.get(attachmentDirectory, fileName);

        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
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
        return index > 0 ? fileName.substring(index) : "";
    }
}
