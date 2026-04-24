package ism.dakar.edumanage.datas.entities;

import ism.dakar.edumanage.security.datas.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuditLogEntity extends AbstractEntity {

    private String userEmail;

    private String userName;

    @Column(nullable = false)
    private String action;

    private String cible;

    @Column(columnDefinition = "TEXT")
    private String details;

    private String adresseIp;

    @Column(nullable = false, updatable = false)
    private LocalDateTime loggedAt;
}

