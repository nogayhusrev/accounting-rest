package com.nogayhusrev.accountingrest.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Component;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Component
public class BaseEntityListener extends AuditingEntityListener {


    @PrePersist
    public void onPrePersist(BaseEntity baseEntity) {


        baseEntity.setInsertDateTime(LocalDateTime.now());
        baseEntity.setLastUpdateDateTime(LocalDateTime.now());
        baseEntity.setInsertUserId(4L);
        baseEntity.setLastUpdateUserId(4L);
    }

    @PreUpdate
    public void onPreUpdate(BaseEntity baseEntity) {


        baseEntity.setLastUpdateDateTime(LocalDateTime.now());
        baseEntity.setLastUpdateUserId(4L);
        baseEntity.setInsertUserId(4L);
    }


}



