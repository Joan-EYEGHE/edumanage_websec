package ism.dakar.edumanage.security.services.impls;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPAExpressions;
import ism.dakar.edumanage.security.api.mappers.AccessMapper;
import ism.dakar.edumanage.security.api.models.AccessDto;
import ism.dakar.edumanage.security.datas.enums.StatutEnum;
import ism.dakar.edumanage.security.datas.repository.AccessRepository;
import ism.dakar.edumanage.security.exceptions.NotFoundException;
import ism.dakar.edumanage.security.services.interfaces.AccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class AccessServiceImpl implements AccessService {
    private final AccessRepository repository;
    private final AccessMapper mapper;

    @Override
    public AccessDto create(AccessDto dto) {
        var entity = mapper.asEntity(dto);
        var entitySaved = repository.save(entity);
        return mapper.asDto(entitySaved);
    }

    @Override
    public AccessDto update(AccessDto dto) {
        var entity = mapper.asEntity(dto);
        var entitySaved = repository.save(entity);
        return mapper.asDto(entitySaved);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new NotFoundException("L'access `"+id+"` n'existe pas");

        var entity = repository.getReferenceById(id);
        repository.deleteById(id);
    }

    @Override
    public AccessDto getByCode(String code) {
        var entity = repository.findByCode(code);
        return mapper.asDto(entity);
    }

    @Override
    public AccessDto get(Long id) {
        if (!repository.existsById(id))
            throw new NotFoundException("L'access `"+id+"` n'existe pas");

        var entity = repository.findById(id).get();
        return mapper.asDto(entity);
    }

    @Override
    public Page<AccessDto> getAll(Map<String, String> searchParams, Pageable pageable) {
        var booleanBuilder = new BooleanBuilder();
        buildSearch(searchParams, booleanBuilder);
        return repository.findAll(booleanBuilder, pageable)
                .map(mapper::asDto);
    }

    @Override
    public List<AccessDto> getAll(Map<String, String> searchParams) {
        var booleanBuilder = new BooleanBuilder();
        buildSearch(searchParams, booleanBuilder);
        return StreamSupport.stream(
                        repository.findAll(booleanBuilder).spliterator(), false
                )
                .map(mapper::asDto)
                .collect(Collectors.toList());
    }

    @Override
    public long countAll(Map<String, String> searchParams) {
        var booleanBuilder = new BooleanBuilder();
        buildSearch(searchParams, booleanBuilder);
        return repository.count(booleanBuilder);
    }


    private void buildSearch(Map<String, String> searchParams, BooleanBuilder booleanBuilder) {

        if (Objects.nonNull(searchParams)) {
//            var qEntity = QAccesEntity.accesEntity;
//
//            if (searchParams.containsKey("visible"))
//                booleanBuilder.and(qEntity.visible.eq(Boolean.parseBoolean(searchParams.get("visible"))));
//
//            if (searchParams.containsKey("active"))
//                booleanBuilder.and(qEntity.active.eq(Boolean.parseBoolean(searchParams.get("active"))));
//
//            if (searchParams.containsKey("statut"))
//                booleanBuilder.and(qEntity.statut.eq(StatutEnum.valueOf(searchParams.get("statut"))));

        }
    }

}
