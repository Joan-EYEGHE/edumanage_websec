package ism.dakar.edumanage.datas.repositories;

import ism.dakar.edumanage.datas.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface UserRepo extends JpaRepository<UserEntity, Long>, QuerydslPredicateExecutor<UserEntity> {
    UserEntity findByEmailEquals(String login);
    UserEntity findByTelephoneEqualsIgnoreCase(String contact);
}
