package ism.dakar.edumanage.security.services.interfaces;


import ism.dakar.edumanage.security.api.models.AccessDto;

public interface AccessService extends IService<AccessDto> {
    AccessDto getByCode(String code);
}
