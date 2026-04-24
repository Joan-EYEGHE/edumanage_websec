package ism.dakar.edumanage.security.services.impls;

import ism.dakar.edumanage.security.api.mappers.AppUserMapper;
import ism.dakar.edumanage.security.api.models.AppUserDto;
import ism.dakar.edumanage.security.api.models.LoginDto;
import ism.dakar.edumanage.security.datas.repository.AppUserRepository;
import ism.dakar.edumanage.security.exceptions.NotFoundException;
import ism.dakar.edumanage.security.services.interfaces.AppUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;


@Service
@RequiredArgsConstructor
@Slf4j
public class AppUserServiceImpl implements AppUserService {
    private final AppUserRepository repository;
    private final AppUserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AppUserDto getByLogin(String login) {
        var entity= repository.findByEmailEquals(login);
        return mapper.asDto(entity);
    }

    @Override
    public AppUserDto login(LoginDto loginDto) {
        log.info("Tentative d'authentufication pour "+loginDto.getEmail());
        var user_ = repository.findByEmailEquals(loginDto.getEmail());

        if (user_ == null  || !user_.getEmail().equals(loginDto.getEmail())) {
            log.error("Authentification echoue car l'utilisateur "+loginDto.getEmail()+" n'existe pas");
            throw new NotFoundException("Utilisateur non trouvé");
        }

        if (passwordEncoder.matches(CharBuffer.wrap(loginDto.getPassword()), user_.getPassword())) {
            var dto = mapper.asDto(user_);
            log.info("Utilisateur "+loginDto.getEmail()+" authentifié");
            return dto;
        }
        log.error("Authentification echoué car le mot de passe de l'utilisateur "+loginDto.getEmail()+" est incorrect");
        throw new NotFoundException("Mot de passe incorrect");
    }

}
