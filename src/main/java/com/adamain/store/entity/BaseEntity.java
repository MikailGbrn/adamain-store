package com.adamain.store.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;

@Getter
@Setter
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity {

    @CreationTimestamp
    private ZonedDateTime createdAt;

    private String createdBy = "system";

    @UpdateTimestamp
    private ZonedDateTime updatedAt;

    private Boolean deleted = false;

    private ZonedDateTime deletedAt;

    private String updatedBy;
}
