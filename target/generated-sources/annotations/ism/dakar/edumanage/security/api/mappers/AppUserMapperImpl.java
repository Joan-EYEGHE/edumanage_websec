package ism.dakar.edumanage.security.api.mappers;

import ism.dakar.edumanage.security.api.models.AppUserDto;
import ism.dakar.edumanage.security.datas.entity.AppUser;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-24T22:42:55+0000",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class AppUserMapperImpl implements AppUserMapper {

    @Override
    public AppUser asEntity(AppUserDto dto) {
        if ( dto == null ) {
            return null;
        }

        AppUser appUser = new AppUser();

        appUser.setId( dto.getId() );
        appUser.setActif( dto.isActif() );
        appUser.setCreateAt( dto.getCreateAt() );
        appUser.setStatut( dto.getStatut() );
        appUser.setEmail( dto.getEmail() );
        appUser.setPassword( dto.getPassword() );

        return appUser;
    }

    @Override
    public List<AppUserDto> parse(List<AppUser> entities) {
        if ( entities == null ) {
            return null;
        }

        List<AppUserDto> list = new ArrayList<AppUserDto>( entities.size() );
        for ( AppUser appUser : entities ) {
            list.add( asDto( appUser ) );
        }

        return list;
    }

    @Override
    public AppUserDto asDto(AppUser entity) {
        if ( entity == null ) {
            return null;
        }

        AppUserDto appUserDto = new AppUserDto();

        appUserDto.setId( entity.getId() );
        appUserDto.setCreateAt( entity.getCreateAt() );
        appUserDto.setActif( entity.isActif() );
        appUserDto.setStatut( entity.getStatut() );
        appUserDto.setEmail( entity.getEmail() );
        appUserDto.setPassword( entity.getPassword() );

        appUserDto.setRoles( entity.getAccess().stream().map(a -> a.getCode()).toList() );
        appUserDto.setRoleIds( entity.getAccess().stream().map(a -> a.getId()).toList() );

        return appUserDto;
    }
}
