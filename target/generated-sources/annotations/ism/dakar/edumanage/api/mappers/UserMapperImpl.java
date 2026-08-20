package ism.dakar.edumanage.api.mappers;

import ism.dakar.edumanage.api.modeles.UserDto;
import ism.dakar.edumanage.datas.entities.UserEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-24T22:42:55+0000",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserEntity asEntity(UserDto dto) {
        if ( dto == null ) {
            return null;
        }

        UserEntity userEntity = new UserEntity();

        userEntity.setId( dto.getId() );
        userEntity.setActif( dto.isActif() );
        userEntity.setCreateAt( dto.getCreateAt() );
        userEntity.setStatut( dto.getStatut() );
        userEntity.setEmail( dto.getEmail() );
        userEntity.setPassword( dto.getPassword() );
        userEntity.setNom( dto.getNom() );
        userEntity.setPrenom( dto.getPrenom() );
        userEntity.setTelephone( dto.getTelephone() );

        return userEntity;
    }

    @Override
    public List<UserDto> parse(List<UserEntity> entities) {
        if ( entities == null ) {
            return null;
        }

        List<UserDto> list = new ArrayList<UserDto>( entities.size() );
        for ( UserEntity userEntity : entities ) {
            list.add( asDto( userEntity ) );
        }

        return list;
    }

    @Override
    public UserDto asDto(UserEntity entity) {
        if ( entity == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setId( entity.getId() );
        userDto.setCreateAt( entity.getCreateAt() );
        userDto.setActif( entity.isActif() );
        userDto.setStatut( entity.getStatut() );
        userDto.setNom( entity.getNom() );
        userDto.setPrenom( entity.getPrenom() );
        userDto.setEmail( entity.getEmail() );
        userDto.setTelephone( entity.getTelephone() );

        userDto.setRoles( entity.getAccess().stream().map(a->a.getCode()).toList() );
        userDto.setRoleIds( entity.getAccess().stream().map(a -> a.getId()).toList() );

        return userDto;
    }

    @Override
    public void updateEntityFromDto(UserDto dto, UserEntity entity) {
        if ( dto == null ) {
            return;
        }

        entity.setActif( dto.isActif() );
        entity.setCreateAt( dto.getCreateAt() );
        entity.setStatut( dto.getStatut() );
        entity.setEmail( dto.getEmail() );
        entity.setNom( dto.getNom() );
        entity.setPrenom( dto.getPrenom() );
        entity.setTelephone( dto.getTelephone() );
    }
}
