package com.Final.Project.Auditor;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AuditorEntity {

    @CreatedBy
    @Column(name="created_by")
    private int createdBy;

    @CreatedDate
    @Column(name="created_on")
    private LocalDateTime createdOn;

    @LastModifiedBy
    @Column(name="modified_by")
    private int modifiedBy;

    @LastModifiedDate
    @Column(name="modified_on")
    private LocalDateTime modifiedOn;

}
