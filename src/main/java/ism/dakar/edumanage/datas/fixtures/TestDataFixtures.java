package ism.dakar.edumanage.datas.fixtures;

import ism.dakar.edumanage.api.modeles.UserDto;
import ism.dakar.edumanage.datas.repositories.UserRepo;
import ism.dakar.edumanage.security.datas.enums.Role;
import ism.dakar.edumanage.security.datas.enums.StatutEnum;
import ism.dakar.edumanage.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Profile("dev")
@RequiredArgsConstructor
@Order(3)
public class TestDataFixtures implements CommandLineRunner {

    private final UserService service;
    private final UserRepo userRepo;

    @Override
    @Transactional
    public void run(String... args) {
        seedIfAbsent("test.gestionnaire@edumanage.local", "Gestionnaire", "Test",
                "+33000000001", Role.GESTIONNAIRE.name());
        seedIfAbsent("test.formateur@edumanage.local", "Formateur", "Test",
                "+33000000002", Role.FORMATEUR.name());
        seedIfAbsent("test.apprenant@edumanage.local", "Apprenant", "Test",
                "+33000000003", Role.APPRENANT.name());
    }

    private void seedIfAbsent(String email, String nom, String prenom,
                               String telephone, String role) {
        if (userRepo.findByEmailEquals(email) != null) return;
        UserDto dto = new UserDto();
        dto.setNom(nom);
        dto.setPrenom(prenom);
        dto.setEmail(email);
        dto.setTelephone(telephone);
        dto.setRoles(List.of(role));
        dto.setActif(true);
        dto.setStatut(StatutEnum.ACTIF);
        dto.setPassword("Test1234!");
        service.create(dto);
    }
}
