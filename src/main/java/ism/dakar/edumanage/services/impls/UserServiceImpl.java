package ism.dakar.edumanage.services.impls;

import com.querydsl.core.BooleanBuilder;
import ism.dakar.edumanage.api.mappers.UserMapper;
import ism.dakar.edumanage.api.modeles.UserDto;
import ism.dakar.edumanage.datas.repositories.UserRepo;
import ism.dakar.edumanage.security.api.mappers.AccessMapper;
import ism.dakar.edumanage.security.api.models.LoginDto;
import ism.dakar.edumanage.security.datas.repository.AccessRepository;
import ism.dakar.edumanage.security.exceptions.BadRequestException;
import ism.dakar.edumanage.security.exceptions.DuplicateReferenceException;
import ism.dakar.edumanage.security.exceptions.NotFoundException;
import ism.dakar.edumanage.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.nio.CharBuffer;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepo repository;
    private final AccessRepository accessRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;


    @Override
    public UserDto login(LoginDto loginDto) {
        log.info("Tentative d'authentufication pour "+loginDto.getEmail());
        var user_ = repository.findByEmailEquals(loginDto.getEmail());

        if (user_ == null  || !user_.getEmail().equals(loginDto.getEmail())) {
            log.error("Authentification echoue car l'utilisateur "+loginDto.getEmail()+" n'existe pas");
            throw new NotFoundException("Identifiant et/ou Mot de passe incorrect");
        }

        if (passwordEncoder.matches(CharBuffer.wrap(loginDto.getPassword()), user_.getPassword())) {
            var dto = mapper.asDto(user_);
            log.info("Utilisateur "+loginDto.getEmail()+" authentifié");
            return dto;
        }
        log.error("Authentification echoué car le mot de passe de l'utilisateur "+loginDto.getEmail()+" est incorrect");
        throw new NotFoundException("Identifiant et/ou Mot de passe incorrect");
    }


    @Override
    public UserDto getByLogin(String username) {
        var entity = repository.findByEmailEquals(username);
        return mapper.asDto(entity);
    }

    @Override
    public UserDto create(UserDto dto) {
        try {
            var entity = mapper.asEntity(dto);
            if (!dto.getRoles().isEmpty()){
                dto.getRoles().forEach(code -> {
                    entity.getAccess().add(accessRepository.findByCode(code));
                });
            }

            if (entity.getAccess().isEmpty())
                throw new BadRequestException("Veuillez renseiner au moins un acces a l'utilisateur");

            entity.setPassword(passwordEncoder.encode(dto.getPassword()));

            var entitySaved = repository.save(entity);
            return mapper.asDto(entitySaved);
        }catch (Exception ex){
            ex.printStackTrace();
            throw new BadRequestException("Une erreur est survenu lors de la création");
        }
    }

    @Override
    public UserDto update(UserDto dto) {
        try {
            var optional = repository.findById(dto.getId());

            if (optional.isEmpty())
                throw new NotFoundException("L'utilisateur `"+dto.getId()+"` n'existe pas");

            var entity = optional.get();

            if (!dto.getEmail().isEmpty() && dto.getEmail().length()>=6) {
                if (!entity.getEmail().equals(dto.getEmail()) && repository.findByEmailEquals(dto.getEmail().strip())!=null)
                    throw new DuplicateReferenceException("L'identifiant' `"+dto.getEmail()+"` n'est pas disponible");
            }else{
                throw new BadRequestException("L'identifiant n'est pas valide");
            }

            mapper.updateEntityFromDto(dto, entity);

            var entitySaved = repository.save(entity);
            return mapper.asDto(entitySaved);

        }catch (Exception ex){
            ex.printStackTrace();
            throw new BadRequestException("Une erreur est survenu lors de la mise a jour");
        }
    }

    @Override
    public void delete(Long id) {
        try {
            var optional = repository.findById(id);
            if (optional.isEmpty())
                throw new NotFoundException("Utilisateur introuvable ou a été supprimé");

            optional.get().setActif(false);
            optional.get().setEmail(optional.get().getEmail() +" - "+ Instant.now().toString());
            repository.save(optional.get());
        }catch (Exception ex){
            ex.printStackTrace();
            throw new BadRequestException("Une erreur est survenu lors de la suppression");
        }
    }

    @Override
    public UserDto get(Long id) {
        try {
            var optional = repository.findById(id);
            if (optional.isEmpty())
                throw new NotFoundException("Utilisateur introuvable ou a été supprimé");

            var entity = repository.findById(id).get();
            return mapper.asDto(entity);

        }catch (Exception ex){
            ex.printStackTrace();
            throw new BadRequestException("Une erreur est survenu lors du chargement");
        }
    }

    @Override
    public Page<UserDto> getAll(Map<String, String> searchParams, Pageable pageable) {

        try {
            var booleanBuilder = new BooleanBuilder();
            buildSearch(searchParams, booleanBuilder);
            return repository.findAll(booleanBuilder, pageable)
                    .map(mapper::asDto);
        }catch (Exception ex){
            ex.printStackTrace();
            throw new BadRequestException("Une erreur est survenu lors du chargement");
        }
    }

    @Override
    public List<UserDto> getAll(Map<String, String> searchParams) {

        try {
            var booleanBuilder = new BooleanBuilder();
            buildSearch(searchParams, booleanBuilder);
            return StreamSupport.stream(
                            repository.findAll(booleanBuilder).spliterator(), false
                    )
                    .map(mapper::asDto)
                    .collect(Collectors.toList());
        }catch (Exception ex){
            ex.printStackTrace();
            throw new BadRequestException("Une erreur est survenu lors du chargement");
        }
    }

    @Override
    public long countAll(Map<String, String> searchParams) {
        try {
            var booleanBuilder = new BooleanBuilder();
            buildSearch(searchParams, booleanBuilder);
            return repository.count(booleanBuilder);
        }catch (Exception ex){
            ex.printStackTrace();
            throw new BadRequestException("Une erreur est survenu lors du calcul total");
        }
    }


    private void buildSearch(Map<String, String> searchParams, BooleanBuilder booleanBuilder) {
        if (Objects.nonNull(searchParams)) {
//            var qEntity = QUserEntity.userEntity;
//
//            if (searchParams.containsKey("actif"))
//                booleanBuilder.and(qEntity.actif.eq(Boolean.parseBoolean(searchParams.get("active"))));
//
//            if (searchParams.containsKey("statut"))
//                booleanBuilder.and(qEntity.statut.eq(StatutEnum.valueOf(searchParams.get("statut"))));
        }
    }

}
