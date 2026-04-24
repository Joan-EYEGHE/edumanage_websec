package ism.dakar.edumanage.datas.repositories;

import ism.dakar.edumanage.datas.entities.FormationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;


public interface FormationRepository extends JpaRepository<FormationEntity, Long>,
        QuerydslPredicateExecutor<FormationEntity> {

}
