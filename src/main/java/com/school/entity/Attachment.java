package com.school.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "attachment")
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, nullable = false)
    private UUID id;

    @Column(name = "attachment_title")
    private String attachmentTitle;

    @Column(nullable = false, updatable = false, name = "upload_date")
    private LocalDate uploadDate;

    @Column(name = "extension")
    private String extension;

    @Column(name = "download_link")
    private String downloadLink;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attachment that = (Attachment) o;
        return Objects.equals(id, that.id) && Objects.equals(attachmentTitle, that.attachmentTitle) && Objects.equals(uploadDate, that.uploadDate) && Objects.equals(extension, that.extension) && Objects.equals(downloadLink, that.downloadLink);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, attachmentTitle, uploadDate, extension, downloadLink);
    }

    @Override
    public String toString() {
        return "Attachment{" +
                "id=" + id +
                ", attachTitle='" + attachmentTitle + '\'' +
                ", uploadDate=" + uploadDate +
                ", extension='" + extension + '\'' +
                ", downloadLink='" + downloadLink + '\'' +
                '}';
    }
}
