package ism.dakar.edumanage.datas.repositories;

import ism.dakar.edumanage.datas.entities.AuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;


public interface AuditLogRepository extends JpaRepository<AuditLogEntity, Long>,
        QuerydslPredicateExecutor<AuditLogEntity> {

}

