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
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(unique = true, nullable = false)
    private UUID attachId;

    @Column(name = "attach_title")
    private String attachTitle;

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
        return Objects.equals(attachId, that.attachId) && Objects.equals(attachTitle, that.attachTitle) && Objects.equals(uploadDate, that.uploadDate) && Objects.equals(extension, that.extension) && Objects.equals(downloadLink, that.downloadLink);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attachId, attachTitle, uploadDate, extension, downloadLink);
    }

    @Override
    public String toString() {
        return "Attachment{" +
                "attachId=" + attachId +
                ", attachTitle='" + attachTitle + '\'' +
                ", uploadDate=" + uploadDate +
                ", extension='" + extension + '\'' +
                ", downloadLink='" + downloadLink + '\'' +
                '}';
    }
}
