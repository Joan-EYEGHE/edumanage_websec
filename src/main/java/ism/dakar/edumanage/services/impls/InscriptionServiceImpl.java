package ism.dakar.edumanage.services.impls;



import ism.dakar.edumanage.datas.repositories.InscriptionRepository;
import ism.dakar.edumanage.security.exceptions.BadRequestException;
import ism.dakar.edumanage.services.interfaces.InscriptionService;
import ism.dakar.edumanage.security.exceptions.NotFoundException;
import ism.dakar.edumanage.datas.entities.QInscriptionEntity;
import ism.dakar.edumanage.datas.entities.InscriptionEntity;
import ism.dakar.edumanage.security.datas.enums.StatutEnum;
import ism.dakar.edumanage.api.mappers.InscriptionMapper;
import ism.dakar.edumanage.datas.entities.PaiementEntity;
import ism.dakar.edumanage.api.modeles.InscriptionDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;
import java.util.Objects;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InscriptionServiceImpl implements InscriptionService {

    private final InscriptionRepository repository;
    private final InscriptionMapper mapper;

    @Override
    public InscriptionDto create(InscriptionDto dto) {
        try {
            var entity = mapper.asEntity(dto);
            entity.setDateInscription(LocalDate.now());
            entity.setStatut(StatutEnum.ACTIF);
            entity.setActif(true);
//            Paiement ===========
            PaiementEntity paiement = new PaiementEntity();
            paiement.setInscription(entity);
            paiement.setMontant(dto.getMontant());
            paiement.setModePaiement(dto.getModePaiement());
            paiement.setDatePaiement(LocalDate.now());
            paiement.setStatut(StatutEnum.ACTIF);
            paiement.setActif(true);
//
            entity.getPaiements().add(paiement);

            var entitySaved = repository.save(entity);
            return mapper.asDto(entitySaved);
        }catch (Exception ex){
            throw new BadRequestException("Une erreur est survenu lors de la création");
        }
    }

    @Override
    public InscriptionDto update(InscriptionDto dto) {
        try {
            var optional = repository.findById(dto.getId());
            if (optional.isEmpty())
                throw new NotFoundException("Inscription introuvable");

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
                throw new NotFoundException("Inscription introuvable");

            repository.deleteById(id);
        }catch (Exception ex){
            throw new BadRequestException("Une erreur est survenue");
        }
    }


    @Override
    public Page<InscriptionDto> getAll(Map<String, String> searchParams, Pageable pageable) {
        var booleanBuilder = new BooleanBuilder();
        buildSearch(searchParams, booleanBuilder);
        return repository.findAll(booleanBuilder, pageable).map(mapper::asDto);
    }

    @Override
    public List<InscriptionDto> getAll(Map<String, String> searchParams) {
        var booleanBuilder = new BooleanBuilder();
        buildSearch(searchParams, booleanBuilder);
        return mapper.parse((List<InscriptionEntity>) repository.findAll(booleanBuilder));
    }

    @Override
    public long countAll(Map<String, String> searchParams) {
        var booleanBuilder = new BooleanBuilder();
        buildSearch(searchParams, booleanBuilder);
        return repository.count(booleanBuilder);
    }

    @Override
    public InscriptionDto get(Long id) {
        return mapper.asDto(repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Inscription introuvable")));
    }

    
    private void buildSearch(Map<String, String> searchParams, BooleanBuilder booleanBuilder) {
        if (Objects.nonNull(searchParams)) {
//           QInscriptionEntity qInscription = QInscriptionEntity.inscriptionEntity;
//
//            if (searchParams.containsKey("apprenantId")) {
//                builder.and(qInscription.apprenant.id.eq(Long.parseLong(searchParams.get("apprenantId"))));
//            }
//            if (searchParams.containsKey("formationId")) {
//                builder.and(qInscription.formation.id.eq(Long.parseLong(searchParams.get("formationId"))));
        }
    }

}
