package ism.dakar.edumanage.security.api.mappers;

import ism.dakar.edumanage.security.api.models.AppUserDto;
import ism.dakar.edumanage.security.datas.entity.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface AppUserMapper extends EntityMapper<AppUserDto, AppUser> {

    @Override
    @Mapping(target = "roles", expression = "java(entity.getAccess().stream().map(a -> a.getCode()).toList())")
    @Mapping(target = "roleIds", expression = "java(entity.getAccess().stream().map(a -> a.getId()).toList())")
    AppUserDto asDto(AppUser entity);
}
