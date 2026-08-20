package ism.dakar.edumanage.api.mappers;

import ism.dakar.edumanage.api.modeles.AuditLogResponseDto;
import ism.dakar.edumanage.datas.entities.AuditLogEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-24T22:42:54+0000",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class AuditLogMapperImpl implements AuditLogMapper {

    @Override
    public AuditLogEntity asEntity(AuditLogResponseDto dto) {
        if ( dto == null ) {
            return null;
        }

        AuditLogEntity auditLogEntity = new AuditLogEntity();

        auditLogEntity.setId( dto.getId() );
        auditLogEntity.setActif( dto.isActif() );
        auditLogEntity.setCreateAt( dto.getCreateAt() );
        auditLogEntity.setStatut( dto.getStatut() );
        auditLogEntity.setUserEmail( dto.getUserEmail() );
        auditLogEntity.setUserName( dto.getUserName() );
        auditLogEntity.setAction( dto.getAction() );
        auditLogEntity.setCible( dto.getCible() );
        auditLogEntity.setDetails( dto.getDetails() );
        auditLogEntity.setAdresseIp( dto.getAdresseIp() );
        auditLogEntity.setLoggedAt( dto.getLoggedAt() );

        return auditLogEntity;
    }

    @Override
    public AuditLogResponseDto asDto(AuditLogEntity entity) {
        if ( entity == null ) {
            return null;
        }

        AuditLogResponseDto auditLogResponseDto = new AuditLogResponseDto();

        auditLogResponseDto.setId( entity.getId() );
        auditLogResponseDto.setCreateAt( entity.getCreateAt() );
        auditLogResponseDto.setActif( entity.isActif() );
        auditLogResponseDto.setStatut( entity.getStatut() );
        auditLogResponseDto.setUserEmail( entity.getUserEmail() );
        auditLogResponseDto.setUserName( entity.getUserName() );
        auditLogResponseDto.setAction( entity.getAction() );
        auditLogResponseDto.setCible( entity.getCible() );
        auditLogResponseDto.setDetails( entity.getDetails() );
        auditLogResponseDto.setAdresseIp( entity.getAdresseIp() );
        auditLogResponseDto.setLoggedAt( entity.getLoggedAt() );

        return auditLogResponseDto;
    }

    @Override
    public List<AuditLogResponseDto> parse(List<AuditLogEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<AuditLogResponseDto> list = new ArrayList<AuditLogResponseDto>( entities.size() );
        for ( AuditLogEntity auditLogEntity : entities ) {
            list.add( asDto( auditLogEntity ) );
        }

        return list;
    }
}
