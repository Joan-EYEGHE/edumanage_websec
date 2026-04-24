package ism.dakar.edumanage.api.mappers;

import ism.dakar.edumanage.api.modeles.UserDto;
import ism.dakar.edumanage.datas.entities.UserEntity;
import ism.dakar.edumanage.security.api.mappers.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;


import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", imports = {Collectors.class})
public interface UserMapper extends EntityMapper<UserDto, UserEntity> {

    @Override
    @Mapping(target = "roles", expression = "java(entity.getAccess().stream().map(a->a.getCode()).toList())")
    @Mapping(target = "roleIds", expression = "java(entity.getAccess().stream().map(a -> a.getId()).toList())")
    @Mapping(target = "password", ignore = true)
    UserDto asDto(UserEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    void updateEntityFromDto(UserDto dto, @MappingTarget UserEntity entity);

}
