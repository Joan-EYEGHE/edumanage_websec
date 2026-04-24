package ism.dakar.edumanage.security.datas.repository;

import ism.dakar.edumanage.security.datas.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Integer> {
    AppUser findByEmailEquals(String login);
}
