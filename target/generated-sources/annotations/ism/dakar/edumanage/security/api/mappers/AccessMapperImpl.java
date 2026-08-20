package ism.dakar.edumanage.security.api.mappers;

import ism.dakar.edumanage.security.api.models.AccessDto;
import ism.dakar.edumanage.security.datas.entity.AccesEntity;
import ism.dakar.edumanage.security.datas.entity.AccesEntity.AccesEntityBuilder;
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
public class AccessMapperImpl implements AccessMapper {

    @Override
    public AccesEntity asEntity(AccessDto dto) {
        if ( dto == null ) {
            return null;
        }

        AccesEntityBuilder accesEntity = AccesEntity.builder();

        accesEntity.code( dto.getCode() );
        accesEntity.description( dto.getDescription() );
        accesEntity.visible( dto.isVisible() );

        return accesEntity.build();
    }

    @Override
    public AccessDto asDto(AccesEntity entity) {
        if ( entity == null ) {
            return null;
        }

        AccessDto accessDto = new AccessDto();

        accessDto.setId( entity.getId() );
        accessDto.setCreateAt( entity.getCreateAt() );
        accessDto.setActif( entity.isActif() );
        accessDto.setStatut( entity.getStatut() );
        accessDto.setCode( entity.getCode() );
        accessDto.setDescription( entity.getDescription() );
        accessDto.setVisible( entity.isVisible() );

        return accessDto;
    }

    @Override
    public List<AccessDto> parse(List<AccesEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<AccessDto> list = new ArrayList<AccessDto>( entities.size() );
        for ( AccesEntity accesEntity : entities ) {
            list.add( asDto( accesEntity ) );
        }

        return list;
    }
}
