package ism.dakar.edumanage.api.mappers;

import ism.dakar.edumanage.api.modeles.FormationDto;
import ism.dakar.edumanage.datas.entities.FormationEntity;
import ism.dakar.edumanage.security.api.mappers.EntityMapper;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface FormationMapper extends EntityMapper<FormationDto, FormationEntity> {

    @Override 
    @Mapping(source = "formateur.id", target = "formateurId")
    @Mapping(source = "formateur.nom", target = "formateurNom")
    FormationDto asDto(FormationEntity entity);

    @Override
    @Mapping(target = "formateur.id", source = "formateurId")
    FormationEntity asEntity(FormationDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "formateur", ignore = true)
    @Mapping(target = "inscriptions", ignore = true)
    void updateEntityFromDto(FormationDto dto, @MappingTarget FormationEntity entity);
}
