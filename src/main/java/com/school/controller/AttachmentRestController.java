package com.school.controller;

import com.school.dto.AttachmentDtoForSend;
import com.school.entity.Attachment;
import com.school.message.InternalizationMessageManagerConfig;
import com.school.service.api.AttachmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/attachments")
public class AttachmentRestController implements GlobalController {
    public static final String KEY_FOR_IMAGE_UPLOADED_SUCCESSFULLY_WITH_ID = "AttachmentRestController.ImageUploadedSuccessfully"; //FixME
    public static final String KEY_FOR_EXTENSION = "AttachmentRestController.Extension";
    public static final String KEY_FOR_IMAGE_DELETED_SUCCESSFULLY = "AttachmentRestController.ImageDeletedSuccessfully";

    private final AttachmentService attachmentService;

    public AttachmentRestController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @PostMapping
    public ResponseEntity<String> uploadAttachment(@RequestParam("attachment") MultipartFile file) {
        log.info("Received request to upload attachment: {}", file.getOriginalFilename()); //FixMe add local

        try {
            Attachment attachment = attachmentService.saveAttachment(file);
            log.info("Attachment uploaded successfully with ID: {}", attachment.getId());

            String message = InternalizationMessageManagerConfig.getMessage(KEY_FOR_IMAGE_UPLOADED_SUCCESSFULLY_WITH_ID)
                    + attachment.getId()
                    + InternalizationMessageManagerConfig.getMessage(KEY_FOR_EXTENSION)
                    + attachment.getExtension();
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            log.error("Failed to upload attachment: {}", file.getOriginalFilename(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload attachment.");
        }
    }


    @GetMapping("/{fileName}")
    public AttachmentDtoForSend getAttachment(@PathVariable String fileName) {
        log.info("Received request to fetch attachment with file name: {}", fileName);

        AttachmentDtoForSend dto = attachmentService.getAttachment(fileName);
        log.info("Attachment fetched successfully for file name: {}", fileName);
        return dto;
    }

    @DeleteMapping("/{id}") //Not Tested
    public ResponseEntity<String> deleteAttachment(@PathVariable String id) {
        log.info("Received request to delete attachment with ID: {}", id);

        try {
            attachmentService.deleteAttachment(id);
            log.info("Attachment deleted successfully with ID: {}", id);

            String message = InternalizationMessageManagerConfig.getMessage(KEY_FOR_IMAGE_DELETED_SUCCESSFULLY);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            log.error("Failed to delete attachment with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete attachment.");
        }
    }
}
