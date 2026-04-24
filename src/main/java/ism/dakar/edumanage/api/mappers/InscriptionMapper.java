package ism.dakar.edumanage.api.mappers;

import ism.dakar.edumanage.api.modeles.FormationDto;
import ism.dakar.edumanage.datas.entities.FormationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ism.dakar.edumanage.api.modeles.InscriptionDto;
import ism.dakar.edumanage.datas.entities.InscriptionEntity;
import ism.dakar.edumanage.security.api.mappers.EntityMapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = { UserMapper.class, FormationMapper.class })
public interface InscriptionMapper extends EntityMapper<InscriptionDto, InscriptionEntity> {

    @Override
    @Mapping(source = "apprenant.id", target = "apprenantId")
    @Mapping(source = "apprenant.nom", target = "apprenantNom")
    @Mapping(source = "formation.id", target = "formationId")
    @Mapping(source = "formation.titre", target = "formationTitre")
    InscriptionDto asDto(InscriptionEntity entity);

    @Override
    @Mapping(target = "apprenant.id", source = "apprenantId")
    @Mapping(target = "formation.id", source = "formationId")
    InscriptionEntity asEntity(InscriptionDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "formation", ignore = true)
    @Mapping(target = "apprenant", ignore = true)
    void updateEntityFromDto(InscriptionDto dto, @MappingTarget InscriptionEntity entity);
}
