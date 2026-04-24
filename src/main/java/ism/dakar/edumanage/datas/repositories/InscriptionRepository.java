package ism.dakar.edumanage.datas.repositories;

import ism.dakar.edumanage.datas.entities.InscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface InscriptionRepository extends JpaRepository<InscriptionEntity, Long>,
        QuerydslPredicateExecutor<InscriptionEntity> {

            
}

