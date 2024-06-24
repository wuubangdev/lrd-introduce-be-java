package com.wuubangdev.lrd.domain;

import java.time.Instant;

import com.wuubangdev.lrd.util.SecurityUtil;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "labs")
public class Lab {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Column(columnDefinition = "MEDIUMTEXT")
    private String name; // Tên phòng thí nghiệm
    @NotBlank
    @Column(columnDefinition = "MEDIUMTEXT")
    private String title; // Tên đầy đủ Việt - Anh + Thông tin người phụ trách
    @NotBlank
    @Column(columnDefinition = "MEDIUMTEXT")
    private String labFunction; // Chức năng phòng thí nghiệm
    @NotBlank
    @Column(columnDefinition = "MEDIUMTEXT")
    private String perform; // Nhiệm vụ + thực hành phòng thí nghiệm
    @NotBlank
    @Column(columnDefinition = "MEDIUMTEXT")
    private String labIteams; // Trang thiết bị thí nghiệm
    @NotBlank
    @Column(columnDefinition = "MEDIUMTEXT")
    private String term; // Học phần phụ trách
    @NotBlank
    @Column(columnDefinition = "MEDIUMTEXT")
    private String secienceField; // Lĩnh vực nghiên cứu

    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @PostPersist
    public void handlePersist() {
        this.createdBy = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        this.createdAt = Instant.now();
    }

    @PreUpdate
    public void handlePreUpdate() {
        this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        this.updatedAt = Instant.now();
    }

}
