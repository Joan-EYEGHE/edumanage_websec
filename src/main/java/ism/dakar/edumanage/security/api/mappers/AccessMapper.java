package ism.dakar.edumanage.security.api.mappers;

import ism.dakar.edumanage.security.api.models.AccessDto;
import ism.dakar.edumanage.security.datas.entity.AccesEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface AccessMapper extends EntityMapper<AccessDto, AccesEntity> {
}
