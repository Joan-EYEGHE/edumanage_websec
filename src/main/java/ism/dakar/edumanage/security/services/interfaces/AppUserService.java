package ism.dakar.edumanage.security.services.interfaces;


import ism.dakar.edumanage.security.api.models.AppUserDto;
import ism.dakar.edumanage.security.api.models.LoginDto;

public interface AppUserService {
    AppUserDto getByLogin(String login);

    AppUserDto login(LoginDto loginUserDto);
}
