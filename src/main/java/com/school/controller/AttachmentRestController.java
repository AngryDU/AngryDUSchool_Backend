package com.school.controller;

import com.school.dto.AttachmentDtoForSend;
import com.school.entity.Attachment;
import com.school.message.InternalizationMessageManagerConfig;
import com.school.service.api.AttachmentService;
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
        Attachment attachment = attachmentService.saveAttachment(file);
        return ResponseEntity.ok(InternalizationMessageManagerConfig
                .getMessage(KEY_FOR_IMAGE_UPLOADED_SUCCESSFULLY_WITH_ID) +
                attachment.getId() +
                InternalizationMessageManagerConfig.getMessage(KEY_FOR_EXTENSION) +
                attachment.getExtension());
    }


    @GetMapping("/{fileName}")
    public AttachmentDtoForSend getAttachment(@PathVariable String fileName) {
        return attachmentService.getAttachment(fileName);
    }

    @DeleteMapping("/{id}") //Not Tested
    public ResponseEntity<String> deleteAttachment(@PathVariable String id) {
        attachmentService.deleteAttachment(id);
        return ResponseEntity.ok(InternalizationMessageManagerConfig
                .getMessage(KEY_FOR_IMAGE_DELETED_SUCCESSFULLY));
    }
}
