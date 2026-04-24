package ism.dakar.edumanage.services.impls;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import ism.dakar.edumanage.api.modeles.InscriptionDto;
import ism.dakar.edumanage.security.exceptions.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;

import ism.dakar.edumanage.api.mappers.PaiementMapper;
import ism.dakar.edumanage.api.modeles.PaiementResponseDto;
import ism.dakar.edumanage.datas.entities.PaiementEntity;
import ism.dakar.edumanage.datas.entities.QPaiementEntity;
import ism.dakar.edumanage.datas.repositories.PaiementRepository;
import ism.dakar.edumanage.security.exceptions.NotFoundException;
import ism.dakar.edumanage.services.interfaces.PaiementService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaiementServiceImpl implements PaiementService {

    private final PaiementRepository repository;
    private final PaiementMapper mapper;


    @Override
    public PaiementResponseDto create(PaiementResponseDto dto) {
        try {
            var entity = mapper.asEntity(dto);
            var entitySaved = repository.save(entity);
            return mapper.asDto(entitySaved);
        }catch (Exception ex){
            throw new BadRequestException("Une erreur est survenu lors de la creation");
        }
    }

    @Override
    public PaiementResponseDto update(PaiementResponseDto dto) {
        try {
            var optional = repository.findById(dto.getId());
            if (optional.isEmpty())
                throw new NotFoundException("Paiement introuvable");

            mapper.updateEntityFromDto(dto, optional.get());
            var entitySaved = repository.save(optional.get());
            return mapper.asDto(entitySaved);
        }catch (Exception ex){
            throw new BadRequestException("Une erreur est survenu lors de la mise a jour");
        }
    }

    @Override
    public PaiementResponseDto get(Long id) {
        return mapper.asDto(repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Paiement introuvable")));
    }

    @Override
    public void delete(Long id) {
        try {
            var optional = repository.findById(id);
            if (optional.isEmpty())
                throw new NotFoundException("Paiement introuvable");

            repository.deleteById(id);
        }catch (Exception ex){
            throw new BadRequestException("Une erreur est survenue");
        }
    }


    @Override
    public Page<PaiementResponseDto> getAll(Map<String, String> searchParams, Pageable pageable) {
        var booleanBuilder = new BooleanBuilder();
        buildSearch(searchParams, booleanBuilder);
        return repository.findAll(booleanBuilder, pageable).map(mapper::asDto);
    }

    @Override
    public List<PaiementResponseDto> getAll(Map<String, String> searchParams) {
        var booleanBuilder = new BooleanBuilder();
        buildSearch(searchParams, booleanBuilder);
        return mapper.parse((List<PaiementEntity>) repository.findAll(booleanBuilder));
    }

    @Override
    public long countAll(Map<String, String> searchParams) {
        var booleanBuilder = new BooleanBuilder();
        buildSearch(searchParams, booleanBuilder);
        return repository.count(booleanBuilder);
    }

    private void buildSearch(Map<String, String> searchParams, BooleanBuilder booleanBuilder) {
        if (Objects.nonNull(searchParams)) {
//            QPaiementEntity qPaiement = QPaiementEntity.paiementEntity;
//
//            if (searchParams.containsKey("inscriptionId")) {
//                builder.and(qPaiement.inscription.id.eq(Long.parseLong(searchParams.get("inscriptionId"))));
//            }
        }
    }

}
