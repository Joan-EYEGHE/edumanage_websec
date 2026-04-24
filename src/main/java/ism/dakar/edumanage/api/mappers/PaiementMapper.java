package ism.dakar.edumanage.api.mappers;

import ism.dakar.edumanage.api.modeles.InscriptionDto;
import ism.dakar.edumanage.datas.entities.InscriptionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ism.dakar.edumanage.api.modeles.PaiementResponseDto;
import ism.dakar.edumanage.datas.entities.PaiementEntity;
import ism.dakar.edumanage.security.api.mappers.EntityMapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = { InscriptionMapper.class })
public interface PaiementMapper extends EntityMapper<PaiementResponseDto, PaiementEntity> {

    @Override
    @Mapping(source = "inscription.id", target = "inscriptionId")
    @Mapping(source = "inscription.apprenant.nom", target = "apprenantNom")
    PaiementResponseDto asDto(PaiementEntity entity);

    @Override
    @Mapping(target = "inscription.id", source = "inscriptionId")
    PaiementEntity asEntity(PaiementResponseDto dto);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "inscription", ignore = true)
    void updateEntityFromDto(PaiementResponseDto dto, @MappingTarget PaiementEntity entity);
}
