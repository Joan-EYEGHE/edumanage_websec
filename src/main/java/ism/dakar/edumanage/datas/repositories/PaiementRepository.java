package ism.dakar.edumanage.datas.repositories;

import ism.dakar.edumanage.datas.entities.PaiementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface PaiementRepository extends JpaRepository<PaiementEntity, Long>,
        QuerydslPredicateExecutor<PaiementEntity> {

    Optional<PaiementEntity> findByReferenceTransaction(String referenceTransaction);
}

