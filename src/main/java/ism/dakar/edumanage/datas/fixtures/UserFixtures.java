package ism.dakar.edumanage.datas.fixtures;

import ism.dakar.edumanage.api.modeles.UserDto;
import ism.dakar.edumanage.datas.entities.UserEntity;
import ism.dakar.edumanage.datas.repositories.UserRepo;
import ism.dakar.edumanage.security.datas.enums.StatutEnum;
import ism.dakar.edumanage.security.datas.repository.AccessRepository;
import ism.dakar.edumanage.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
@Order(2)
public class UserFixtures implements CommandLineRunner {

    private final UserService service;
    private final AccessRepository accessRepository;
    private final PasswordEncoder encoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (service.getAll(null).size()==0){
            List<UserDto> users = new ArrayList<>();
//            Steeve
            UserDto steeve = new UserDto();
            steeve.setNom("Nguema");
            steeve.setPrenom("Steeve");
            steeve.setEmail("snguemaabessolo@gmail.com");
            steeve.setTelephone("+221781479603");
            steeve.setRoles(List.of("ADMINISTRATEUR".toUpperCase()));
            steeve.setActif(true);
            steeve.setStatut(StatutEnum.ACTIF);
            steeve.setPassword("steeve@admin1");
            users.add(steeve);
//            madi
            UserDto madi = new UserDto();
            madi.setNom("Diallo");
            madi.setPrenom("Madeleine");
            madi.setEmail("madodiallo2208@gmail.com");
            madi.setTelephone("+221 77 946 19 02");
            madi.setRoles(List.of("ADMINISTRATEUR".toUpperCase()));
            madi.setActif(true);
            madi.setStatut(StatutEnum.ACTIF);
            madi.setPassword("madi@admin2");
            users.add(madi);
//            joan
            UserDto joan = new UserDto();
            joan.setNom("Eyeghe");
            joan.setPrenom("Joan");
            joan.setEmail("joanarcher26@gmail.com");
            joan.setTelephone("+221 78 452 05 75");
            joan.setRoles(List.of("ADMINISTRATEUR".toUpperCase()));
            joan.setActif(true);
            joan.setStatut(StatutEnum.ACTIF);
            joan.setPassword("joan@admin2");
            users.add(joan);

            users.forEach(service::create);
        }
    }
}
