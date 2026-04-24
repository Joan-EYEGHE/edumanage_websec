package ism.dakar.edumanage.security.datas.repository;

import ism.dakar.edumanage.security.datas.entity.AccesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface AccessRepository extends JpaRepository<AccesEntity, Long>, QuerydslPredicateExecutor<AccesEntity> {
    AccesEntity findByCode(String code);
}
