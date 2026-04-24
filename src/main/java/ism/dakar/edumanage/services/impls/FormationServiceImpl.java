package ism.dakar.edumanage.services.impls;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import ism.dakar.edumanage.security.exceptions.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import ism.dakar.edumanage.api.mappers.FormationMapper;
import ism.dakar.edumanage.api.modeles.FormationDto;
import ism.dakar.edumanage.datas.entities.FormationEntity;
import ism.dakar.edumanage.datas.entities.QFormationEntity;
import ism.dakar.edumanage.datas.repositories.FormationRepository;
import ism.dakar.edumanage.security.exceptions.NotFoundException;
import ism.dakar.edumanage.services.interfaces.FormationService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FormationServiceImpl implements FormationService {

    private final FormationRepository repository;
    private final FormationMapper mapper;

    @Override
    public FormationDto create(FormationDto dto) {
        try {
            var entity = mapper.asEntity(dto);
            var entitySaved = repository.save(entity);
            return mapper.asDto(entitySaved);
        }catch (Exception ex){
            throw new BadRequestException("Une erreur est survenu lors de la création");
        }
    }

    @Override
    public FormationDto update(FormationDto dto) {
        try {
            var optional = repository.findById(dto.getId());
            if (optional.isEmpty())
                throw new NotFoundException("Formation introuvable");

            mapper.updateEntityFromDto(dto, optional.get());
            var entitySaved = repository.save(optional.get());
            return mapper.asDto(entitySaved);
        }catch (Exception ex){
            throw new BadRequestException("Une erreur est survenu lors de la mise a jour");
        }
    }

    @Override
    public void delete(Long id) {
        try {
            var optional = repository.findById(id);
            if (optional.isEmpty())
                throw new NotFoundException("Formation introuvable");

            repository.deleteById(id);
        }catch (Exception ex){
            throw new BadRequestException("Une erreur est survenue");
        }
    }

    @Override
    public FormationDto get(Long id) {
        return mapper.asDto(
                repository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Formation introuvable")));
    }

    @Override
    public Page<FormationDto> getAll(Map<String, String> searchParams, Pageable pageable) {
        var booleanBuilder = new BooleanBuilder();
        buildSearch(searchParams, booleanBuilder);
        return repository.findAll(booleanBuilder, pageable).map(mapper::asDto);
    }

    @Override
    public List<FormationDto> getAll(Map<String, String> searchParams) {
        var booleanBuilder = new BooleanBuilder();
        buildSearch(searchParams, booleanBuilder);
        return mapper.parse((List<FormationEntity>) repository.findAll(booleanBuilder));
    }

    @Override
    public long countAll(Map<String, String> searchParams) {
        var booleanBuilder = new BooleanBuilder();
        buildSearch(searchParams, booleanBuilder);
        return repository.count(booleanBuilder);
    }


    private void buildSearch(Map<String, String> searchParams, BooleanBuilder booleanBuilder) {
        if (Objects.nonNull(searchParams)) {
//        QFormationEntity qFormation = QFormationEntity.FormationEntity;
//
//        if (searchParams.containsKey("userEmail")) {
//            builder.and(qFormation.userEmail.containsIgnoreCase(searchParams.get("userEmail")));
//        }
//        if (searchParams.containsKey("action")) {
//            builder.and(qFormation.action.containsIgnoreCase(searchParams.get("action")));
//        }
        }
    }

}
