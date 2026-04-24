package ism.dakar.edumanage.api.mappers;

import org.mapstruct.Mapper;

import ism.dakar.edumanage.api.modeles.AuditLogResponseDto;
import ism.dakar.edumanage.datas.entities.AuditLogEntity;
import ism.dakar.edumanage.security.api.mappers.EntityMapper;

@Mapper(componentModel = "spring")
public interface AuditLogMapper extends EntityMapper<AuditLogResponseDto, AuditLogEntity> {
}
